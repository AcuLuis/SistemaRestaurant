package restaurante.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Actualiza las bases de datos creadas con versiones anteriores de la
 * aplicación. Todas las sentencias son idempotentes: comprueban el estado
 * antes de modificarlo, por lo que se pueden ejecutar en cada arranque.
 */
public final class MigradorBD {

    private static final Logger LOG = Logger.getLogger(MigradorBD.class.getName());

    /**
     * Tablas cuyo identificador se genera con una secuencia de SQL Server
     * (nombre de la secuencia: seq_ + tabla).
     */
    static final Map<String, String> SECUENCIAS;

    static {
        Map<String, String> m = new LinkedHashMap<>();
        m.put("Area", "areaID");
        m.put("Rol", "rolID");
        m.put("Insumo", "insumoID");
        m.put("Producto", "productoID");
        m.put("Cliente", "clienteID");
        m.put("Contrato", "contratoID");
        m.put("Empleado", "empleadoID");
        m.put("Usuario", "usuarioID");
        m.put("CabOrdenDeVenta", "ordenDeVentaID");
        m.put("Pedidos", "idPedidos");
        m.put("Mesa", "mesaID");
        m.put("CabReceta", "recetaID");
        m.put("CabOrdenDeProduccionSemanal", "ordenProduccionID");
        m.put("Reserva", "reservaID");
        SECUENCIAS = Collections.unmodifiableMap(m);
    }

    static final List<String> SENTENCIAS = List.of(
            // Los hashes de contraseña necesitan más espacio que el texto plano anterior.
            "IF COL_LENGTH('Usuario', 'contraseña') < 100"
                    + " ALTER TABLE Usuario ALTER COLUMN contraseña varchar(100)",
            // Longitudes insuficientes para correos, direcciones y descripciones reales.
            "IF COL_LENGTH('Cliente', 'correo') < 100 ALTER TABLE Cliente ALTER COLUMN correo varchar(100)",
            "IF COL_LENGTH('Cliente', 'direccion') < 100 ALTER TABLE Cliente ALTER COLUMN direccion varchar(100)",
            "IF COL_LENGTH('Pedidos', 'ubic') < 100 ALTER TABLE Pedidos ALTER COLUMN ubic varchar(100)",
            "IF COL_LENGTH('Insumo', 'descripInsumo') < 60"
                    + " ALTER TABLE Insumo ALTER COLUMN descripInsumo varchar(60)",
            // Precio aplicado en cada venta, para que el historial no cambie al modificar precios.
            "IF COL_LENGTH('DetOrdenDeVenta', 'precioUnit') IS NULL"
                    + " ALTER TABLE DetOrdenDeVenta ADD precioUnit decimal(10, 2) NULL",
            // Importes en DECIMAL: float introduce errores de redondeo en cantidades de dinero.
            convertirADecimal("Producto", "precioUnit", "decimal(10, 2)"),
            convertirADecimal("Insumo", "precioUnit", "decimal(10, 2)"),
            convertirADecimal("DetOrdenDeVenta", "precioUnit", "decimal(10, 2)"),
            convertirADecimal("Contrato", "sueldo", "decimal(10, 2)"),
            // Cantidades de insumos y recetas con decimales (por ejemplo, 0.250 kg).
            convertirADecimal("Insumo", "cantidad", "decimal(12, 3)"),
            convertirADecimal("DetReceta", "cantidad", "decimal(12, 3)"),
            // El teléfono es un texto: admite prefijos y ceros iniciales.
            "IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('Cliente') AND name = 'telefono'"
                    + " AND system_type_id = TYPE_ID('int')) ALTER TABLE Cliente ALTER COLUMN telefono varchar(15)",
            // Existencias nunca negativas (WITH NOCHECK no valida los datos ya existentes).
            "IF OBJECT_ID('ckProductoCantidad') IS NULL"
                    + " ALTER TABLE Producto WITH NOCHECK ADD CONSTRAINT ckProductoCantidad CHECK (cantidad >= 0)",
            "IF OBJECT_ID('ckInsumoCantidad') IS NULL"
                    + " ALTER TABLE Insumo WITH NOCHECK ADD CONSTRAINT ckInsumoCantidad CHECK (cantidad >= 0)",
            // Baja lógica de empleados.
            "IF COL_LENGTH('Empleado', 'estado') IS NULL"
                    + " ALTER TABLE Empleado ADD estado int NOT NULL CONSTRAINT dfEmpleadoEstado DEFAULT 1",
            // Estado de las mesas: 0 = disponible, 1 = fuera de servicio.
            "IF EXISTS (SELECT 1 FROM Mesa WHERE estado IS NULL) UPDATE Mesa SET estado = 0 WHERE estado IS NULL",
            "IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('Mesa') AND name = 'estado'"
                    + " AND is_nullable = 1) ALTER TABLE Mesa ALTER COLUMN estado int NOT NULL",
            // Anulación de ventas.
            "IF COL_LENGTH('CabOrdenDeVenta', 'estado') IS NULL"
                    + " ALTER TABLE CabOrdenDeVenta ADD estado int NOT NULL CONSTRAINT dfVentaEstado DEFAULT 1,"
                    + " motivoAnulacion varchar(200) NULL, usuarioAnulaID int NULL, fechaAnulacion datetime NULL",
            // Órdenes de producción.
            "IF COL_LENGTH('CabOrdenDeProduccionSemanal', 'estado') IS NULL"
                    + " ALTER TABLE CabOrdenDeProduccionSemanal ADD estado int NOT NULL CONSTRAINT dfProduccionEstado"
                    + " DEFAULT 0, fechaCompletada datetime NULL",
            // Reservas.
            "IF COL_LENGTH('Reserva', 'hora') IS NULL"
                    + " ALTER TABLE Reserva ADD hora time NULL, personas int NULL,"
                    + " mesaID int NULL CONSTRAINT fkReservaMesa REFERENCES Mesa (mesaID), observacion varchar(200) NULL",
            // Catálogos imprescindibles para registrar productos e insumos.
            "IF NOT EXISTS (SELECT 1 FROM Plato)"
                    + " INSERT INTO Plato (idPlato, descPlato) VALUES (30, 'entrada'), (31, 'menú'), (32, 'sopa')",
            "IF NOT EXISTS (SELECT 1 FROM TipProducto)"
                    + " INSERT INTO TipProducto (idTipProducto, descripcion) VALUES (40, 'envasado'), (41, 'preparado')",
            "IF NOT EXISTS (SELECT 1 FROM TipInsumo)"
                    + " INSERT INTO TipInsumo (idTipInsumo, descripcion) VALUES (10, 'perecible'), (11, 'No perecible')");

    private MigradorBD() {
    }

    /** Aplica las actualizaciones pendientes en una transacción. */
    public static void actualizar() {
        Jdbc.transaccion(c -> {
            aplicar(c);
            return null;
        });
        LOG.log(Level.INFO, "Esquema de base de datos verificado");
    }

    /** Aplica las migraciones y sincroniza las secuencias con la conexión indicada. */
    public static void aplicar(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            for (String sentencia : SENTENCIAS) {
                st.execute(sentencia);
            }
            for (String sentencia : sentenciasSecuencias()) {
                st.execute(sentencia);
            }
        }
    }

    /**
     * Crea las secuencias que falten y, si hay filas con identificadores
     * mayores que el valor actual (por ejemplo, tras cargar datos de ejemplo
     * con identificadores fijos), las adelanta.
     */
    static List<String> sentenciasSecuencias() {
        List<String> sentencias = new ArrayList<>();
        for (Map.Entry<String, String> e : SECUENCIAS.entrySet()) {
            String tabla = e.getKey();
            String columna = e.getValue();
            String secuencia = "seq_" + tabla;
            sentencias.add("IF OBJECT_ID('" + secuencia + "', 'SO') IS NULL BEGIN"
                    + " DECLARE @inicio bigint = (SELECT ISNULL(MAX(" + columna + "), 0) + 1 FROM " + tabla + ");"
                    + " DECLARE @sql nvarchar(300) = N'CREATE SEQUENCE " + secuencia + " AS int START WITH '"
                    + " + CAST(@inicio AS nvarchar(20));"
                    + " EXEC sp_executesql @sql; END");
            sentencias.add("BEGIN"
                    + " DECLARE @maximo bigint = (SELECT ISNULL(MAX(" + columna + "), 0) FROM " + tabla + ");"
                    + " DECLARE @actual bigint = (SELECT CAST(current_value AS bigint) FROM sys.sequences"
                    + " WHERE name = '" + secuencia + "');"
                    + " IF @maximo >= @actual BEGIN"
                    + " DECLARE @sql nvarchar(300) = N'ALTER SEQUENCE " + secuencia + " RESTART WITH '"
                    + " + CAST(@maximo + 1 AS nvarchar(20));"
                    + " EXEC sp_executesql @sql; END END");
        }
        return sentencias;
    }

    private static String convertirADecimal(String tabla, String columna, String tipo) {
        return "IF EXISTS (SELECT 1 FROM sys.columns WHERE object_id = OBJECT_ID('" + tabla + "') AND name = '"
                + columna + "' AND system_type_id IN (TYPE_ID('float'), TYPE_ID('real'), TYPE_ID('int')))"
                + " ALTER TABLE " + tabla + " ALTER COLUMN " + columna + " " + tipo + " NULL";
    }
}
