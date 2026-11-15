package restaurante.ui;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.PlatoDao;
import restaurante.dao.ProductoDao;
import restaurante.dao.TipProductoDao;
import restaurante.modelo.Plato;
import restaurante.modelo.Producto;
import restaurante.modelo.TipProducto;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;
import restaurante.util.Validaciones;

/**
 * Mantenimiento de productos de la carta.
 *
 * <p>Los tipos de plato y de producto se leen de la base de datos; antes sus
 * identificadores estaban escritos en el código y no coincidían con los de la
 * tabla al editar un producto.</p>
 */
public class FrmProducto extends javax.swing.JInternalFrame {

    /** Texto del código mientras el registro aún no se ha grabado. */
    private static final String NUEVO = "(nuevo)";

    private final ProductoDao prodDao = new ProductoDao();
    private final BusquedaDiferida busqueda;
    private final List<Plato> platos;
    private final List<TipProducto> tipos;
    private List<Producto> productos = List.of();
    private Producto seleccionado;

    public FrmProducto() {
        initComponents();
        this.platos = new PlatoDao().listar();
        this.tipos = new TipProductoDao().listar();
        this.cmbPlato.addItem("");
        this.platos.forEach(p -> this.cmbPlato.addItem(p.getDescPlato()));
        this.cmbTipProduct.addItem("");
        this.tipos.forEach(t -> this.cmbTipProduct.addItem(t.getDescripcion()));
        Tablas.configurar(this.tblProducto);
        Tablas.alSeleccionar(this.tblProducto, this::cargarSeleccion);
        this.txtProductoID.setEditable(false);
        this.txtProductoID.setToolTipText("El código se asigna automáticamente al grabar");
        this.txtPrecioUnit.setToolTipText("Por ejemplo: 25.50");
        this.busqueda = new BusquedaDiferida(this.txtBuscar, this::buscar);
        this.getRootPane().setDefaultButton(this.btnGrabar);
        this.buscar("");
        this.limpiar();
    }

    private void buscar(String texto) {
        productos = prodDao.listar(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblProducto);
        for (Producto p : productos) {
            modelo.addRow(new Object[] {p.getProductoID(), p.getDescripProducto(), p.getCantidad(),
                Tablas.importe(p.getPrecioUnit()), descripcionPlato(p.getPlato()), descripcionTipo(p.getTipo())});
        }
    }

    private String descripcionPlato(int id) {
        return platos.stream().filter(p -> p.getIdPlato() == id).map(Plato::getDescPlato).findFirst().orElse("");
    }

    private String descripcionTipo(int id) {
        return tipos.stream().filter(t -> t.getIdTipProducto() == id).map(TipProducto::getDescripcion)
                .findFirst().orElse("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtProductoID = new javax.swing.JTextField();
        txtDescripcion = new javax.swing.JTextField();
        txtCantidad = new javax.swing.JTextField();
        txtPrecioUnit = new javax.swing.JTextField();
        cmbPlato = new javax.swing.JComboBox<>();
        cmbTipProduct = new javax.swing.JComboBox<>();
        btnLimpia = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProducto = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnEliminar = new javax.swing.JButton();
        btnGrabar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Código");

        jLabel2.setText("Descripción");

        jLabel3.setText("Cantidad");

        jLabel4.setText("Precio Unitario");

        jLabel5.setText("Plato");

        jLabel6.setText("Tipo de Producto");

        txtProductoID.setEditable(false);

        btnLimpia.setText("Limpiar");
        btnLimpia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiaActionPerformed(evt);
            }
        });

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descripción", "Cantidad", "Precio Unitario", "Plato", "Tipo de Producto"
            }
        ));
        tblProducto.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProductoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblProducto);
        if (tblProducto.getColumnModel().getColumnCount() > 0) {
            tblProducto.getColumnModel().getColumn(0).setResizable(false);
            tblProducto.getColumnModel().getColumn(1).setResizable(false);
            tblProducto.getColumnModel().getColumn(2).setResizable(false);
            tblProducto.getColumnModel().getColumn(3).setResizable(false);
            tblProducto.getColumnModel().getColumn(5).setResizable(false);
        }

        jLabel7.setText("Buscar");

        txtBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarActionPerformed(evt);
            }
        });
        txtBuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 7, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 222, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49))
        );

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(51, 51, 51));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(jLabel1)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 31, Short.MAX_VALUE)
                                .addComponent(btnLimpia)
                                .addGap(58, 58, 58)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPrecioUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(btnGrabar)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(cmbTipProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(10, 10, 10)
                                                .addComponent(cmbPlato, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                    .addGap(10, 10, 10))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnEliminar)
                        .addGap(126, 126, 126)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnEliminar)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtProductoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtDescripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtCantidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtPrecioUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cmbPlato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbTipProduct, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(85, 85, 85)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpia)
                            .addComponent(btnGrabar)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(74, 74, 74))))
        );

        jTabbedPane1.addTab("Producto", jPanel1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 834, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnLimpiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiaActionPerformed
        this.limpiar();
    }//GEN-LAST:event_btnLimpiaActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        busqueda.reiniciar();
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
        this.cargarSeleccion();
    }//GEN-LAST:event_tblProductoMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        this.buscar(this.txtBuscar.getText().trim());
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void grabar() {
        String descripcion = this.txtDescripcion.getText().trim();
        Optional<Integer> cantidad = Validaciones.enteroNoNegativo(this.txtCantidad.getText());
        Optional<BigDecimal> precio = Validaciones.importe(this.txtPrecioUnit.getText());
        if (descripcion.isEmpty() || descripcion.length() > 40) {
            Dialogos.aviso(this, "Indique la descripción del producto (máximo 40 caracteres).");
            this.txtDescripcion.requestFocusInWindow();
            return;
        }
        if (cantidad.isEmpty()) {
            Dialogos.aviso(this, "La cantidad debe ser un número entero mayor o igual que cero.");
            this.txtCantidad.requestFocusInWindow();
            return;
        }
        if (precio.isEmpty()) {
            Dialogos.aviso(this, "El precio unitario debe ser un importe mayor o igual que cero (con hasta dos decimales).");
            this.txtPrecioUnit.requestFocusInWindow();
            return;
        }
        if (this.cmbPlato.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el tipo de plato.");
            this.cmbPlato.requestFocusInWindow();
            return;
        }
        if (this.cmbTipProduct.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el tipo de producto.");
            this.cmbTipProduct.requestFocusInWindow();
            return;
        }
        Producto prod = new Producto();
        prod.setDescripProducto(descripcion);
        prod.setCantidad(cantidad.get());
        prod.setPrecioUnit(precio.get());
        prod.setPlato(platos.get(this.cmbPlato.getSelectedIndex() - 1).getIdPlato());
        prod.setTipo(tipos.get(this.cmbTipProduct.getSelectedIndex() - 1).getIdTipProducto());
        if (seleccionado == null) {
            prodDao.insertar(prod);
            Dialogos.info(this, "Producto registrado con el código " + prod.getProductoID() + ".");
        } else {
            prod.setProductoID(seleccionado.getProductoID());
            prodDao.actualizar(prod);
            Dialogos.info(this, "Producto actualizado.");
        }
        this.buscar(this.txtBuscar.getText().trim());
        this.limpiar();
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla el producto que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar el producto \"" + seleccionado.getDescripProducto() + "\"?")) {
            return;
        }
        prodDao.eliminar(seleccionado);
        this.buscar(this.txtBuscar.getText().trim());
        this.limpiar();
    }

    private void cargarSeleccion() {
        int fila = Tablas.filaSeleccionada(this.tblProducto);
        if (fila < 0) {
            return;
        }
        seleccionado = productos.get(fila);
        this.txtProductoID.setText(String.valueOf(seleccionado.getProductoID()));
        this.txtDescripcion.setText(seleccionado.getDescripProducto());
        this.txtCantidad.setText(String.valueOf(seleccionado.getCantidad()));
        this.txtPrecioUnit.setText(Tablas.importe(seleccionado.getPrecioUnit()));
        this.cmbPlato.setSelectedItem(descripcionPlato(seleccionado.getPlato()));
        this.cmbTipProduct.setSelectedItem(descripcionTipo(seleccionado.getTipo()));
        this.btnGrabar.setText("Actualizar");
    }

    private void limpiar() {
        seleccionado = null;
        this.tblProducto.clearSelection();
        this.txtProductoID.setText(NUEVO);
        this.txtDescripcion.setText("");
        this.txtCantidad.setText("");
        this.txtPrecioUnit.setText("");
        this.cmbPlato.setSelectedIndex(0);
        this.cmbTipProduct.setSelectedIndex(0);
        this.btnGrabar.setText("Grabar");
        this.txtDescripcion.requestFocusInWindow();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnLimpia;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbPlato;
    private javax.swing.JComboBox<String> cmbTipProduct;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblProducto;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCantidad;
    private javax.swing.JTextField txtDescripcion;
    private javax.swing.JTextField txtPrecioUnit;
    private javax.swing.JTextField txtProductoID;
    // End of variables declaration//GEN-END:variables
}
