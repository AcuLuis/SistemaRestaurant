package restaurante.config;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class ConfiguracionTest {

    @TempDir
    Path carpeta;

    @Test
    void leeElPrimerArchivoExistente() throws IOException {
        Path archivo = carpeta.resolve("app.properties");
        Files.writeString(archivo, "db.host=servidor\ndb.port=1500\ndb.password=  clave  \n", StandardCharsets.UTF_8);
        Configuracion cfg = Configuracion.cargar(List.of(carpeta.resolve("no-existe.properties"), archivo), Map.of());
        assertEquals("servidor", cfg.texto("db.host", "localhost"));
        assertEquals(1500, cfg.entero("db.port", 1433));
        assertEquals("clave", cfg.texto("db.password", ""));
        assertEquals(archivo.toAbsolutePath(), cfg.origen().orElseThrow());
    }

    @Test
    void lasVariablesDeEntornoTienenPrioridad() throws IOException {
        Path archivo = carpeta.resolve("app.properties");
        Files.writeString(archivo, "db.password=del-archivo\n", StandardCharsets.UTF_8);
        Configuracion cfg = Configuracion.cargar(List.of(archivo), Map.of("RESTAURANTE_DB_PASSWORD", "del-entorno"));
        assertEquals("del-entorno", cfg.texto("db.password", ""));
    }

    @Test
    void sinArchivoSeUsanLosValoresPorDefecto() {
        Configuracion cfg = Configuracion.cargar(List.of(carpeta.resolve("no-existe")), Map.of());
        assertTrue(cfg.origen().isEmpty());
        assertEquals("localhost", cfg.texto("db.host", "localhost"));
        assertTrue(cfg.booleano("db.encrypt", true));
        assertFalse(cfg.definido("smtp.user"));
    }

    @Test
    void unNumeroInvalidoSeInformaConClaridad() throws IOException {
        Path archivo = carpeta.resolve("app.properties");
        Files.writeString(archivo, "db.port=mil\n", StandardCharsets.UTF_8);
        Configuracion cfg = Configuracion.cargar(List.of(archivo), Map.of());
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> cfg.entero("db.port", 1433));
        assertTrue(e.getMessage().contains("db.port"));
    }

    @Test
    void nombreDeLaVariableDeEntorno() {
        assertEquals("RESTAURANTE_DB_TRUSTSERVERCERTIFICATE",
                Configuracion.nombreVariableEntorno("db.trustServerCertificate"));
    }
}
