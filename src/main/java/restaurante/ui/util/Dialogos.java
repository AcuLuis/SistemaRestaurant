package restaurante.ui.util;

import java.awt.Component;
import java.awt.KeyboardFocusManager;
import java.awt.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import restaurante.datos.DatosException;

/**
 * Mensajes al usuario con un formato coherente en toda la aplicación
 * (título, icono y ventana padre correctos).
 */
public final class Dialogos {

    private static final Logger LOG = Logger.getLogger(Dialogos.class.getName());
    private static final String TITULO = "Sistema Restaurant";

    private Dialogos() {
    }

    public static void info(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre(padre), mensaje, TITULO, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void aviso(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre(padre), mensaje, TITULO, JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component padre, String mensaje) {
        JOptionPane.showMessageDialog(padre(padre), mensaje, TITULO, JOptionPane.ERROR_MESSAGE);
    }

    /** Pide confirmación; la opción por defecto es "No" para evitar acciones accidentales. */
    public static boolean confirmar(Component padre, String mensaje) {
        Object[] opciones = {"Sí", "No"};
        int r = JOptionPane.showOptionDialog(padre(padre), mensaje, TITULO, JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, opciones, opciones[1]);
        return r == 0;
    }

    /** Muestra un error inesperado con un mensaje comprensible y lo registra en el log. */
    public static void error(Component padre, Throwable t) {
        DatosException datos = buscarDatosException(t);
        if (datos != null) {
            error(padre, datos.getMensajeUsuario());
            return;
        }
        LOG.log(Level.SEVERE, "Error no controlado", t);
        error(padre, "Se produjo un error inesperado: " + (t.getMessage() == null ? t.getClass().getSimpleName()
                : t.getMessage()));
    }

    /**
     * Instala un manejador global para que ningún error quede oculto en la
     * consola: se muestra al usuario y se registra.
     */
    public static void instalarManejadorGlobal() {
        Thread.setDefaultUncaughtExceptionHandler((hilo, error) -> {
            if (SwingUtilities.isEventDispatchThread()) {
                error(null, error);
            } else {
                SwingUtilities.invokeLater(() -> error(null, error));
            }
        });
    }

    private static DatosException buscarDatosException(Throwable t) {
        for (Throwable actual = t; actual != null; actual = actual.getCause()) {
            if (actual instanceof DatosException) {
                return (DatosException) actual;
            }
        }
        return null;
    }

    private static Component padre(Component padre) {
        if (padre != null) {
            return padre;
        }
        Window activa = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
        return activa;
    }
}
