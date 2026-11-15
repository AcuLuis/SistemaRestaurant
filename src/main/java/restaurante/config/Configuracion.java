package restaurante.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

/**
 * Configuración de la aplicación.
 *
 * <p>Los valores se leen, por orden de prioridad, de:</p>
 * <ol>
 *   <li>Variables de entorno con el prefijo {@code RESTAURANTE_} (por ejemplo,
 *       {@code db.password} se puede definir con {@code RESTAURANTE_DB_PASSWORD}).</li>
 *   <li>El archivo indicado en la propiedad del sistema {@code restaurante.config}.</li>
 *   <li>{@code config/app.properties} en el directorio de trabajo.</li>
 *   <li>{@code app.properties} en la carpeta {@code .sistema-restaurant} del usuario.</li>
 * </ol>
 *
 * <p>Las credenciales nunca deben escribirse en el código fuente.</p>
 */
public final class Configuracion {

    public static final String PROPIEDAD_RUTA = "restaurante.config";
    private static final String PREFIJO_ENTORNO = "RESTAURANTE_";

    private static volatile Configuracion instancia;

    private final Properties propiedades;
    private final Map<String, String> entorno;
    private final Path origen;

    Configuracion(Properties propiedades, Map<String, String> entorno, Path origen) {
        this.propiedades = propiedades;
        this.entorno = entorno;
        this.origen = origen;
    }

    /** Devuelve la configuración cargada (se carga una sola vez). */
    public static Configuracion get() {
        Configuracion actual = instancia;
        if (actual == null) {
            synchronized (Configuracion.class) {
                actual = instancia;
                if (actual == null) {
                    actual = cargar(rutasCandidatas(), System.getenv());
                    instancia = actual;
                }
            }
        }
        return actual;
    }

    /**
     * Descarta la configuración cargada para que se vuelva a leer en el
     * siguiente acceso (por ejemplo, tras cambiar -Drestaurante.config).
     */
    public static synchronized void recargar() {
        instancia = null;
    }

    static List<Path> rutasCandidatas() {
        List<Path> rutas = new ArrayList<>();
        String rutaSistema = System.getProperty(PROPIEDAD_RUTA);
        if (rutaSistema != null && !rutaSistema.isBlank()) {
            rutas.add(Paths.get(rutaSistema));
        }
        rutas.add(Paths.get("config", "app.properties"));
        rutas.add(Paths.get(System.getProperty("user.home"), ".sistema-restaurant", "app.properties"));
        return rutas;
    }

    static Configuracion cargar(List<Path> rutas, Map<String, String> entorno) {
        Properties propiedades = new Properties();
        for (Path ruta : rutas) {
            if (Files.isRegularFile(ruta)) {
                try (InputStream in = Files.newInputStream(ruta)) {
                    propiedades.load(new java.io.InputStreamReader(in, java.nio.charset.StandardCharsets.UTF_8));
                } catch (IOException e) {
                    throw new UncheckedIOException("No se pudo leer la configuración: " + ruta, e);
                }
                return new Configuracion(propiedades, entorno, ruta.toAbsolutePath());
            }
        }
        return new Configuracion(propiedades, entorno, null);
    }

    /** Archivo de configuración utilizado, si se encontró alguno. */
    public Optional<Path> origen() {
        return Optional.ofNullable(origen);
    }

    public String texto(String clave, String porDefecto) {
        String valorEntorno = entorno.get(nombreVariableEntorno(clave));
        if (valorEntorno != null) {
            return valorEntorno.trim();
        }
        String valor = propiedades.getProperty(clave);
        return valor == null ? porDefecto : valor.trim();
    }

    public int entero(String clave, int porDefecto) {
        String valor = texto(clave, null);
        if (valor == null || valor.isEmpty()) {
            return porDefecto;
        }
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IllegalStateException("El valor de '" + clave + "' debe ser un número entero: " + valor, e);
        }
    }

    public boolean booleano(String clave, boolean porDefecto) {
        String valor = texto(clave, null);
        if (valor == null || valor.isEmpty()) {
            return porDefecto;
        }
        return Boolean.parseBoolean(valor);
    }

    /** Indica si la clave tiene un valor no vacío. */
    public boolean definido(String clave) {
        String valor = texto(clave, null);
        return valor != null && !valor.isEmpty();
    }

    static String nombreVariableEntorno(String clave) {
        return PREFIJO_ENTORNO + clave.toUpperCase(Locale.ROOT).replace('.', '_').replace('-', '_');
    }

    /** Claves admitidas en el archivo de configuración. */
    public static final class Claves {
        public static final String DB_HOST = "db.host";
        public static final String DB_PUERTO = "db.port";
        public static final String DB_INSTANCIA = "db.instance";
        public static final String DB_NOMBRE = "db.name";
        public static final String DB_USUARIO = "db.user";
        public static final String DB_CONTRASENA = "db.password";
        public static final String DB_CIFRAR = "db.encrypt";
        public static final String DB_CONFIAR_CERTIFICADO = "db.trustServerCertificate";

        public static final String SMTP_HOST = "smtp.host";
        public static final String SMTP_PUERTO = "smtp.port";
        public static final String SMTP_USUARIO = "smtp.user";
        public static final String SMTP_CONTRASENA = "smtp.password";
        public static final String SMTP_REMITENTE = "smtp.from";

        public static final String REDES_FACEBOOK = "redes.facebook";
        public static final String REDES_INSTAGRAM = "redes.instagram";
        public static final String REDES_TWITTER = "redes.twitter";

        public static final List<String> TODAS = Collections.unmodifiableList(List.of(
                DB_HOST, DB_PUERTO, DB_INSTANCIA, DB_NOMBRE, DB_USUARIO, DB_CONTRASENA, DB_CIFRAR,
                DB_CONFIAR_CERTIFICADO, SMTP_HOST, SMTP_PUERTO, SMTP_USUARIO, SMTP_CONTRASENA,
                SMTP_REMITENTE, REDES_FACEBOOK, REDES_INSTAGRAM, REDES_TWITTER));

        private Claves() {
        }
    }
}
