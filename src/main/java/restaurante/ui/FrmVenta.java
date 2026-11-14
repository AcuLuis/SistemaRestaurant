package restaurante.ui;

import java.math.BigDecimal;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.VentaDao;
import restaurante.dao.ClienteDao;
import restaurante.dao.EmpleadoDao;
import restaurante.dao.MesaDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.TipProductoDao;
import restaurante.modelo.CabOrdenDeVenta;
import restaurante.modelo.Cliente;
import restaurante.modelo.DetOrdenDeVenta;
import restaurante.modelo.Empleado;
import restaurante.modelo.Mesa;
import restaurante.modelo.Producto;
import restaurante.modelo.TipProducto;
import restaurante.seguridad.Sesion;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;
import restaurante.ui.util.Validaciones;

/**
 * Registro de ventas e historial de compras por cliente.
 */
public class FrmVenta extends javax.swing.JFrame {

    private static final String EFECTIVO = "Efectivo";
    private static final String CREDITO = "Crédito";
    private static final String COMER_EN_LOCAL = "ComerEnLocal";
    private static final String DELIVERY = "Delivery";
    private static final String PARA_LLEVAR = "ParaLlevar";

    private final VentaDao ventaDao = new VentaDao();
    private final ClienteDao clienteDao = new ClienteDao();
    private final ProductoDao productoDao = new ProductoDao();
    private final List<Empleado> empleados;
    private final List<Mesa> mesas;
    private final List<TipProducto> tipos;
    private final List<LineaVenta> carrito = new ArrayList<>();
    private final BusquedaDiferida busqueda;
    private List<Cliente> clientesConCompras = List.of();
    private List<CabOrdenDeVenta> ventasCliente = List.of();
    private Cliente cliente;
    private Producto producto;

    /** Producto añadido a la venta, con el precio vigente al añadirlo. */
    private static final class LineaVenta {
        private final Producto producto;
        private int cantidad;

        LineaVenta(Producto producto, int cantidad) {
            this.producto = producto;
            this.cantidad = cantidad;
        }

        BigDecimal subtotal() {
            return producto.getPrecioUnit().multiply(BigDecimal.valueOf(cantidad));
        }
    }

    public FrmVenta() {
        initComponents();
        this.setTitle("Ventas - Sistema Restaurant");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.empleados = new EmpleadoDao().listar(false, "");
        this.mesas = new MesaDao().listar();
        this.tipos = new TipProductoDao().listar();
        this.cmbEmpleado.addItem("");
        this.empleados.forEach(e -> this.cmbEmpleado.addItem(e.getNombres() + " " + e.getApellidos()));
        this.cmbMesa.addItem("");
        this.mesas.forEach(m -> this.cmbMesa.addItem(m.getUbic()));
        this.cmbMetodoPago.addItem("");
        this.cmbMetodoPago.addItem(EFECTIVO);
        this.cmbMetodoPago.addItem(CREDITO);
        for (JTable tabla : new JTable[] {tblClientes, tblCompraClie, tblDetCompraClie, tblProducto}) {
            Tablas.configurar(tabla);
        }
        Tablas.alSeleccionar(this.tblClientes, this::cargarVentasCliente);
        Tablas.alSeleccionar(this.tblCompraClie, this::cargarDetalleVenta);
        for (JTextField campo : new JTextField[] {txtIdCliente, txtNombreClie, txtApellidosClie, txtTelefonoClie,
            txtIdProducto, txtDescripProd, txtTipProd, txtPrecio}) {
            campo.setEditable(false);
        }
        this.txtDireccionClie.setToolTipText("Dirección de entrega (se puede modificar para este pedido)");
        this.txtCantProd.setToolTipText("Pulse Intro para agregar el producto");
        this.rbtnComerEnLocal.addItemListener(e -> actualizarMesa());
        this.rbtnDelivery.addItemListener(e -> actualizarMesa());
        this.rbtnParaLlevar.addItemListener(e -> actualizarMesa());
        configurarCarrito();
        this.busqueda = new BusquedaDiferida(this.txtBuscaCliente, this::llenaTblCliente);
        this.jTabbedPane1.setSelectedIndex(1);
        this.setLocationRelativeTo(null);
        this.limpiarVenta();
        this.llenaTblCliente("");
    }

    private void configurarCarrito() {
        this.tblProducto.setToolTipText("Seleccione un producto y pulse Supr para quitarlo de la venta");
        JPopupMenu menu = new JPopupMenu();
        JMenuItem quitar = new JMenuItem("Quitar de la venta");
        quitar.addActionListener(e -> quitarSeleccionado());
        menu.add(quitar);
        this.tblProducto.setComponentPopupMenu(menu);
        this.tblProducto.getInputMap(JComponent.WHEN_FOCUSED)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "quitar");
        this.tblProducto.getActionMap().put("quitar", new AbstractAction() {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                quitarSeleccionado();
            }
        });
    }

    private String descripcionTipo(int id) {
        return tipos.stream().filter(t -> t.getIdTipProducto() == id).map(TipProducto::getDescripcion)
                .findFirst().orElse("");
    }

    private String descripcionMesa(int id) {
        return mesas.stream().filter(m -> m.getMesaID() == id).map(Mesa::getUbic).findFirst().orElse("-");
    }

    private static String descripcionEntrega(String codigo) {
        if (COMER_EN_LOCAL.equals(codigo)) {
            return "Comer en local";
        }
        if (PARA_LLEVAR.equals(codigo)) {
            return "Para llevar";
        }
        return codigo == null ? "" : "Delivery";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtBuscaCliente = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblClientes = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCompraClie = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblDetCompraClie = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        rbtnDelivery = new javax.swing.JRadioButton();
        rbtnParaLlevar = new javax.swing.JRadioButton();
        rbtnComerEnLocal = new javax.swing.JRadioButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtNombreClie = new javax.swing.JTextField();
        txtApellidosClie = new javax.swing.JTextField();
        txtIdCliente = new javax.swing.JTextField();
        btnBuscarCliente = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtDireccionClie = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTelefonoClie = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        cmbMesa = new javax.swing.JComboBox<>();
        jLabel14 = new javax.swing.JLabel();
        cmbMetodoPago = new javax.swing.JComboBox<>();
        btnCrearCliente = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        cmbEmpleado = new javax.swing.JComboBox<>();
        jPanel4 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        txtIdProducto = new javax.swing.JTextField();
        txtDescripProd = new javax.swing.JTextField();
        txtTipProd = new javax.swing.JTextField();
        txtPrecio = new javax.swing.JTextField();
        txtCantProd = new javax.swing.JTextField();
        btnBuscarProducto = new javax.swing.JButton();
        btnAgregarProducto = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        btnRegistrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        canvas1 = new java.awt.Canvas();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Registro de venta");

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Cliente"));

        jLabel15.setText("Buscar:");

        txtBuscaCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscaClienteActionPerformed(evt);
            }
        });
        txtBuscaCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscaClienteKeyReleased(evt);
            }
        });

        tblClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nombre", "Apellidos", "Estado"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblClientesMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblClientes);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel15)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtBuscaCliente)))
                .addContainerGap())
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(txtBuscaCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder("Compras del cliente"));

        tblCompraClie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "N.º venta", "Cliente", "Mesa", "Fecha", "Método de pago", "Tipo de entrega"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCompraClie.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCompraClieMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblCompraClie);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Detalle de la compra"));

        tblDetCompraClie.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripción", "Tipo", "Precio"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(tblDetCompraClie);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 451, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(34, 34, 34)
                .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(77, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Historial de ventas", jPanel1);

        jLabel2.setText("Tipo de entrega");

        buttonGroup1.add(rbtnDelivery);
        rbtnDelivery.setText("Delivery");
        rbtnDelivery.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbtnDeliveryActionPerformed(evt);
            }
        });

        buttonGroup1.add(rbtnParaLlevar);
        rbtnParaLlevar.setText("Para llevar");

        buttonGroup1.add(rbtnComerEnLocal);
        rbtnComerEnLocal.setText("Comer en el local");

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del cliente"));

        jLabel3.setText("Nombre:");

        jLabel4.setText("Apellidos:");

        jLabel5.setText("Código:");

        txtNombreClie.setEnabled(false);

        txtApellidosClie.setEnabled(false);

        txtIdCliente.setEnabled(false);
        txtIdCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdClienteActionPerformed(evt);
            }
        });

        btnBuscarCliente.setText("Buscar cliente");
        btnBuscarCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarClienteActionPerformed(evt);
            }
        });

        jLabel6.setText("Dirección:");

        jLabel7.setText("Teléfono:");

        txtTelefonoClie.setEnabled(false);

        jLabel13.setText("Mesa:");

        jLabel14.setText("Método de pago:");

        btnCrearCliente.setText("Nuevo cliente");
        btnCrearCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCrearClienteActionPerformed(evt);
            }
        });

        jLabel16.setText("Empleado:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addGap(22, 22, 22)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(218, 218, 218)
                                        .addComponent(jLabel14))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtTelefonoClie, javax.swing.GroupLayout.DEFAULT_SIZE, 108, Short.MAX_VALUE)
                                            .addComponent(txtApellidosClie)
                                            .addComponent(txtIdCliente)
                                            .addComponent(txtNombreClie)
                                            .addComponent(txtDireccionClie))
                                        .addGap(95, 95, 95)
                                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                                .addComponent(jLabel13)
                                                .addGap(8, 8, 8))
                                            .addComponent(jLabel16, javax.swing.GroupLayout.Alignment.TRAILING))))))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbEmpleado, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 17, Short.MAX_VALUE)))
                                .addContainerGap())
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel7)
                                .addComponent(jLabel6))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(btnCrearCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(75, 75, 75)
                                .addComponent(btnBuscarCliente)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtIdCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(cmbMesa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNombreClie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel16)
                            .addComponent(cmbEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 5, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cmbMetodoPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(txtApellidosClie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtDireccionClie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTelefonoClie, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(btnCrearCliente)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBuscarCliente)
                        .addGap(17, 17, 17))))
        );

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos del Producto"));

        jLabel8.setText("Código:");

        jLabel9.setText("Descripción:");

        jLabel10.setText("Tipo:");

        jLabel11.setText("Precio:");

        jLabel12.setText("Cantidad:");

        txtIdProducto.setEnabled(false);

        txtDescripProd.setEnabled(false);

        txtTipProd.setEnabled(false);

        txtPrecio.setEnabled(false);
        txtPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPrecioActionPerformed(evt);
            }
        });

        txtCantProd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtCantProdActionPerformed(evt);
            }
        });

        btnBuscarProducto.setText("Buscar producto");
        btnBuscarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarProductoActionPerformed(evt);
            }
        });

        btnAgregarProducto.setText("Agregar producto");
        btnAgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarProductoActionPerformed(evt);
            }
        });

        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripción", "Tipo", "Precio", "Cantidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblProducto);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 465, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addComponent(btnRegistrar)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addComponent(btnRegistrar)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127))
                            .addComponent(btnBuscarProducto, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(9, 9, 9))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9)
                            .addComponent(jLabel10)
                            .addComponent(jLabel12))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtTipProd, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripProd, javax.swing.GroupLayout.PREFERRED_SIZE, 360, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(txtCantProd, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAgregarProducto)))))
                .addGap(27, 27, 27))
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtIdProducto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnBuscarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)))
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtDescripProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(txtTipProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(txtPrecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtCantProd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnAgregarProducto, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addComponent(rbtnDelivery)
                                .addGap(47, 47, 47)
                                .addComponent(rbtnParaLlevar)
                                .addGap(34, 34, 34)
                                .addComponent(rbtnComerEnLocal))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(36, 36, 36))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(rbtnDelivery)
                            .addComponent(rbtnParaLlevar)
                            .addComponent(rbtnComerEnLocal))
                        .addGap(8, 8, 8)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Venta", jPanel2);

        jButton1.setBackground(new java.awt.Color(51, 51, 51));
        jButton1.setText("Cerrar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(516, 516, 516)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(280, 280, 280)
                        .addComponent(canvas1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1234, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(128, 128, 128))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(canvas1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 453, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rbtnDeliveryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbtnDeliveryActionPerformed
        this.actualizarMesa();
    }//GEN-LAST:event_rbtnDeliveryActionPerformed

    private void txtIdClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdClienteActionPerformed
    }//GEN-LAST:event_txtIdClienteActionPerformed

    private void txtPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPrecioActionPerformed
    }//GEN-LAST:event_txtPrecioActionPerformed

    private void txtBuscaClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscaClienteActionPerformed
        this.llenaTblCliente(this.txtBuscaCliente.getText().trim());
    }//GEN-LAST:event_txtBuscaClienteActionPerformed

    private void btnCrearClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCrearClienteActionPerformed
        CrearCliente ventana = new CrearCliente();
        ventana.alRegistrar(this::usarCliente);
        ventana.setVisible(true);
    }//GEN-LAST:event_btnCrearClienteActionPerformed

    private void btnBuscarClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarClienteActionPerformed
        SelCliente dialogo = new SelCliente(this, true);
        dialogo.setVisible(true);
        Cliente elegido = dialogo.getClienteElegido();
        if (elegido != null) {
            this.usarCliente(elegido);
        }
    }//GEN-LAST:event_btnBuscarClienteActionPerformed

    private void btnBuscarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarProductoActionPerformed
        SelProducto dialogo = new SelProducto(this, true);
        dialogo.setVisible(true);
        Producto elegido = dialogo.getProductoElegido();
        if (elegido != null) {
            this.usarProducto(elegido);
        }
    }//GEN-LAST:event_btnBuscarProductoActionPerformed

    private void btnAgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarProductoActionPerformed
        this.agregarProducto();
    }//GEN-LAST:event_btnAgregarProductoActionPerformed

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        this.registrar();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void txtBuscaClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscaClienteKeyReleased
        busqueda.reiniciar();
    }//GEN-LAST:event_txtBuscaClienteKeyReleased

    private void tblClientesMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblClientesMouseClicked
        this.cargarVentasCliente();
    }//GEN-LAST:event_tblClientesMouseClicked

    private void tblCompraClieMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCompraClieMouseClicked
        this.cargarDetalleVenta();
    }//GEN-LAST:event_tblCompraClieMouseClicked

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txtCantProdActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtCantProdActionPerformed
        this.agregarProducto();
    }//GEN-LAST:event_txtCantProdActionPerformed

    // ------------------------------------------------------------- Venta

    private void usarCliente(Cliente c) {
        this.cliente = c;
        this.txtIdCliente.setText(String.valueOf(c.getClienteID()));
        this.txtNombreClie.setText(c.getNombres());
        this.txtApellidosClie.setText(c.getApellidos());
        this.txtDireccionClie.setText(c.getDireccion() == null ? "" : c.getDireccion());
        this.txtTelefonoClie.setText(c.getTelefono() == null ? "" : c.getTelefono());
    }

    private void usarProducto(Producto p) {
        this.producto = p;
        this.txtIdProducto.setText(String.valueOf(p.getProductoID()));
        this.txtDescripProd.setText(p.getDescripProducto());
        this.txtTipProd.setText(descripcionTipo(p.getTipo()));
        this.txtPrecio.setText(Tablas.importe(p.getPrecioUnit()));
        this.txtCantProd.setText("1");
        this.txtCantProd.requestFocusInWindow();
        this.txtCantProd.selectAll();
    }

    private void agregarProducto() {
        if (producto == null) {
            Dialogos.aviso(this, "Busque y seleccione un producto.");
            this.btnBuscarProducto.requestFocusInWindow();
            return;
        }
        Optional<Integer> cantidad = Validaciones.enteroPositivo(this.txtCantProd.getText());
        if (cantidad.isEmpty()) {
            Dialogos.aviso(this, "La cantidad debe ser un número entero mayor que cero.");
            this.txtCantProd.requestFocusInWindow();
            return;
        }
        Optional<LineaVenta> existente = carrito.stream()
                .filter(l -> l.producto.getProductoID() == producto.getProductoID()).findFirst();
        if (existente.isPresent()) {
            existente.get().cantidad += cantidad.get();
        } else {
            carrito.add(new LineaVenta(producto, cantidad.get()));
        }
        refrescarCarrito();
        limpiaDet();
    }

    private void quitarSeleccionado() {
        int fila = Tablas.filaSeleccionada(this.tblProducto);
        if (fila >= 0) {
            carrito.remove(fila);
            refrescarCarrito();
        }
    }

    private void refrescarCarrito() {
        DefaultTableModel modelo = Tablas.vaciar(this.tblProducto);
        for (LineaVenta l : carrito) {
            modelo.addRow(new Object[] {l.producto.getProductoID(), l.producto.getDescripProducto(),
                descripcionTipo(l.producto.getTipo()), Tablas.importe(l.producto.getPrecioUnit()), l.cantidad});
        }
        this.jScrollPane1.setBorder(BorderFactory.createTitledBorder("Productos de la venta   |   Total: "
                + Tablas.importe(total())));
    }

    private BigDecimal total() {
        return carrito.stream().map(LineaVenta::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void actualizarMesa() {
        boolean enLocal = this.rbtnComerEnLocal.isSelected();
        this.cmbMesa.setEnabled(enLocal);
        if (!enLocal) {
            this.cmbMesa.setSelectedIndex(0);
        }
    }

    private void registrar() {
        String direccion = this.txtDireccionClie.getText().trim();
        if (cliente == null) {
            Dialogos.aviso(this, "Busque un cliente o registre uno nuevo.");
            return;
        }
        if (carrito.isEmpty()) {
            Dialogos.aviso(this, "Agregue al menos un producto a la venta.");
            return;
        }
        if (!rbtnComerEnLocal.isSelected() && !rbtnDelivery.isSelected() && !rbtnParaLlevar.isSelected()) {
            Dialogos.aviso(this, "Seleccione el tipo de entrega.");
            return;
        }
        if (rbtnComerEnLocal.isSelected() && this.cmbMesa.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione la mesa.");
            this.cmbMesa.requestFocusInWindow();
            return;
        }
        if (rbtnDelivery.isSelected() && direccion.isEmpty()) {
            Dialogos.aviso(this, "Indique la dirección de entrega.");
            this.txtDireccionClie.requestFocusInWindow();
            return;
        }
        if (this.cmbEmpleado.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el empleado que atiende la venta.");
            this.cmbEmpleado.requestFocusInWindow();
            return;
        }
        if (this.cmbMetodoPago.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el método de pago.");
            this.cmbMetodoPago.requestFocusInWindow();
            return;
        }
        if (!Dialogos.confirmar(this, "¿Registrar la venta a " + cliente.getNombres() + " " + cliente.getApellidos()
                + " por un total de " + Tablas.importe(total()) + "?")) {
            return;
        }
        CabOrdenDeVenta cab = new CabOrdenDeVenta();
        cab.setEmpleadoID(empleados.get(this.cmbEmpleado.getSelectedIndex() - 1).getEmpleadoID());
        cab.setClienteID(cliente.getClienteID());
        // Se conserva la codificación original: 1 = efectivo, 0 = crédito.
        cab.setMetodoPago(EFECTIVO.equals(this.cmbMetodoPago.getSelectedItem()) ? 1 : 0);
        String ubicacion;
        if (rbtnComerEnLocal.isSelected()) {
            Mesa mesa = mesas.get(this.cmbMesa.getSelectedIndex() - 1);
            cab.setMesaID(mesa.getMesaID());
            cab.setTipEntrega(COMER_EN_LOCAL);
            ubicacion = mesa.getUbic();
        } else if (rbtnDelivery.isSelected()) {
            cab.setTipEntrega(DELIVERY);
            ubicacion = direccion;
        } else {
            cab.setTipEntrega(PARA_LLEVAR);
            ubicacion = "Para llevar";
        }
        List<DetOrdenDeVenta> detalles = new ArrayList<>();
        for (LineaVenta l : carrito) {
            DetOrdenDeVenta det = new DetOrdenDeVenta();
            det.setProductoID(l.producto.getProductoID());
            det.setCantidad(l.cantidad);
            det.setPrecioUnit(l.producto.getPrecioUnit());
            detalles.add(det);
        }
        BigDecimal importe = total();
        int numero = this.ventaDao.registrarVenta(cab, ubicacion, detalles);
        Dialogos.info(this, "Venta N.º " + numero + " registrada correctamente.\nTotal: " + Tablas.importe(importe));
        this.limpiarVenta();
        this.llenaTblCliente(this.txtBuscaCliente.getText().trim());
    }

    private void limpiaDet() {
        this.producto = null;
        this.txtIdProducto.setText("");
        this.txtDescripProd.setText("");
        this.txtTipProd.setText("");
        this.txtPrecio.setText("");
        this.txtCantProd.setText("");
    }

    private void limpiarVenta() {
        this.cliente = null;
        this.txtIdCliente.setText("");
        this.txtNombreClie.setText("");
        this.txtApellidosClie.setText("");
        this.txtDireccionClie.setText("");
        this.txtTelefonoClie.setText("");
        this.buttonGroup1.clearSelection();
        this.cmbMetodoPago.setSelectedIndex(0);
        this.cmbEmpleado.setSelectedIndex(0);
        Integer empleadoSesion = Sesion.actual().map(Sesion::getEmpleadoID).orElse(null);
        for (int i = 0; empleadoSesion != null && i < empleados.size(); i++) {
            if (empleados.get(i).getEmpleadoID() == empleadoSesion) {
                this.cmbEmpleado.setSelectedIndex(i + 1);
            }
        }
        carrito.clear();
        refrescarCarrito();
        limpiaDet();
        actualizarMesa();
    }

    // ------------------------------------------------------------- Historial

    private void llenaTblCliente(String texto) {
        clientesConCompras = clienteDao.listarConCompras(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblClientes);
        for (Cliente c : clientesConCompras) {
            modelo.addRow(new Object[] {c.getClienteID(), c.getNombres(), c.getApellidos(),
                c.getEstado() == ClienteDao.ESTADO_ACTIVO ? "Activo" : "Inactivo"});
        }
        Tablas.vaciar(this.tblCompraClie);
        Tablas.vaciar(this.tblDetCompraClie);
    }

    private void cargarVentasCliente() {
        int fila = Tablas.filaSeleccionada(this.tblClientes);
        if (fila < 0) {
            return;
        }
        ventasCliente = ventaDao.listarPorCliente(clientesConCompras.get(fila).getClienteID());
        DefaultTableModel modelo = Tablas.vaciar(this.tblCompraClie);
        for (CabOrdenDeVenta v : ventasCliente) {
            modelo.addRow(new Object[] {v.getOrdenDeVentaID(), v.getClienteID(), descripcionMesa(v.getMesaID()),
                v.getFechaVenta(), v.getMetodoPago() == 1 ? EFECTIVO : CREDITO, descripcionEntrega(v.getTipEntrega())});
        }
        Tablas.vaciar(this.tblDetCompraClie);
    }

    private void cargarDetalleVenta() {
        int fila = Tablas.filaSeleccionada(this.tblCompraClie);
        if (fila < 0) {
            return;
        }
        BigDecimal totalVenta = BigDecimal.ZERO;
        DefaultTableModel modelo = Tablas.vaciar(this.tblDetCompraClie);
        for (Producto p : productoDao.listarPorVenta(ventasCliente.get(fila).getOrdenDeVentaID())) {
            modelo.addRow(new Object[] {p.getProductoID(), p.getCantidad() + " x " + p.getDescripProducto(),
                descripcionTipo(p.getTipo()), Tablas.importe(p.getPrecioUnit())});
            totalVenta = totalVenta.add(p.getPrecioUnit().multiply(BigDecimal.valueOf(p.getCantidad())));
        }
        this.jScrollPane4.setBorder(BorderFactory.createTitledBorder("Total de la venta: " + Tablas.importe(totalVenta)));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarProducto;
    private javax.swing.JButton btnBuscarCliente;
    private javax.swing.JButton btnBuscarProducto;
    private javax.swing.JButton btnCrearCliente;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.ButtonGroup buttonGroup1;
    private java.awt.Canvas canvas1;
    private javax.swing.JComboBox<String> cmbEmpleado;
    private javax.swing.JComboBox<String> cmbMesa;
    private javax.swing.JComboBox<String> cmbMetodoPago;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton rbtnComerEnLocal;
    private javax.swing.JRadioButton rbtnDelivery;
    private javax.swing.JRadioButton rbtnParaLlevar;
    private javax.swing.JTable tblClientes;
    private javax.swing.JTable tblCompraClie;
    private javax.swing.JTable tblDetCompraClie;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtApellidosClie;
    private javax.swing.JTextField txtBuscaCliente;
    private javax.swing.JTextField txtCantProd;
    private javax.swing.JTextField txtDescripProd;
    private javax.swing.JTextField txtDireccionClie;
    private javax.swing.JTextField txtIdCliente;
    private javax.swing.JTextField txtIdProducto;
    private javax.swing.JTextField txtNombreClie;
    private javax.swing.JTextField txtPrecio;
    private javax.swing.JTextField txtTelefonoClie;
    private javax.swing.JTextField txtTipProd;
    // End of variables declaration//GEN-END:variables

}
