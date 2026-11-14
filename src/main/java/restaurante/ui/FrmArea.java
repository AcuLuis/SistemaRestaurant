package restaurante.ui;

import java.util.List;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.AreaDao;
import restaurante.modelo.Area;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;

/**
 * Mantenimiento de áreas.
 */
public class FrmArea extends javax.swing.JInternalFrame {

    private static final int LONGITUD_MAXIMA = 40;

    private final AreaDao dao = new AreaDao();
    private final BusquedaDiferida busqueda;
    private List<Area> registros = List.of();
    private Area seleccionado;

    public FrmArea() {
        initComponents();
        Tablas.configurar(this.tblArea);
        Tablas.alSeleccionar(this.tblArea, this::cargarSeleccion);
        this.txtAreaID.setEditable(false);
        this.txtAreaID.setToolTipText("El código se asigna automáticamente al grabar");
        this.txtDescripcionArea.setToolTipText("Máximo " + LONGITUD_MAXIMA + " caracteres");
        this.btnLimpiar.addActionListener(e -> this.limpia());
        this.busqueda = new BusquedaDiferida(this.txtBuscarArea, this::buscar);
        this.getRootPane().setDefaultButton(this.btnGrabar);
        this.buscar("");
        this.limpia();
    }

    private void buscar(String texto) {
        registros = dao.listaArea(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblArea);
        for (Area r : registros) {
            modelo.addRow(new Object[] {r.getAreaID(), r.getDescripArea()});
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblArea = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarArea = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtAreaID = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDescripcionArea = new javax.swing.JTextField();
        btnGrabar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FORMULARIO ÁREA");

        tblArea.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Área_ID", "Descripción"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAreaMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblArea);

        jLabel2.setText("Buscar");

        txtBuscarArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarAreaKeyReleased(evt);
            }
        });

        jLabel3.setText("Área ID");

        txtAreaID.setEnabled(false);

        jLabel4.setText("Descripción");

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

        btnCerrar.setBackground(new java.awt.Color(51, 51, 51));
        btnCerrar.setText("Cerrar");
        btnCerrar.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
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
                .addGap(259, 259, 259)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtDescripcionArea, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtAreaID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnGrabar)
                        .addGap(63, 63, 63)
                        .addComponent(btnLimpiar))
                    .addComponent(btnEliminar)
                    .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(79, 79, 79)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 432, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarArea, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtBuscarArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAreaID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(txtDescripcionArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(52, 52, 52)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnGrabar)
                            .addComponent(btnLimpiar))
                        .addGap(31, 31, 31)
                        .addComponent(btnEliminar)
                        .addGap(27, 27, 27)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarAreaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarAreaKeyReleased
        busqueda.reiniciar();
    }//GEN-LAST:event_txtBuscarAreaKeyReleased

    private void tblAreaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAreaMouseClicked
        this.cargarSeleccion();
    }//GEN-LAST:event_tblAreaMouseClicked

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void grabar() {
        String descripcion = this.txtDescripcionArea.getText().trim();
        if (descripcion.isEmpty()) {
            Dialogos.aviso(this, "Falta indicar la descripción.");
            this.txtDescripcionArea.requestFocusInWindow();
            return;
        }
        if (descripcion.length() > LONGITUD_MAXIMA) {
            Dialogos.aviso(this, "La descripción admite como máximo " + LONGITUD_MAXIMA + " caracteres.");
            this.txtDescripcionArea.requestFocusInWindow();
            return;
        }
        Area registro = new Area();
        registro.setDescripArea(descripcion);
        if (seleccionado == null) {
            dao.insertaArea(registro);
            Dialogos.info(this, "Área registrado con el código " + registro.getAreaID() + ".");
        } else {
            registro.setAreaID(seleccionado.getAreaID());
            dao.actualizarArea(registro);
            Dialogos.info(this, "Área actualizado.");
        }
        this.buscar(this.txtBuscarArea.getText().trim());
        this.limpia();
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla el registro que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar \"" + seleccionado.getDescripArea() + "\"?")) {
            return;
        }
        dao.eliminarArea(seleccionado);
        this.buscar(this.txtBuscarArea.getText().trim());
        this.limpia();
    }

    private void cargarSeleccion() {
        int fila = Tablas.filaSeleccionada(this.tblArea);
        if (fila < 0) {
            return;
        }
        seleccionado = registros.get(fila);
        this.txtAreaID.setText(String.valueOf(seleccionado.getAreaID()));
        this.txtDescripcionArea.setText(seleccionado.getDescripArea());
        this.btnGrabar.setText("Actualizar");
    }

    private void limpia() {
        seleccionado = null;
        this.tblArea.clearSelection();
        this.txtAreaID.setText(String.valueOf(dao.siguienteId()));
        this.txtDescripcionArea.setText("");
        this.btnGrabar.setText("Grabar");
        this.txtDescripcionArea.requestFocusInWindow();
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
    private javax.swing.JTable tblArea;
    private javax.swing.JTextField txtAreaID;
    private javax.swing.JTextField txtBuscarArea;
    private javax.swing.JTextField txtDescripcionArea;
    // End of variables declaration//GEN-END:variables
}
