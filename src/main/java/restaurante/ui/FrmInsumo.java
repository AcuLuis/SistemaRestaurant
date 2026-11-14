
package restaurante.ui;

import restaurante.modelo.Insumo;
import restaurante.modelo.TipInsumo;
import restaurante.dao.InsumoDao;
import restaurante.dao.TipInsumoDao;
import restaurante.util.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmInsumo extends javax.swing.JInternalFrame {
    
    TipInsumoDao tipinsu = new TipInsumoDao();
    InsumoDao insuDao = new InsumoDao();
    DefaultTableModel dtmInsumo;
    String tipInsumo;
    Util util = new Util();
    public FrmInsumo() {
        
        initComponents();
        dtmInsumo = (DefaultTableModel) this.tblIns.getModel();
        this.llenaTblIns(false, "");
        this.llenaCmbTipo();
    }
    public void llenaTblIns(boolean sw, String cad){
        
        this.txtInsumoID.setText(Integer.toString(util.idNext("Insumo", "insumoID")));
        Vector<Insumo> listaInsumo;
        listaInsumo = insuDao.ListaItem(sw, cad);
        dtmInsumo.setRowCount(0);
        
        for(int i=0;i<listaInsumo.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaInsumo.get(i).getInsumoID());
            tipInsumo = tipinsu.devTipInsumo(listaInsumo.get(i).getIdTipInsumo());
            vector.addElement(tipInsumo);
            vector.addElement(listaInsumo.get(i).getUnidadMedida());
            vector.addElement(listaInsumo.get(i).getDescripInsumo());
            vector.addElement(listaInsumo.get(i).getPrecioUnit());
            vector.addElement(listaInsumo.get(i).getCantidad());
            dtmInsumo.addRow(vector);
        }
        
    }
    
   public void llenaCmbTipo(){

        Vector<TipInsumo> listaTipInsu = new Vector<TipInsumo>();
        listaTipInsu = this.tipinsu.llenaTipInsumo();
        this.cmbTipIns.addItem("");
        for (int i=0; i<listaTipInsu.size();i++){
            this.cmbTipIns.addItem(listaTipInsu.get(i).getDescripcion());
        }
        
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
        jLabel2.setText("InsumoID: ");

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
                "InsumoID", "TipInsumo", "unidadMedida", "descripInsumo", "precioUnit", "cantidad"
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

        if (valida() == true){
        
                Insumo insumo = new Insumo();

                insumo.setInsumoID(Integer.parseInt(this.txtInsumoID.getText()));
                
                this.insuDao.eliminarInsumo(insumo.getInsumoID());
                this.llenaTblIns(false, "");
                this.limpiar();
        }

    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtUnidadMedActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUnidadMedActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUnidadMedActionPerformed

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        limpiar();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        
        if(this.txtBuscar.getText().isEmpty()){
        
            this.llenaTblIns(false, "");
        }else{
        
            this.llenaTblIns(true, this.txtBuscar.getText());
        }
 
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void tblInsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblInsMouseClicked

        int idx;
        
        this.btnGrabar.setText("Actualizar");
        idx = this.tblIns.getSelectedRow();
        
        this.txtInsumoID.setText(this.dtmInsumo.getValueAt(idx, 0).toString());
        this.cmbTipIns.setSelectedItem(this.dtmInsumo.getValueAt(idx, 1).toString());
        this.txtUnidadMed.setText(this.dtmInsumo.getValueAt(idx, 2).toString());
        this.txtDesc.setText(this.dtmInsumo.getValueAt(idx, 3).toString());
        this.txtPrecU.setText(this.dtmInsumo.getValueAt(idx, 4).toString());
        this.txtCant.setText(this.dtmInsumo.getValueAt(idx, 5).toString());
        
    }//GEN-LAST:event_tblInsMouseClicked

    private void txtDescActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDescActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDescActionPerformed

    private void cmbTipInsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbTipInsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbTipInsActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed

        if(this.valida() == true){
        
            if(this.btnGrabar.getText().equals("Grabar")){
                
                Insumo insumo = new Insumo();

                insumo.setInsumoID(Integer.parseInt(this.txtInsumoID.getText()));
                
                insumo.setIdTipInsumo(this.tipinsu.devIDTipInsumo(this.cmbTipIns.getSelectedItem().toString()));

                insumo.setUnidadMedida(this.txtUnidadMed.getText());
                insumo.setDescripInsumo(this.txtDesc.getText());
                insumo.setPrecioUnit(Double.parseDouble(this.txtPrecU.getText()));
                insumo.setCantidad(Integer.parseInt(this.txtCant.getText()));
                
                this.insuDao.insertaInsumo(insumo);
                this.limpiar();
                
                this.llenaTblIns(false, "");
            }else{
                
                Insumo insumo = new Insumo();

                insumo.setInsumoID(Integer.parseInt(this.txtInsumoID.getText()));
                
                insumo.setIdTipInsumo(this.tipinsu.devIDTipInsumo(this.cmbTipIns.getSelectedItem().toString()));

                insumo.setUnidadMedida(this.txtUnidadMed.getText());
                insumo.setDescripInsumo(this.txtDesc.getText());
                insumo.setPrecioUnit(Double.parseDouble(this.txtPrecU.getText()));
                insumo.setCantidad(Integer.parseInt(this.txtCant.getText()));
                
                this.insuDao.actualizarInsumo(insumo);
                this.limpiar();
                
                this.llenaTblIns(false, "");           
            }
        }
    }//GEN-LAST:event_btnGrabarActionPerformed

    public void limpiar(){
    
        this.cmbTipIns.setSelectedItem("");
        this.txtUnidadMed.setText("");
        this.txtPrecU.setText("");
        this.txtDesc.setText("");
        this.txtCant.setText("");
        this.btnGrabar.setText("Grabar");
    
    }
    
    public boolean valida(){
    
        if(this.cmbTipIns.getSelectedItem().equals("") && this.txtUnidadMed.getText().isEmpty() &&
           this.txtDesc.getText().isEmpty() && this.txtCant.getText().isEmpty() && this.txtPrecU.getText().isEmpty()){
            
            JOptionPane.showMessageDialog(null, "FALTA LLENAR LOS CAMPOS...");
        }else if(this.cmbTipIns.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR TIPO INSUMO...");
        }else if(this.txtUnidadMed.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR UNIDAD DE MEDIDA...");
        }else if(this.txtDesc.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR DESCRIPCION...");
        }else if(this.txtCant.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR CANTIDAD...");
        }else if(this.txtPrecU.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR PRECIO...");
        }else{
            return true;
        }
        
        return false;
    }
    
    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed
     



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
