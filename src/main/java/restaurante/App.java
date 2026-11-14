package restaurante;

import javax.swing.SwingUtilities;
import restaurante.ui.Entrada;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Estilo;

/**
 * Punto de entrada de Sistema Restaurant.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        Dialogos.instalarManejadorGlobal();
        SwingUtilities.invokeLater(() -> {
            Estilo.instalar();
            new Entrada().setVisible(true);
        });
    }
}
