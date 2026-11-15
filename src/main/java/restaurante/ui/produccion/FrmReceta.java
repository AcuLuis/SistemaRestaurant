package restaurante.ui.produccion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.InsumoDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.RecetaDao;
import restaurante.modelo.CabReceta;
import restaurante.modelo.DetReceta;
import restaurante.modelo.Insumo;
import restaurante.modelo.Producto;
import restaurante.servicio.RecetaServicio;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Iconos;
import restaurante.ui.util.Tablas;
import restaurante.util.Validaciones;

/**
 * Recetas: insumos necesarios para preparar una unidad de cada producto. Se
 * usan al completar las órdenes de producción para descontar los insumos.
 */
public class FrmReceta extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private final transient ProductoDao productoDao = new ProductoDao();
    private final transient RecetaDao recetaDao = new RecetaDao();
    private final transient RecetaServicio servicio = new RecetaServicio();
    private final transient List<Insumo> insumos = new InsumoDao().listar(false, "");
    private final transient List<DetReceta> lineas = new ArrayList<>();
    private transient List<Producto> productos = List.of();
    private transient Producto producto;

    private final DefaultTableModel modeloProductos = new DefaultTableModel(new String[] {"Código", "Producto", "Receta"}, 0);
    private final JTable tblProductos = new JTable(modeloProductos);
    private final DefaultTableModel modeloLineas = new DefaultTableModel(new String[] {"Insumo", "Cantidad", "Unidad"}, 0);
    private final JTable tblLineas = new JTable(modeloLineas);
    private final JLabel lblProducto = new JLabel("Seleccione un producto");
    private final JTextArea txtPreparacion = new JTextArea(4, 30);
    private final JComboBox<String> cmbInsumo = new JComboBox<>();
    private final JTextField txtCantidad = new JTextField(6);
    private final JButton btnGrabar = new JButton("Grabar receta");
    private final JButton btnEliminar = new JButton("Eliminar receta");

    public FrmReceta() {
        super("Recetas", true, true, true, true);
        setFrameIcon(Iconos.icono("receta"));
        Tablas.configurar(tblProductos);
        Tablas.configurar(tblLineas);
        Tablas.alSeleccionar(tblProductos, this::cargarProducto);
        insumos.forEach(i -> cmbInsumo.addItem(i.getDescripInsumo() + " (" + i.getUnidadMedida() + ")"));
        txtCantidad.setToolTipText("Cantidad del insumo por unidad del producto (hasta tres decimales)");
        txtPreparacion.setLineWrap(true);
        txtPreparacion.setWrapStyleWord(true);

        JPanel izquierda = new JPanel(new BorderLayout());
        izquierda.setBorder(BorderFactory.createTitledBorder("Productos"));
        izquierda.add(new JScrollPane(tblProductos), BorderLayout.CENTER);

        JPanel agregar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnAgregar = new JButton("Agregar");
        JButton btnQuitar = new JButton("Quitar");
        btnAgregar.addActionListener(e -> agregarLinea());
        btnQuitar.addActionListener(e -> quitarLinea());
        txtCantidad.addActionListener(e -> agregarLinea());
        agregar.add(new JLabel("Insumo:"));
        agregar.add(cmbInsumo);
        agregar.add(new JLabel("Cantidad:"));
        agregar.add(txtCantidad);
        agregar.add(btnAgregar);
        agregar.add(btnQuitar);

        JPanel preparacion = new JPanel(new BorderLayout());
        preparacion.setBorder(BorderFactory.createTitledBorder("Preparación (opcional)"));
        preparacion.add(new JScrollPane(txtPreparacion), BorderLayout.CENTER);

        JPanel ingredientes = new JPanel(new BorderLayout());
        ingredientes.setBorder(BorderFactory.createTitledBorder("Insumos por unidad"));
        ingredientes.add(new JScrollPane(tblLineas), BorderLayout.CENTER);
        ingredientes.add(agregar, BorderLayout.SOUTH);

        lblProducto.setFont(lblProducto.getFont().deriveFont(java.awt.Font.BOLD, 14f));
        JPanel derecha = new JPanel(new BorderLayout(0, 6));
        derecha.add(lblProducto, BorderLayout.NORTH);
        derecha.add(ingredientes, BorderLayout.CENTER);
        derecha.add(preparacion, BorderLayout.SOUTH);

        JSplitPane division = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, izquierda, derecha);
        division.setResizeWeight(0.4);
        division.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnCerrar = new JButton("Cerrar");
        btnGrabar.addActionListener(e -> grabar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());
        acciones.add(btnGrabar);
        acciones.add(btnEliminar);
        acciones.add(btnCerrar);

        getContentPane().add(division, BorderLayout.CENTER);
        getContentPane().add(acciones, BorderLayout.SOUTH);
        setSize(980, 540);
        cargarProductos();
        habilitar(false);
    }

    private void cargarProductos() {
        Set<Integer> conReceta = recetaDao.productosConReceta();
        productos = productoDao.listar(false, "");
        modeloProductos.setRowCount(0);
        for (Producto p : productos) {
            modeloProductos.addRow(new Object[] {p.getProductoID(), p.getDescripProducto(),
                conReceta.contains(p.getProductoID()) ? "Sí" : "No"});
        }
    }

    private void cargarProducto() {
        int fila = Tablas.filaSeleccionada(tblProductos);
        if (fila < 0) {
            return;
        }
        producto = productos.get(fila);
        lblProducto.setText("Receta de: " + producto.getDescripProducto());
        lineas.clear();
        Optional<CabReceta> receta = recetaDao.buscarPorProducto(producto.getProductoID());
        txtPreparacion.setText(receta.map(CabReceta::getPreparacion).orElse(""));
        receta.ifPresent(r -> lineas.addAll(recetaDao.listarDetalle(r.getRecetaID())));
        refrescarLineas();
        habilitar(true);
        btnEliminar.setEnabled(receta.isPresent());
    }

    private void agregarLinea() {
        if (producto == null || cmbInsumo.getSelectedIndex() < 0) {
            return;
        }
        Optional<BigDecimal> cantidad = Validaciones.cantidad(txtCantidad.getText());
        if (cantidad.isEmpty() || cantidad.get().signum() == 0) {
            Dialogos.aviso(this, "La cantidad debe ser un número mayor que cero (hasta tres decimales).");
            txtCantidad.requestFocusInWindow();
            return;
        }
        Insumo insumo = insumos.get(cmbInsumo.getSelectedIndex());
        lineas.removeIf(l -> l.getInsumoID() == insumo.getInsumoID());
        DetReceta linea = new DetReceta(insumo.getInsumoID(), cantidad.get());
        linea.setDescripInsumo(insumo.getDescripInsumo());
        linea.setUnidadMedida(insumo.getUnidadMedida());
        lineas.add(linea);
        refrescarLineas();
        txtCantidad.setText("");
        cmbInsumo.requestFocusInWindow();
    }

    private void quitarLinea() {
        int fila = Tablas.filaSeleccionada(tblLineas);
        if (fila >= 0) {
            lineas.remove(fila);
            refrescarLineas();
        }
    }

    private void refrescarLineas() {
        modeloLineas.setRowCount(0);
        for (DetReceta l : lineas) {
            modeloLineas.addRow(new Object[] {l.getDescripInsumo(), Tablas.cantidad(l.getCantidad()), l.getUnidadMedida()});
        }
    }

    private void grabar() {
        if (producto == null) {
            return;
        }
        servicio.guardar(producto.getProductoID(), txtPreparacion.getText(), lineas);
        Dialogos.info(this, "Receta de \"" + producto.getDescripProducto() + "\" guardada.");
        recargar();
    }

    private void eliminar() {
        if (producto == null || !Dialogos.confirmar(this, "¿Desea eliminar la receta de \""
                + producto.getDescripProducto() + "\"?")) {
            return;
        }
        servicio.eliminar(producto.getProductoID());
        recargar();
    }

    private void recargar() {
        int fila = tblProductos.getSelectedRow();
        cargarProductos();
        if (fila >= 0 && fila < tblProductos.getRowCount()) {
            tblProductos.setRowSelectionInterval(fila, fila);
        }
    }

    private void habilitar(boolean activo) {
        for (java.awt.Component c : new java.awt.Component[] {cmbInsumo, txtCantidad, txtPreparacion, btnGrabar,
            btnEliminar}) {
            c.setEnabled(activo);
        }
    }
}
