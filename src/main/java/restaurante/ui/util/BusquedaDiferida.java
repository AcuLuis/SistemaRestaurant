package restaurante.ui.util;

import java.util.function.Consumer;
import javax.swing.JTextField;
import javax.swing.Timer;

/**
 * Búsqueda que se ejecuta cuando el usuario deja de escribir durante un
 * instante, en lugar de lanzar una consulta a la base de datos por cada tecla.
 */
public final class BusquedaDiferida {

    private static final int ESPERA_MS = 300;

    private final Timer temporizador;

    public BusquedaDiferida(JTextField campo, Consumer<String> busqueda) {
        temporizador = new Timer(ESPERA_MS, e -> busqueda.accept(campo.getText().trim()));
        temporizador.setRepeats(false);
    }

    /** Reinicia la espera; llamar en cada cambio del texto. */
    public void reiniciar() {
        temporizador.restart();
    }
}
