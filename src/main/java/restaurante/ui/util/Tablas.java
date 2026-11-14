package restaurante.ui.util;

import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

/**
 * Configuración común de las tablas de los formularios.
 */
public final class Tablas {

    private Tablas() {
    }

    /**
     * Tabla de solo lectura, con selección de una fila y ordenable al pulsar
     * en la cabecera. Editar celdas directamente no guardaba nada y confundía.
     */
    public static void configurar(JTable tabla) {
        tabla.setDefaultEditor(Object.class, null);
        tabla.setDefaultEditor(Number.class, null);
        tabla.setDefaultEditor(Boolean.class, null);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.setAutoCreateRowSorter(true);
        tabla.getTableHeader().setReorderingAllowed(false);
        tabla.setFillsViewportHeight(true);
    }

    /** Índice en el modelo de la fila seleccionada, o -1 si no hay selección. */
    public static int filaSeleccionada(JTable tabla) {
        int vista = tabla.getSelectedRow();
        return vista < 0 ? -1 : tabla.convertRowIndexToModel(vista);
    }

    /**
     * Ejecuta la acción cuando cambia la fila seleccionada, ya sea con el ratón
     * o con el teclado (antes solo se reaccionaba al clic).
     */
    public static void alSeleccionar(JTable tabla, Runnable accion) {
        tabla.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tabla.getSelectedRow() >= 0) {
                accion.run();
            }
        });
    }

    /** Ejecuta la acción con doble clic o con la tecla Intro sobre una fila. */
    public static void alElegir(JTable tabla, Runnable accion) {
        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && tabla.getSelectedRow() >= 0) {
                    accion.run();
                }
            }
        });
        tabla.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "elegir");
        tabla.getActionMap().put("elegir", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                if (tabla.getSelectedRow() >= 0) {
                    accion.run();
                }
            }
        });
    }

    /** Vacía la tabla conservando las columnas. */
    public static DefaultTableModel vaciar(JTable tabla) {
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        modelo.setRowCount(0);
        return modelo;
    }

    /** Importe con dos decimales (separador decimal punto, como se introduce en los formularios). */
    public static String importe(BigDecimal valor) {
        return valor == null ? "" : valor.setScale(2, RoundingMode.HALF_UP).toPlainString();
    }

    /** Cantidad sin ceros decimales innecesarios (2, 0.25, 1.5). */
    public static String cantidad(BigDecimal valor) {
        return valor == null ? "" : valor.stripTrailingZeros().toPlainString();
    }
}
