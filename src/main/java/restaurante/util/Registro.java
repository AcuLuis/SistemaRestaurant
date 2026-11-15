package restaurante.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Configura el registro de la aplicación (java.util.logging): los mensajes
 * se guardan en archivos rotativos para poder diagnosticar errores en los
 * equipos donde se usa, y en la consola solo se muestran avisos y errores.
 *
 * <p>Carpeta por defecto: {@code ~/.sistema-restaurant/logs}. Se puede cambiar
 * con {@code -Drestaurante.logs=<carpeta>}.</p>
 */
public final class Registro {

    private static final int TAMANO_MAXIMO = 2 * 1024 * 1024;
    private static final int ARCHIVOS = 5;
    private static volatile Path carpeta;

    private Registro() {
    }

    public static synchronized void configurar() {
        if (carpeta != null) {
            return;
        }
        System.setProperty("java.util.logging.SimpleFormatter.format",
                "%1$tF %1$tT %4$-7s [%3$s] %5$s%6$s%n");
        Logger raiz = Logger.getLogger("");
        for (Handler h : raiz.getHandlers()) {
            if (h instanceof ConsoleHandler) {
                h.setLevel(Level.WARNING);
            }
        }
        raiz.setLevel(Level.INFO);
        Path destino = Paths.get(System.getProperty("restaurante.logs",
                Paths.get(System.getProperty("user.home"), ".sistema-restaurant", "logs").toString()));
        try {
            Files.createDirectories(destino);
            FileHandler archivo = new FileHandler(destino.resolve("sistema-restaurant-%g.log").toString(),
                    TAMANO_MAXIMO, ARCHIVOS, true);
            archivo.setEncoding("UTF-8");
            archivo.setFormatter(new SimpleFormatter());
            archivo.setLevel(Level.INFO);
            raiz.addHandler(archivo);
            carpeta = destino;
            Logger.getLogger(Registro.class.getName()).info("Inicio de Sistema Restaurant. Registro en " + destino);
        } catch (IOException | SecurityException e) {
            Logger.getLogger(Registro.class.getName()).log(Level.WARNING,
                    "No se pudo crear el registro en " + destino + "; solo se usará la consola", e);
        }
    }

    /** Carpeta con los archivos de registro, o null si solo se usa la consola. */
    public static Path carpeta() {
        return carpeta;
    }
}
