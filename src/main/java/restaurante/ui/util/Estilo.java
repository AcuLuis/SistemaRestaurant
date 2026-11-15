package restaurante.ui.util;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.List;
import java.util.prefs.Preferences;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

/**
 * Apariencia común de la aplicación.
 */
public final class Estilo {

    private static final String ICONO = "/iconos/logotipo-pequeno.png";
    private static final String CLAVE_TEMA = "temaOscuro.";

    private Estilo() {
    }

    /**
     * Aplica el aspecto FlatLaf y registra los ajustes automáticos que se
     * aplican a cada ventana al abrirse (icono y contraste de botones).
     */
    public static void instalar() {
        FlatLightLaf.setup();
        UIManager.put("Table.showHorizontalLines", true);
        UIManager.put("Table.intercellSpacing", new java.awt.Dimension(0, 1));
        UIManager.put("TabbedPane.showTabSeparators", true);
        UIManager.put("OptionPane.yesButtonText", "Sí");
        UIManager.put("OptionPane.noButtonText", "No");
        UIManager.put("OptionPane.cancelButtonText", "Cancelar");
        UIManager.put("OptionPane.okButtonText", "Aceptar");
        UIManager.put("FileChooser.cancelButtonText", "Cancelar");
        UIManager.put("FileChooser.saveButtonText", "Guardar");
        UIManager.put("FileChooser.openButtonText", "Abrir");

        List<Image> iconos = icono();
        Toolkit.getDefaultToolkit().addAWTEventListener(evento -> {
            if (evento.getID() == WindowEvent.WINDOW_OPENED) {
                Window ventana = ((WindowEvent) evento).getWindow();
                if (ventana.getIconImages().isEmpty() && !iconos.isEmpty()) {
                    ventana.setIconImages(iconos);
                }
                ajustarContraste(ventana);
            }
        }, AWTEvent.WINDOW_EVENT_MASK);
    }

    /** Indica si el usuario eligió el tema oscuro (preferencia guardada en este equipo). */
    public static boolean temaOscuro(String usuario) {
        return preferencias().getBoolean(CLAVE_TEMA + usuario, false);
    }

    /** Aplica el tema claro u oscuro a todas las ventanas abiertas y lo recuerda para el usuario. */
    public static void aplicarTema(String usuario, boolean oscuro) {
        preferencias().putBoolean(CLAVE_TEMA + usuario, oscuro);
        if (oscuro == FlatLaf.isLafDark()) {
            return;
        }
        if (oscuro) {
            FlatDarkLaf.setup();
        } else {
            FlatLightLaf.setup();
        }
        FlatLaf.updateUI();
        for (Window ventana : Window.getWindows()) {
            ajustarContraste(ventana);
        }
    }

    private static Preferences preferencias() {
        return Preferences.userRoot().node("sistema-restaurant");
    }

    /**
     * Los formularios definen botones con fondo oscuro pero conservan el color
     * de texto por defecto, que resulta ilegible. Se usa texto claro en esos casos.
     */
    public static void ajustarContraste(Component componente) {
        if (componente instanceof AbstractButton) {
            AbstractButton boton = (AbstractButton) componente;
            Color fondo = boton.getBackground();
            boolean fondoPropio = fondo != null && !(fondo instanceof UIResource);
            boolean textoPorDefecto = boton.getForeground() == null || boton.getForeground() instanceof UIResource;
            if (fondoPropio && textoPorDefecto && luminancia(fondo) < 0.45) {
                boton.setForeground(Color.WHITE);
            }
        }
        if (componente instanceof Container) {
            for (Component hijo : ((Container) componente).getComponents()) {
                ajustarContraste(hijo);
            }
        }
    }

    static double luminancia(Color c) {
        return (0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue()) / 255.0;
    }

    private static List<Image> icono() {
        URL url = Estilo.class.getResource(ICONO);
        return url == null ? List.of() : List.of(new ImageIcon(url).getImage());
    }
}
