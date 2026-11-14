package restaurante.datos;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Actualiza las bases de datos creadas con versiones anteriores de la
 * aplicación. Todas las sentencias son idempotentes: comprueban el estado
 * antes de modificarlo, por lo que se pueden ejecutar en cada arranque.
 */
public final class MigradorBD {

    private static final Logger LOG = Logger.getLogger(MigradorBD.class.getName());

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
                    + " ALTER TABLE DetOrdenDeVenta ADD precioUnit float NULL",
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

    static void aplicar(Connection c) throws SQLException {
        try (Statement st = c.createStatement()) {
            for (String sentencia : SENTENCIAS) {
                st.execute(sentencia);
            }
        }
    }
}
