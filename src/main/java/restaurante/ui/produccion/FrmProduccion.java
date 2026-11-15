package restaurante.ui.produccion;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.AreaDao;
import restaurante.dao.ProduccionDao;
import restaurante.dao.ProductoDao;
import restaurante.modelo.Area;
import restaurante.modelo.CabOrdenDeProduccionSemanal;
import restaurante.modelo.DetOrdenDeProduccionSemanal;
import restaurante.modelo.Producto;
import restaurante.servicio.ProduccionServicio;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Iconos;
import restaurante.ui.util.SelectorFecha;
import restaurante.ui.util.Tablas;
import restaurante.util.Validaciones;

/**
 * Órdenes de producción de la cocina. Al completarlas se suman las unidades
 * a las existencias de los productos y se descuentan los insumos de sus recetas.
 */
public class FrmProduccion extends JInternalFrame {

    private static final long serialVersionUID = 1L;

    private final transient ProduccionDao dao = new ProduccionDao();
    private final transient ProduccionServicio servicio = new ProduccionServicio();
    private transient List<CabOrdenDeProduccionSemanal> ordenes = List.of();

    private final DefaultTableModel modeloOrdenes = new DefaultTableModel(
            new String[] {"N.º", "Fecha", "Área", "Responsable", "Estado", "Completada"}, 0);
    private final JTable tblOrdenes = new JTable(modeloOrdenes);
    private final DefaultTableModel modeloDetalle = new DefaultTableModel(new String[] {"Producto", "Cantidad"}, 0);
    private final JTable tblDetalle = new JTable(modeloDetalle);

    public FrmProduccion() {
        super("Órdenes de producción", true, true, true, true);
        setFrameIcon(Iconos.icono("produccion"));
        Tablas.configurar(tblOrdenes);
        Tablas.configurar(tblDetalle);
        Tablas.alSeleccionar(tblOrdenes, this::cargarDetalle);

        JPanel arriba = new JPanel(new BorderLayout());
        arriba.setBorder(BorderFactory.createTitledBorder("Órdenes"));
        arriba.add(new JScrollPane(tblOrdenes), BorderLayout.CENTER);
        JPanel abajo = new JPanel(new BorderLayout());
        abajo.setBorder(BorderFactory.createTitledBorder("Productos de la orden"));
        abajo.add(new JScrollPane(tblDetalle), BorderLayout.CENTER);
        JSplitPane division = new JSplitPane(JSplitPane.VERTICAL_SPLIT, arriba, abajo);
        division.setResizeWeight(0.55);
        division.setBorder(BorderFactory.createEmptyBorder(6, 6, 0, 6));

        JButton btnNueva = new JButton("Nueva orden...");
        JButton btnCompletar = new JButton("Completar orden");
        JButton btnEliminar = new JButton("Eliminar orden");
        JButton btnCerrar = new JButton("Cerrar");
        btnNueva.addActionListener(e -> nuevaOrden());
        btnCompletar.addActionListener(e -> completar());
        btnEliminar.addActionListener(e -> eliminar());
        btnCerrar.addActionListener(e -> dispose());
        btnCompletar.setToolTipText("Suma las unidades a las existencias y descuenta los insumos de las recetas");
        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acciones.add(btnNueva);
        acciones.add(btnCompletar);
        acciones.add(btnEliminar);
        acciones.add(btnCerrar);

        getContentPane().add(division, BorderLayout.CENTER);
        getContentPane().add(acciones, BorderLayout.SOUTH);
        setSize(860, 560);
        cargarOrdenes();
    }

    private void cargarOrdenes() {
        ordenes = dao.listar();
        modeloOrdenes.setRowCount(0);
        for (CabOrdenDeProduccionSemanal o : ordenes) {
            modeloOrdenes.addRow(new Object[] {o.getOrdenProduccionID(), o.getFechaxsem(), o.getDescripArea(),
                o.getEmpleado() == null ? "-" : o.getEmpleado(),
                o.getEstado() == CabOrdenDeProduccionSemanal.COMPLETADA ? "Completada" : "Pendiente",
                o.getFechaCompletada() == null ? "" : o.getFechaCompletada()});
        }
        modeloDetalle.setRowCount(0);
    }

    private void cargarDetalle() {
        Optional<CabOrdenDeProduccionSemanal> orden = seleccionada(false);
        modeloDetalle.setRowCount(0);
        orden.ifPresent(o -> dao.listarDetalle(o.getOrdenProduccionID()).forEach(d ->
                modeloDetalle.addRow(new Object[] {d.getDescripProducto(), d.getCantidad()})));
    }

    private Optional<CabOrdenDeProduccionSemanal> seleccionada(boolean avisar) {
        int fila = Tablas.filaSeleccionada(tblOrdenes);
        if (fila < 0) {
            if (avisar) {
                Dialogos.aviso(this, "Seleccione una orden en la tabla.");
            }
            return Optional.empty();
        }
        return Optional.of(ordenes.get(fila));
    }

    private void completar() {
        seleccionada(true).ifPresent(o -> {
            if (Dialogos.confirmar(this, "¿Completar la orden N.º " + o.getOrdenProduccionID() + "?\n\n"
                    + "Se sumarán los productos a las existencias y se descontarán los insumos de sus recetas.")) {
                servicio.completar(o.getOrdenProduccionID());
                Dialogos.info(this, "Orden N.º " + o.getOrdenProduccionID() + " completada.");
                cargarOrdenes();
            }
        });
    }

    private void eliminar() {
        seleccionada(true).ifPresent(o -> {
            if (Dialogos.confirmar(this, "¿Eliminar la orden N.º " + o.getOrdenProduccionID() + "?")) {
                servicio.eliminar(o.getOrdenProduccionID());
                cargarOrdenes();
            }
        });
    }

    private void nuevaOrden() {
        Window padre = SwingUtilities.getWindowAncestor(this);
        JDialog dialogo = new JDialog(padre, "Nueva orden de producción", JDialog.ModalityType.APPLICATION_MODAL);
        List<Producto> productos = new ProductoDao().listar(false, "");
        List<Area> areas = new AreaDao().listarTodas();
        List<DetOrdenDeProduccionSemanal> lineas = new ArrayList<>();

        JTextField txtFecha = new JTextField(LocalDate.now().toString(), 10);
        SelectorFecha.instalar(txtFecha);
        JComboBox<String> cmbArea = new JComboBox<>();
        areas.forEach(a -> cmbArea.addItem(a.getDescripArea()));
        cmbArea.setEditable(true);
        JComboBox<String> cmbProducto = new JComboBox<>();
        productos.forEach(p -> cmbProducto.addItem(p.getDescripProducto()));
        JTextField txtCantidad = new JTextField(5);
        DefaultTableModel modelo = new DefaultTableModel(new String[] {"Producto", "Cantidad"}, 0);
        JTable tabla = new JTable(modelo);
        Tablas.configurar(tabla);

        Runnable refrescar = () -> {
            modelo.setRowCount(0);
            lineas.forEach(l -> modelo.addRow(new Object[] {l.getDescripProducto(), l.getCantidad()}));
        };
        Runnable agregar = () -> {
            Optional<Integer> cantidad = Validaciones.enteroPositivo(txtCantidad.getText());
            if (cmbProducto.getSelectedIndex() < 0 || cantidad.isEmpty()) {
                Dialogos.aviso(dialogo, "Elija un producto e indique una cantidad entera mayor que cero.");
                return;
            }
            Producto p = productos.get(cmbProducto.getSelectedIndex());
            Optional<DetOrdenDeProduccionSemanal> existente = lineas.stream()
                    .filter(l -> l.getProductoID() == p.getProductoID()).findFirst();
            if (existente.isPresent()) {
                existente.get().setCantidad(existente.get().getCantidad() + cantidad.get());
            } else {
                DetOrdenDeProduccionSemanal l = new DetOrdenDeProduccionSemanal(p.getProductoID(), cantidad.get());
                l.setDescripProducto(p.getDescripProducto());
                lineas.add(l);
            }
            txtCantidad.setText("");
            refrescar.run();
        };
        JButton btnAgregar = new JButton("Agregar");
        btnAgregar.addActionListener(e -> agregar.run());
        txtCantidad.addActionListener(e -> agregar.run());
        JButton btnQuitar = new JButton("Quitar");
        btnQuitar.addActionListener(e -> {
            int fila = Tablas.filaSeleccionada(tabla);
            if (fila >= 0) {
                lineas.remove(fila);
                refrescar.run();
            }
        });

        JPanel datos = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.LINE_START;
        datos.add(new JLabel("Fecha:"), c);
        datos.add(txtFecha, c);
        datos.add(new JLabel("Área:"), c);
        datos.add(cmbArea, c);
        JPanel linea = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linea.add(new JLabel("Producto:"));
        linea.add(cmbProducto);
        linea.add(new JLabel("Cantidad:"));
        linea.add(txtCantidad);
        linea.add(btnAgregar);
        linea.add(btnQuitar);
        JPanel centro = new JPanel(new BorderLayout());
        centro.add(linea, BorderLayout.NORTH);
        centro.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JButton btnCrear = new JButton("Crear orden");
        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialogo.dispose());
        btnCrear.addActionListener(e -> {
            Object area = cmbArea.getEditor().getItem();
            int numero = servicio.crear(txtFecha.getText(), area == null ? "" : area.toString(), lineas);
            Dialogos.info(dialogo, "Orden N.º " + numero + " creada.");
            dialogo.dispose();
            cargarOrdenes();
        });
        JPanel botones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        botones.add(btnCrear);
        botones.add(btnCancelar);

        JPanel contenido = new JPanel(new BorderLayout(6, 6));
        contenido.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        contenido.add(datos, BorderLayout.NORTH);
        contenido.add(centro, BorderLayout.CENTER);
        contenido.add(botones, BorderLayout.SOUTH);
        dialogo.setContentPane(contenido);
        dialogo.setSize(620, 440);
        dialogo.setLocationRelativeTo(padre);
        dialogo.setVisible(true);
    }
}
