package restaurante.datos;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Lee scripts SQL incluidos en la aplicación y los ejecuta lote a lote.
 * Los lotes se separan con una línea que contiene solo "GO", igual que en
 * sqlcmd y SQL Server Management Studio.
 */
public final class ScriptSql {

    private static final Pattern SEPARADOR = Pattern.compile("(?im)^\\s*GO\\s*$");

    private ScriptSql() {
    }

    /** Lee un script del classpath (por ejemplo, "/bd/schema.sql"). */
    public static String leerRecurso(String ruta) {
        try (InputStream in = ScriptSql.class.getResourceAsStream(ruta)) {
            if (in == null) {
                throw new IllegalStateException("No se encontró el script " + ruta);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException("No se pudo leer el script " + ruta, e);
        }
    }

    /** Divide el script en lotes, descartando los vacíos o que solo contienen comentarios. */
    public static List<String> lotes(String script) {
        List<String> lotes = new ArrayList<>();
        for (String lote : SEPARADOR.split(script)) {
            if (!soloComentarios(lote)) {
                lotes.add(lote.trim());
            }
        }
        return lotes;
    }

    /** Sustituye las variables con formato ${nombre}. */
    public static String sustituir(String script, Map<String, String> variables) {
        String resultado = script;
        for (Map.Entry<String, String> v : variables.entrySet()) {
            resultado = resultado.replace("${" + v.getKey() + "}", v.getValue());
        }
        return resultado;
    }

    /** Ejecuta todos los lotes con la conexión indicada (respeta su transacción). */
    public static void ejecutar(Connection c, String script) throws SQLException {
        try (Statement st = c.createStatement()) {
            for (String lote : lotes(script)) {
                st.execute(lote);
            }
        }
    }

    static boolean soloComentarios(String lote) {
        for (String linea : lote.split("\\R")) {
            String t = linea.trim();
            if (!t.isEmpty() && !t.startsWith("--")) {
                return false;
            }
        }
        return true;
    }
}
