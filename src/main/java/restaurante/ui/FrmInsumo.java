package restaurante.ui;

import java.math.BigDecimal;

import java.util.List;
import java.util.Optional;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.InsumoDao;
import restaurante.dao.TipInsumoDao;
import restaurante.modelo.Insumo;
import restaurante.modelo.TipInsumo;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;
import restaurante.util.Validaciones;

/**
 * Mantenimiento de insumos (materia prima).
 */
public class FrmInsumo extends javax.swing.JInternalFrame {

    /** Texto del código mientras el registro aún no se ha grabado. */
    private static final String NUEVO = "(nuevo)";

    private final InsumoDao insuDao = new InsumoDao();
    private final BusquedaDiferida busqueda;
    private final List<TipInsumo> tipos;
    private List<Insumo> insumos = List.of();
    private Insumo seleccionado;

    public FrmInsumo() {
        initComponents();
        this.tipos = new TipInsumoDao().listar();
        this.cmbTipIns.addItem("");
        this.tipos.forEach(t -> this.cmbTipIns.addItem(t.getDescripcion()));
        Tablas.configurar(this.tblIns);
        Tablas.alSeleccionar(this.tblIns, this::cargarSeleccion);
        this.txtInsumoID.setEditable(false);
        this.txtInsumoID.setToolTipText("El código se asigna automáticamente al grabar");
        this.txtUnidadMed.setToolTipText("Por ejemplo: kg, litro, unidad (máximo 10 caracteres)");
        this.busqueda = new BusquedaDiferida(this.txtBuscar, this::llenaTblIns);
        this.getRootPane().setDefaultButton(this.btnGrabar);
        this.llenaTblIns("");
        this.limpiar();
    }

    private void llenaTblIns(String texto) {
        insumos = insuDao.listar(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblIns);
        for (Insumo i : insumos) {
            modelo.addRow(new Object[] {i.getInsumoID(), descripcionTipo(i.getIdTipInsumo()), i.getUnidadMedida(),
                i.getDescripInsumo(), Tablas.importe(i.getPrecioUnit()), Tablas.cantidad(i.getCantidad())});
        }
    }

    private String descripcionTipo(int id) {
        return tipos.stream().filter(t -> t.getIdTipInsumo() == id).map(TipInsumo::getDescripcion).findFirst().orElse("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtInsumoID = new javax.swing.JTextField();
        txtUnidadMed = new javax.swing.JTextField();
        txtDesc = new javax.swing.JTextField();
        txtCant = new javax.swing.JTextField();
        txtPrecU = new javax.swing.JTextField();
        txtBuscar = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblIns = new javax.swing.JTable();
        btnLimpiar = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        cmbTipIns = new javax.swing.JComboBox<>();
        btnGrabar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FORMULARIO INSUMO");

        jLabel2.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel2.setText("Código:");

        jLabel3.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel3.setText("Unidad de Medida:");

        jLabel4.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel4.setText("Descripción:");

        jLabel5.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel5.setText("Precio por Unidad:");

        jLabel6.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel6.setText("Cantidad");

        jLabel7.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel7.setText("Tipo de Insumo:");

        jLabel8.setFont(new java.awt.Font("Trebuchet MS", 0, 11)); // NOI18N
        jLabel8.setText("Buscar:");

        txtInsumoID.setEnabled(false);

        txtUnidadMed.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUnidadMedActionPerformed(evt);
            }
        });

        txtDesc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDescActionPerformed(evt);
            }
        });

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

        tblIns.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Tipo", "Unidad", "Descripción", "Precio", "Cantidad"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblIns.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblInsMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblIns);

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(51, 51, 51));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        cmbTipIns.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbTipInsActionPerformed(evt);
            }
        });

        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(197, 197, 197)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtPrecU, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(65, 65, 65)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel4)
                            .addComponent(jLabel7)
                            .addComponent(jLabel2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addComponent(btnGrabar)
                                .addGap(24, 24, 24)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbTipIns, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtUnidadMed, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtInsumoID, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(48, 48, 48)
                                .addComponent(btnLimpiar)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 134, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addComponent(btnEliminar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(46, 46, 46))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addGap(32, 32, 32)
                                .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 262, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 538, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(27, Short.MAX_VALUE))))
            .addGroup(layout.createSequentialGroup()
                .addGap(390, 390, 390)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtInsumoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(cmbTipIns, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUnidadMed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtPrecU, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCant, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(32, 32, 32)))
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnEliminar)
                            .addComponent(btnLimpiar)
                            .addComponent(btnGrabar))
                        .addGap(27, 27, 27))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtUnidadMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnidadMedActionPerformed
    }//GEN-LAST:event_txtUnidadMedActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        this.limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        busqueda.reiniciar();
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void tblInsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsMouseClicked
        this.cargarSeleccion();
    }//GEN-LAST:event_tblInsMouseClicked

    private void txtDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescActionPerformed
    }//GEN-LAST:event_txtDescActionPerformed

    private void cmbTipInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipInsActionPerformed
    }//GEN-LAST:event_cmbTipInsActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        this.llenaTblIns(this.txtBuscar.getText().trim());
    }//GEN-LAST:event_txtBuscarActionPerformed

    private void grabar() {
        String unidad = this.txtUnidadMed.getText().trim();
        String descripcion = this.txtDesc.getText().trim();
        Optional<BigDecimal> precio = Validaciones.importe(this.txtPrecU.getText());
        Optional<BigDecimal> cantidad = Validaciones.cantidad(this.txtCant.getText());
        if (this.cmbTipIns.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el tipo de insumo.");
            this.cmbTipIns.requestFocusInWindow();
            return;
        }
        if (unidad.isEmpty() || unidad.length() > 10) {
            Dialogos.aviso(this, "Indique la unidad de medida (máximo 10 caracteres).");
            this.txtUnidadMed.requestFocusInWindow();
            return;
        }
        if (descripcion.isEmpty() || descripcion.length() > 60) {
            Dialogos.aviso(this, "Indique la descripción del insumo (máximo 60 caracteres).");
            this.txtDesc.requestFocusInWindow();
            return;
        }
        if (precio.isEmpty()) {
            Dialogos.aviso(this, "El precio debe ser un importe mayor o igual que cero (con hasta dos decimales).");
            this.txtPrecU.requestFocusInWindow();
            return;
        }
        if (cantidad.isEmpty()) {
            Dialogos.aviso(this, "La cantidad debe ser un número mayor o igual que cero (con hasta tres decimales).");
            this.txtCant.requestFocusInWindow();
            return;
        }
        Insumo insumo = new Insumo();
        insumo.setIdTipInsumo(tipos.get(this.cmbTipIns.getSelectedIndex() - 1).getIdTipInsumo());
        insumo.setUnidadMedida(unidad);
        insumo.setDescripInsumo(descripcion);
        insumo.setPrecioUnit(precio.get());
        insumo.setCantidad(cantidad.get());
        if (seleccionado == null) {
            this.insuDao.insertar(insumo);
            Dialogos.info(this, "Insumo registrado con el código " + insumo.getInsumoID() + ".");
        } else {
            insumo.setInsumoID(seleccionado.getInsumoID());
            this.insuDao.actualizar(insumo);
            Dialogos.info(this, "Insumo actualizado.");
        }
        this.llenaTblIns(this.txtBuscar.getText().trim());
        this.limpiar();
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla el insumo que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar el insumo \"" + seleccionado.getDescripInsumo() + "\"?")) {
            return;
        }
        this.insuDao.eliminar(seleccionado.getInsumoID());
        this.llenaTblIns(this.txtBuscar.getText().trim());
        this.limpiar();
    }

    private void cargarSeleccion() {
        int fila = Tablas.filaSeleccionada(this.tblIns);
        if (fila < 0) {
            return;
        }
        seleccionado = insumos.get(fila);
        this.txtInsumoID.setText(String.valueOf(seleccionado.getInsumoID()));
        this.cmbTipIns.setSelectedItem(descripcionTipo(seleccionado.getIdTipInsumo()));
        this.txtUnidadMed.setText(seleccionado.getUnidadMedida());
        this.txtDesc.setText(seleccionado.getDescripInsumo());
        this.txtPrecU.setText(Tablas.importe(seleccionado.getPrecioUnit()));
        this.txtCant.setText(Tablas.cantidad(seleccionado.getCantidad()));
        this.btnGrabar.setText("Actualizar");
    }

    /** Deja el formulario listo para registrar un insumo nuevo (antes conservaba el código anterior). */
    public void limpiar() {
        seleccionado = null;
        this.tblIns.clearSelection();
        this.txtInsumoID.setText(NUEVO);
        this.cmbTipIns.setSelectedIndex(0);
        this.txtUnidadMed.setText("");
        this.txtPrecU.setText("");
        this.txtDesc.setText("");
        this.txtCant.setText("");
        this.btnGrabar.setText("Grabar");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JComboBox<String> cmbTipIns;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblIns;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCant;
    private javax.swing.JTextField txtDesc;
    private javax.swing.JTextField txtInsumoID;
    private javax.swing.JTextField txtPrecU;
    private javax.swing.JTextField txtUnidadMed;
    // End of variables declaration//GEN-END:variables
}
