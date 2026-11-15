package restaurante.ui.base;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Iconos;
import restaurante.ui.util.Tablas;

/**
 * Base de los formularios de mantenimiento: búsqueda, tabla de registros,
 * panel de datos y botones Nuevo, Grabar, Eliminar y Cerrar con el mismo
 * comportamiento y la misma posición en todos los formularios.
 *
 * <p>La ventana se puede redimensionar y maximizar; la tabla y el panel de
 * datos se adaptan al tamaño.</p>
 *
 * @param <T> tipo de registro que se mantiene
 */
public abstract class FormularioMantenimiento<T> extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    protected final JTextField txtBuscar = new JTextField(22);
    protected final JTable tabla;
    protected final JButton btnNuevo = new JButton("Nuevo");
    protected final JButton btnGrabar = new JButton("Grabar");
    protected final JButton btnEliminar;
    protected final JButton btnCerrar = new JButton("Cerrar");

    private final DefaultTableModel modelo;
    private final JPanel campos = new JPanel(new GridBagLayout());
    private final JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    private int filas;
    private BusquedaDiferida busqueda;
    private List<T> registros = List.of();
    private T seleccionado;

    /**
     * @param textoEliminar texto del botón de eliminar, o {@code null} si el
     *                      registro no se elimina (por ejemplo, se da de baja)
     */
    protected FormularioMantenimiento(String titulo, String icono, String textoEliminar, String... columnas) {
        super(titulo, true, true, true, true);
        setFrameIcon(Iconos.icono(icono));
        modelo = new DefaultTableModel(columnas, 0);
        tabla = new JTable(modelo);
        Tablas.configurar(tabla);
        Tablas.alSeleccionar(tabla, this::cargarSeleccion);
        btnEliminar = textoEliminar == null ? null : new JButton(textoEliminar);

        JPanel buscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        buscar.add(new JLabel("Buscar:"));
        buscar.add(txtBuscar);
        txtBuscar.putClientProperty("JTextField.placeholderText", "Escriba para filtrar");
        txtBuscar.putClientProperty("JTextField.showClearButton", true);

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.add(buscar, BorderLayout.NORTH);
        izquierda.add(new JScrollPane(tabla), BorderLayout.CENTER);

        campos.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Datos"),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)));
        JPanel derecha = new JPanel(new BorderLayout());
        derecha.add(campos, BorderLayout.NORTH);
        derecha.setBorder(BorderFactory.createEmptyBorder(0, 6, 0, 0));

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, derecha);
        division.setResizeWeight(0.62);
        division.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        acciones.add(btnNuevo);
        acciones.add(btnGrabar);
        if (btnEliminar != null) {
            acciones.add(btnEliminar);
        }
        acciones.add(btnCerrar);
        btnNuevo.addActionListener(e -> limpiar());
        btnGrabar.addActionListener(e -> grabarSeleccion());
        if (btnEliminar != null) {
            btnEliminar.addActionListener(e -> eliminarSeleccion());
        }
        btnCerrar.addActionListener(e -> dispose());

        getContentPane().add(division, BorderLayout.CENTER);
        getContentPane().add(acciones, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnGrabar);
        setSize(960, 520);
    }

    /** Debe llamarse al final del constructor de la subclase, tras agregar los campos. */
    protected final void iniciar() {
        busqueda = new BusquedaDiferida(txtBuscar, this::buscar);
        txtBuscar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                busqueda.reiniciar();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                busqueda.reiniciar();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                busqueda.reiniciar();
            }
        });
        GridBagConstraints relleno = new GridBagConstraints();
        relleno.gridy = filas;
        relleno.weighty = 1;
        campos.add(new JLabel(), relleno);
        buscar("");
        limpiar();
    }

    /** Agrega una fila «etiqueta: campo» al panel de datos. */
    protected final void agregarCampo(String etiqueta, JComponent campo) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.gridy = filas++;
        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_END;
        JLabel label = new JLabel(etiqueta);
        label.setLabelFor(campo);
        campos.add(label, c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;
        campos.add(campo, c);
    }

    /** Agrega un botón adicional junto a los botones estándar. */
    protected final void agregarAccion(JButton boton) {
        acciones.add(boton, acciones.getComponentCount() - 1);
    }

    protected abstract List<T> consultar(String texto);

    protected abstract Object[] fila(T registro);

    protected abstract void mostrar(T registro);

    protected abstract void limpiarCampos();

    /** Valida y guarda. Devuelve false (tras avisar al usuario) si algún dato no es válido. */
    protected abstract boolean grabar(T seleccionado);

    protected void eliminar(T registro) {
        throw new UnsupportedOperationException();
    }

    /** Texto con el que se identifica el registro en las confirmaciones. */
    protected abstract String describir(T registro);

    /** Campo que recibe el foco al empezar un registro nuevo. */
    protected abstract JComponent primerCampo();

    protected final T seleccionado() {
        return seleccionado;
    }

    /** Vuelve a consultar los registros manteniendo el filtro. */
    protected final void refrescar() {
        buscar(txtBuscar.getText().trim());
    }

    /** Muestra un aviso y lleva el foco al campo con el problema. Devuelve siempre false. */
    protected final boolean invalido(JComponent campo, String mensaje) {
        Dialogos.aviso(this, mensaje);
        campo.requestFocusInWindow();
        return false;
    }

    private void buscar(String texto) {
        registros = consultar(texto);
        modelo.setRowCount(0);
        for (T r : registros) {
            modelo.addRow(fila(r));
        }
    }

    private void cargarSeleccion() {
        int fila = Tablas.filaSeleccionada(tabla);
        if (fila < 0) {
            return;
        }
        seleccionado = registros.get(fila);
        mostrar(seleccionado);
        btnGrabar.setText("Actualizar");
    }

    protected final void limpiar() {
        seleccionado = null;
        tabla.clearSelection();
        limpiarCampos();
        btnGrabar.setText("Grabar");
        primerCampo().requestFocusInWindow();
    }

    private void grabarSeleccion() {
        boolean nuevo = seleccionado == null;
        if (grabar(seleccionado)) {
            Dialogos.info(this, nuevo ? "Registro creado." : "Registro actualizado.");
            refrescar();
            limpiar();
        }
    }

    private void eliminarSeleccion() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla el registro que desea eliminar.");
            return;
        }
        if (Dialogos.confirmar(this, "¿Desea eliminar " + describir(seleccionado) + "?")) {
            eliminar(seleccionado);
            refrescar();
            limpiar();
        }
    }
}
