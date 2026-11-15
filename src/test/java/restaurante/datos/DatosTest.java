package restaurante.datos;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DatosTest {

    @Test
    void separaLotesPorGoYDescartaLosVacios() {
        String script = "-- comentario\nCREATE TABLE A (x int)\nGO\n  go  \nINSERT INTO A VALUES (1)\nGo\n-- solo comentario\nGO\n";
        List<String> lotes = ScriptSql.lotes(script);
        assertEquals(List.of("CREATE TABLE A (x int)", "INSERT INTO A VALUES (1)"),
                lotes.stream().map(l -> l.replaceAll("(?m)^--.*\\R", "").trim()).toList());
    }

    @Test
    void noSeparaPorGoDentroDeUnaLinea() {
        assertEquals(1, ScriptSql.lotes("SELECT 'GO' AS texto\n").size());
    }

    @Test
    void sustituyeVariables() {
        assertEquals("INSERT INTO T VALUES (7, 7)",
                ScriptSql.sustituir("INSERT INTO T VALUES (${idLocal}, ${idLocal})", Map.of("idLocal", "7")));
    }

    @Test
    void losScriptsIncluidosSonLegibles() {
        String esquema = ScriptSql.leerRecurso("/bd/schema.sql");
        assertTrue(ScriptSql.lotes(esquema).size() > 20);
        assertTrue(esquema.contains("CREATE TABLE Usuario"));
        String ejemplo = ScriptSql.leerRecurso("/bd/datos-ejemplo.sql");
        assertTrue(ejemplo.contains("${idLocal}"));
        assertFalse(ScriptSql.sustituir(ejemplo, Map.of("idLocal", "1")).contains("${"));
    }

    @Test
    void lasMigracionesSonIdempotentes() {
        assertTrue(MigradorBD.SENTENCIAS.stream().allMatch(s -> s.startsWith("IF ")));
        assertTrue(MigradorBD.sentenciasSecuencias().stream().allMatch(s -> s.startsWith("IF ") || s.startsWith("BEGIN")));
        assertEquals(MigradorBD.SECUENCIAS.size() * 2, MigradorBD.sentenciasSecuencias().size());
    }

    @Test
    void escapaLosComodinesDeLike() {
        assertEquals("50[%] de [_]descuento [[]1]%", Jdbc.empiezaPor(" 50% de _descuento [1] "));
        assertEquals("%", Jdbc.empiezaPor(null));
    }

    @Test
    void traduceLosErroresDeSqlServer() {
        assertTrue(DatosException.traducir(new SQLException("fk", "23000", 547)).contains("relacionado"));
        assertTrue(DatosException.traducir(new SQLException("pk", "23000", 2627)).contains("Ya existe"));
        assertTrue(DatosException.traducir(new SQLException("largo", "22001", 8152)).contains("demasiado largo"));
        assertTrue(DatosException.traducir(new SQLException("red", "08S01", 0)).contains("conectar"));
    }
}
