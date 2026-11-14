package restaurante.ui;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.RolDao;
import restaurante.modelo.Rol;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;

/**
 * Mantenimiento de roles.
 */
public class FrmRol extends javax.swing.JInternalFrame {

    private static final int LONGITUD_MAXIMA = 25;

    private final RolDao dao = new RolDao();
    private final BusquedaDiferida busqueda;
    private List<Rol> registros = List.of();
    private Rol seleccionado;

    public FrmRol() {
        initComponents();
        Tablas.configurar(this.tblRol);
        Tablas.alSeleccionar(this.tblRol, this::cargarSeleccion);
        this.txtRolID.setEditable(false);
        this.txtRolID.setToolTipText("El código se asigna automáticamente al grabar");
        this.txtDescripcionRol.setToolTipText("Máximo " + LONGITUD_MAXIMA + " caracteres");
        this.busqueda = new BusquedaDiferida(this.txtBuscarRol, this::buscar);
        this.getRootPane().setDefaultButton(this.btnGrabar);
        this.buscar("");
        this.limpia();
    }

    private void buscar(String texto) {
        registros = dao.listaRol(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblRol);
        for (Rol r : registros) {
            modelo.addRow(new Object[] {r.getRolID(), r.getDescripRol()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtDescripcionRol = new javax.swing.JTextField();
        txtRolID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRol = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        txtBuscarRol = new javax.swing.JTextField();
        btnGrabar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FORMULARIO ROL");

        jLabel2.setText("Rol ID");

        jLabel3.setText("Descripción");

        txtRolID.setEnabled(false);

        tblRol.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Rol_ID", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblRol.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRolMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblRol);

        jLabel4.setText("Buscar");

        txtBuscarRol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarRolKeyReleased(evt);
            }
        });

        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Limpiar");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        btnCerrar.setBackground(new java.awt.Color(51, 51, 51));
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(80, 80, 80)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminar)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtRolID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDescripcionRol, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 74, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGrabar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnLimpiar)
                        .addGap(83, 83, 83)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(29, 29, 29)
                        .addComponent(txtBuscarRol, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
            .addGroup(layout.createSequentialGroup()
                .addGap(267, 267, 267)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(jLabel1)
                        .addGap(58, 58, 58)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(txtBuscarRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(txtRolID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 275, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtDescripcionRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGrabar)
                            .addComponent(btnLimpiar))
                        .addGap(44, 44, 44)
                        .addComponent(btnEliminar)
                        .addGap(46, 46, 46)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarRolKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarRolKeyReleased
        busqueda.reiniciar();
    }//GEN-LAST:event_txtBuscarRolKeyReleased

    private void tblRolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRolMouseClicked
        this.cargarSeleccion();
    }//GEN-LAST:event_tblRolMouseClicked

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        this.limpia();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void grabar() {
        String descripcion = this.txtDescripcionRol.getText().trim();
        if (descripcion.isEmpty()) {
            Dialogos.aviso(this, "Falta indicar la descripción.");
            this.txtDescripcionRol.requestFocusInWindow();
            return;
        }
        if (descripcion.length() > LONGITUD_MAXIMA) {
            Dialogos.aviso(this, "La descripción admite como máximo " + LONGITUD_MAXIMA + " caracteres.");
            this.txtDescripcionRol.requestFocusInWindow();
            return;
        }
        Rol registro = new Rol();
        registro.setDescripRol(descripcion);
        if (seleccionado == null) {
            dao.insertaRol(registro);
            Dialogos.info(this, "Rol registrado con el código " + registro.getRolID() + ".");
        } else {
            registro.setRolID(seleccionado.getRolID());
            dao.actualizarRol(registro);
            Dialogos.info(this, "Rol actualizado.");
        }
        this.buscar(this.txtBuscarRol.getText().trim());
        this.limpia();
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla el registro que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar \"" + seleccionado.getDescripRol() + "\"?")) {
            return;
        }
        dao.eliminarRol(seleccionado);
        this.buscar(this.txtBuscarRol.getText().trim());
        this.limpia();
    }

    private void cargarSeleccion() {
        int fila = Tablas.filaSeleccionada(this.tblRol);
        if (fila < 0) {
            return;
        }
        seleccionado = registros.get(fila);
        this.txtRolID.setText(String.valueOf(seleccionado.getRolID()));
        this.txtDescripcionRol.setText(seleccionado.getDescripRol());
        this.btnGrabar.setText("Actualizar");
    }

    private void limpia() {
        seleccionado = null;
        this.tblRol.clearSelection();
        this.txtRolID.setText(String.valueOf(dao.siguienteId()));
        this.txtDescripcionRol.setText("");
        this.btnGrabar.setText("Grabar");
        this.txtDescripcionRol.requestFocusInWindow();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblRol;
    private javax.swing.JTextField txtBuscarRol;
    private javax.swing.JTextField txtDescripcionRol;
    private javax.swing.JTextField txtRolID;
    // End of variables declaration//GEN-END:variables
}
