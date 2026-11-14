package restaurante.datos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import restaurante.config.Configuracion;
import restaurante.config.Configuracion.Claves;

/**
 * Crea las conexiones a SQL Server a partir de la configuración.
 *
 * <p>Cada operación abre su propia conexión y la cierra al terminar
 * (try-with-resources). Para una aplicación de escritorio con pocos usuarios
 * concurrentes es suficiente y evita conexiones abiertas indefinidamente.</p>
 */
public final class ConexionBD {

    private static final Logger LOG = Logger.getLogger(ConexionBD.class.getName());
    private static final Pattern NOMBRE_VALIDO = Pattern.compile("[A-Za-z_][A-Za-z0-9_]{0,127}");
    private static final int TIEMPO_ESPERA_SEGUNDOS = 10;

    /** Resultado de comprobar la disponibilidad de la base de datos. */
    public enum Estado {
        /** El servidor responde y la base de datos existe. */
        DISPONIBLE,
        /** El servidor responde pero la base de datos aún no se ha creado. */
        SIN_BASE_DATOS,
        /** No se pudo conectar con el servidor (apagado, credenciales, red...). */
        SERVIDOR_NO_DISPONIBLE
    }

    private ConexionBD() {
    }

    /** Abre una conexión a la base de datos de la aplicación. */
    public static Connection abrir() throws SQLException {
        return DriverManager.getConnection(url(true), credenciales());
    }

    /** Abre una conexión al servidor sin seleccionar base de datos (para crearla). */
    public static Connection abrirServidor() throws SQLException {
        return DriverManager.getConnection(url(false), credenciales());
    }

    /** Nombre de la base de datos configurada (validado para usarse en sentencias DDL). */
    public static String nombreBaseDatos() {
        String nombre = Configuracion.get().texto(Claves.DB_NOMBRE, "BD_Restaurant");
        if (!NOMBRE_VALIDO.matcher(nombre).matches()) {
            throw new IllegalStateException("Nombre de base de datos no válido en la configuración: " + nombre);
        }
        return nombre;
    }

    /**
     * Comprueba si el servidor está accesible y si la base de datos existe.
     * Distingue ambos casos para no ofrecer crear la base de datos cuando el
     * problema real es que el servidor no responde.
     */
    public static Estado verificar() {
        try (Connection c = abrirServidor();
             PreparedStatement ps = c.prepareStatement("SELECT DB_ID(?)")) {
            ps.setString(1, nombreBaseDatos());
            try (ResultSet rs = ps.executeQuery()) {
                rs.next();
                rs.getInt(1);
                return rs.wasNull() ? Estado.SIN_BASE_DATOS : Estado.DISPONIBLE;
            }
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "No se pudo conectar con el servidor de base de datos", e);
            ultimoError = e.getMessage();
            return Estado.SERVIDOR_NO_DISPONIBLE;
        }
    }

    private static volatile String ultimoError;

    /** Mensaje técnico del último error de conexión, útil para diagnosticar. */
    public static String ultimoError() {
        return ultimoError;
    }

    static String url(boolean conBaseDatos) {
        Configuracion cfg = Configuracion.get();
        String host = cfg.texto(Claves.DB_HOST, "localhost");
        String instancia = cfg.texto(Claves.DB_INSTANCIA, "");
        StringBuilder url = new StringBuilder("jdbc:sqlserver://").append(host);
        if (!instancia.isEmpty()) {
            url.append('\\').append(instancia);
        } else {
            url.append(':').append(cfg.entero(Claves.DB_PUERTO, 1433));
        }
        if (conBaseDatos) {
            url.append(";databaseName=").append(nombreBaseDatos());
        }
        url.append(";encrypt=").append(cfg.booleano(Claves.DB_CIFRAR, true));
        url.append(";trustServerCertificate=").append(cfg.booleano(Claves.DB_CONFIAR_CERTIFICADO, true));
        url.append(";loginTimeout=").append(TIEMPO_ESPERA_SEGUNDOS);
        url.append(";applicationName=SistemaRestaurant");
        return url.toString();
    }

    private static Properties credenciales() {
        Configuracion cfg = Configuracion.get();
        Properties p = new Properties();
        p.setProperty("user", cfg.texto(Claves.DB_USUARIO, "sa"));
        p.setProperty("password", cfg.texto(Claves.DB_CONTRASENA, ""));
        return p;
    }
}
