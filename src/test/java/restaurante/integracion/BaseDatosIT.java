package restaurante.integracion;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.mssqlserver.MSSQLServerContainer;
import restaurante.config.Configuracion;
import restaurante.datos.ConexionBD;
import restaurante.datos.InstaladorBD;
import restaurante.modelo.Empleado;
import restaurante.modelo.LocalR;
import restaurante.seguridad.Sesion;
import restaurante.seguridad.TipoUsuario;

/**
 * Base de las pruebas de integración: arranca (una sola vez) un SQL Server
 * desechable en Docker y apunta la configuración de la aplicación a él.
 * Si Docker no está disponible, las pruebas se omiten.
 */
abstract class BaseDatosIT {

    protected static final String CONTRASENA_ADMIN = "Admin2024";
    private static MSSQLServerContainer servidor;

    @BeforeAll
    static void iniciarServidor() throws IOException {
        assumeTrue(DockerClientFactory.instance().isDockerAvailable(), "Docker no está disponible");
        synchronized (BaseDatosIT.class) {
            if (servidor == null) {
                servidor = new MSSQLServerContainer("mcr.microsoft.com/mssql/server:2022-latest").acceptLicense();
                servidor.start();
                Path config = Files.createTempFile("restaurante-it", ".properties");
                Files.writeString(config, "db.host=" + servidor.getHost() + "\n"
                        + "db.port=" + servidor.getMappedPort(1433) + "\n"
                        + "db.name=BD_Restaurant\n"
                        + "db.user=" + servidor.getUsername() + "\n"
                        + "db.password=" + servidor.getPassword() + "\n", StandardCharsets.UTF_8);
                config.toFile().deleteOnExit();
                System.setProperty(Configuracion.PROPIEDAD_RUTA, config.toString());
                Configuracion.recargar();
            }
        }
    }

    /** Elimina la base de datos si existe. */
    protected static void borrarBaseDatos() throws SQLException {
        try (Connection c = ConexionBD.abrirServidor(); Statement st = c.createStatement()) {
            st.execute("IF DB_ID('BD_Restaurant') IS NOT NULL BEGIN"
                    + " ALTER DATABASE BD_Restaurant SET SINGLE_USER WITH ROLLBACK IMMEDIATE;"
                    + " DROP DATABASE BD_Restaurant; END");
        }
    }

    /** Instala una base de datos nueva con los datos de ejemplo e inicia sesión como administrador. */
    protected static void instalarConDatosDeEjemplo() throws SQLException {
        borrarBaseDatos();
        Empleado admin = new Empleado();
        admin.setNombres("admin");
        admin.setApellidos("Principal");
        admin.setFechaNacimiento("1990-01-01");
        admin.setGenero(1);
        admin.setDni("12345678");
        admin.setEstadoCivil("SOLTERO");
        LocalR local = new LocalR();
        local.setIdLocalR(1);
        local.setUbicacion("Centro");
        local.setCapacidadMax(80);
        local.setPisos(1);
        InstaladorBD.crearBaseDatos(admin, local, "admin", CONTRASENA_ADMIN.toCharArray());
        InstaladorBD.cargarDatosEjemplo(1);
        Sesion.iniciar(new Sesion(InstaladorBD.ID_USUARIO_ADMIN, "admin", TipoUsuario.ADMINISTRADOR,
                InstaladorBD.ID_EMPLEADO_ADMIN, 1));
    }
}
