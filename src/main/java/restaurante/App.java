package restaurante;

import javax.swing.SwingUtilities;
import restaurante.ui.Entrada;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Estilo;
import restaurante.util.Registro;

/**
 * Punto de entrada de Sistema Restaurant.
 */
public final class App {

    private App() {
    }

    public static void main(String[] args) {
        Registro.configurar();
        Dialogos.instalarManejadorGlobal();
        SwingUtilities.invokeLater(() -> {
            Estilo.instalar();
            new Entrada().setVisible(true);
        });
    }
}
