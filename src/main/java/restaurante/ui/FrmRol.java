
package restaurante.ui;

import restaurante.modelo.Rol;
import restaurante.dao.RolDao;
import restaurante.util.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmRol extends javax.swing.JInternalFrame {

    DefaultTableModel dtmRol;
    RolDao rolDao = new RolDao();
    Util u = new Util();
    public FrmRol() {
        initComponents();
        dtmRol = (DefaultTableModel) this.tblRol.getModel();
        this.llenaTblRol(false, "");
        
    }
    
    private void llenaTblRol(boolean valida, String cadena){
    
        Vector<Rol> listaRol;
        
        this.txtRolID.setText(Integer.toString(u.idNext("Rol", "rolID")));
        
        listaRol = rolDao.listaRol(valida, cadena);
        dtmRol.setRowCount(0);
        
        for(int i=0;i<listaRol.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaRol.get(i).getRolID());
            vector.addElement(listaRol.get(i).getDescripRol());
            
            dtmRol.addRow(vector);
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
        
        if(this.txtBuscarRol.getText().isEmpty()){
            this.llenaTblRol(false, "");
        }else{
            this.llenaTblRol(true, this.txtBuscarRol.getText());
        }
    }//GEN-LAST:event_txtBuscarRolKeyReleased

    private void tblRolMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRolMouseClicked
        
        int idx = this.tblRol.getSelectedRow();
        
        this.txtRolID.setText(this.dtmRol.getValueAt(idx, 0).toString());
        this.txtDescripcionRol.setText(this.dtmRol.getValueAt(idx, 1).toString());
        
        this.btnGrabar.setText("Actualizar");
    }//GEN-LAST:event_tblRolMouseClicked

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        Rol rol = new Rol();
        if(this.valida() == true){
            
            if(this.btnGrabar.getText().equals("Grabar")){
            
                rol.setRolID(Integer.parseInt(this.txtRolID.getText()));
                rol.setDescripRol(this.txtDescripcionRol.getText());
                
                this.rolDao.insertaRol(rol);
                this.limpia();
                this.llenaTblRol(false, "");
            }else{
            
                rol.setRolID(Integer.parseInt(this.txtRolID.getText()));
                rol.setDescripRol(this.txtDescripcionRol.getText());
                
                this.rolDao.actualizarRol(rol);
                this.limpia();
                this.llenaTblRol(false, "");
            }
        
        }
        
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
        this.limpia();
        this.llenaTblRol(false, "");
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        Rol rol = new Rol();
        rol.setRolID(Integer.parseInt(this.txtRolID.getText()));
        this.rolDao.eliminarRol(rol);
        this.limpia();
        this.llenaTblRol(false, "");
    }//GEN-LAST:event_btnEliminarActionPerformed

    private boolean valida(){
    
        if (this.txtRolID.getText().isEmpty() && this.txtDescripcionRol.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta llenar campos...");
        }else if(this.txtRolID.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta llenar rol ID...");
        }else if(this.txtDescripcionRol.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta llenar Descripción...");
        }else {
            return true;
        }
        
        return false;
    
    }
    
    private void limpia(){
    
        this.txtDescripcionRol.setText("");
        
        this.btnGrabar.setText("Grabar");
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
