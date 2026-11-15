package restaurante.integracion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Map;
import org.junit.jupiter.api.Test;
import restaurante.dao.AreaDao;
import restaurante.dao.ClienteDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.UsuarioDao;
import restaurante.datos.ConexionBD;
import restaurante.datos.DatosException;
import restaurante.datos.InstaladorBD;
import restaurante.datos.Jdbc;
import restaurante.datos.MigradorBD;
import restaurante.datos.ScriptSql;
import restaurante.modelo.Area;
import restaurante.reportes.Reportes;
import restaurante.seguridad.TipoUsuario;

class InstalacionYMigracionIT extends BaseDatosIT {

    private static final Object[][] REPORTES = {
        {"repContratoSimple", Map.of()}, {"repAreaSimple", Map.of()}, {"repRolSimple", Map.of()},
        {"reportProdSimp", Map.of()}, {"reporTblCruzProd", Map.of()}, {"reportContratoTblCruz", Map.of()},
        {"reportTblCruzUsuarios", Map.of()}, {"repInsSimp", Map.of()},
        {"repContratoPorArea", Map.of("descripArea", "Preparación")},
        {"repContratoPorRol", Map.of("Descripcion", "Mesero")},
        {"repContratoPorFechas", Map.of("fechaIni", "2024-01-01", "fechaFin", "2026-12-31")},
        {"repInsumoPorPrecio", Map.of("precioMin", 5.0, "precioMax", 20.0)},
        {"reporProdParam", Map.of("parPrecMin", 10.0, "parPrecMax", 30.0)},
        {"repVentasPorDia", Map.of("fechaIni", "2024-01-01", "fechaFin", "2030-12-31")},
        {"repVentasPorProducto", Map.of("fechaIni", "2024-01-01", "fechaFin", "2030-12-31")},
        {"repVentasPorEmpleado", Map.of("fechaIni", "2024-01-01", "fechaFin", "2030-12-31")},
        {"comprobante", Map.of("ordenDeVentaID", 1)},
    };

    @Test
    void instalacionNuevaConDatosDeEjemplo() throws Exception {
        instalarConDatosDeEjemplo();
        assertEquals(ConexionBD.Estado.DISPONIBLE, ConexionBD.verificar());
        assertEquals(TipoUsuario.ADMINISTRADOR,
                new UsuarioDao().autenticar("admin", CONTRASENA_ADMIN.toCharArray(), 1).orElseThrow().getTipo());
        assertTrue(new UsuarioDao().autenticar("demo.ventas", "Demo2024".toCharArray(), 1).isPresent());
        assertTrue(new UsuarioDao().autenticar("admin", "otra".toCharArray(), 1).isEmpty());
        assertThrows(DatosException.class, () -> InstaladorBD.cargarDatosEjemplo(1));

        Area area = new Area();
        area.setDescripArea("Nueva");
        assertEquals(504, new AreaDao().insertar(area), "las secuencias continúan tras los datos de ejemplo");

        for (Object[] caso : REPORTES) {
            @SuppressWarnings("unchecked")
            Map<String, Object> parametros = (Map<String, Object>) caso[1];
            Reportes.generar((String) caso[0], parametros);
        }
    }

    @Test
    void migraUnaBaseCreadaConElEsquemaOriginal() throws Exception {
        borrarBaseDatos();
        try (Connection c = ConexionBD.abrirServidor(); Statement st = c.createStatement()) {
            st.execute("CREATE DATABASE BD_Restaurant");
        }
        try (Connection c = ConexionBD.abrir(); Statement st = c.createStatement()) {
            ScriptSql.ejecutar(c, recurso("/bd/schema-v1.sql"));
            st.execute("INSERT INTO LocalR (idLocalR, Ubicacion) VALUES (1, 'Centro')");
            st.execute("INSERT INTO Empleado (empleadoID, nombres, apellidos) VALUES (1001, 'Admin', 'Prueba')");
            st.execute("INSERT INTO Usuario (usuarioID, idLocalR, usuario, contraseña, tipoUsuario, empleadoID)"
                    + " VALUES (101, 1, 'admin', 'texto-plano-1', 1, 1001)");
            ScriptSql.ejecutar(c, recurso("/bd/datos-v1.sql").replace("${idLocal}", "1"));
        }
        assertEquals("float", tipo("Producto", "precioUnit"));

        MigradorBD.actualizar();
        MigradorBD.actualizar();

        assertEquals("decimal", tipo("Producto", "precioUnit"));
        assertEquals("decimal", tipo("Contrato", "sueldo"));
        assertEquals("decimal", tipo("Insumo", "cantidad"));
        assertEquals("varchar", tipo("Cliente", "telefono"));
        assertFalse(tipo("CabOrdenDeVenta", "estado").isEmpty());
        assertEquals("987000001", new ClienteDao().listar(false, "").get(0).getTelefono());
        assertEquals(0, new BigDecimal("28.00").compareTo(new ProductoDao().listar(true, "Lomo").get(0).getPrecioUnit()));

        assertTrue(new UsuarioDao().autenticar("admin", "texto-plano-1".toCharArray(), 1).isPresent());
        String guardada = Jdbc.uno("SELECT contraseña FROM Usuario WHERE usuarioID = 101", rs -> rs.getString(1))
                .orElseThrow();
        assertTrue(guardada.startsWith("pbkdf2$"), "la contraseña antigua se sustituye por su hash");

        Area area = new Area();
        area.setDescripArea("Nueva");
        assertEquals(504, new AreaDao().insertar(area));
    }

    private static String tipo(String tabla, String columna) {
        return Jdbc.uno("SELECT DATA_TYPE FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = ? AND COLUMN_NAME = ?",
                rs -> rs.getString(1), tabla, columna).orElse("");
    }

    private static String recurso(String ruta) throws Exception {
        try (var in = InstalacionYMigracionIT.class.getResourceAsStream(ruta)) {
            return new String(in.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}
