package restaurante.ui;


import java.util.List;
import restaurante.modelo.*;
import restaurante.dao.*;
import restaurante.util.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmProducto extends javax.swing.JInternalFrame {
    PlatoDao platDao;
    ProductoDao prodDao;
    TipProductoDao tipProbDao;
    DefaultTableModel dtm;
    private int idProducto;
    
    public FrmProducto() {
        platDao = new PlatoDao();
        prodDao = new ProductoDao();
        tipProbDao = new TipProductoDao();
        initComponents();
        dtm = (DefaultTableModel)this.tblProducto.getModel();
        llenaTblProducto(false,"");
        llenaCmbPlato();
        llenaCmbTipo(); 
        
    }
    
    private void llenaCmbTipo(){
        List<TipProducto> listaTip;
        listaTip = tipProbDao.listaTip();
        cmbTipProduct.addItem("");
        for(int i=0;i<listaTip.size();i++){
            this.cmbTipProduct.addItem(listaTip.get(i).getDescripcion());
        }
    }
    private void llenaCmbPlato(){
        List<Plato> listaPlato;
        listaPlato = platDao.listaPlato();
        cmbPlato.addItem("");
        for(int i=0;i<listaPlato.size();i++){
            this.cmbPlato.addItem(listaPlato.get(i).getDescPlat());
        }
    }
    private void llenaTblProducto(boolean valida, String Cadena){
        
        List<Producto> listProduct;
        
        listProduct = prodDao.ListaProducto(valida, Cadena);
        dtm.setRowCount(0);
        
        for(int i=0; i<listProduct.size();i++){
            Vector v = new Vector();
            v.addElement(listProduct.get(i).getProductoID());
            v.addElement(listProduct.get(i).getDescripProducto());
            v.addElement(listProduct.get(i).getCantidad());
            v.addElement(listProduct.get(i).getPrecioUnit());
            v.addElement(listProduct.get(i).getPlato());
            v.addElement(listProduct.get(i).getTipo());
            dtm.addRow(v);
        }
        

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

        jLabel1.setText("Producto ID");

        jLabel2.setText("Descripcion");

        jLabel3.setText("Cantidad");

        jLabel4.setText("Precio Unitario");

        jLabel5.setText("Plato");

        jLabel6.setText("Tipo de Producto");

        txtProductoID.setEditable(false);

        btnLimpia.setText("Limpia");
        btnLimpia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiaActionPerformed(evt);
            }
        });

        tblProducto.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Producto ID", "Descripcion", "Cantidad", "Precio Unitario", "Plato", "Tipo de Producto"
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
        limpiar();
    }//GEN-LAST:event_btnLimpiaActionPerformed

    private void txtBuscarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarKeyReleased
        if(txtBuscar.getText().isEmpty()){
            llenaTblProducto(false,"");
        }else{
            llenaTblProducto(true,this.txtBuscar.getText());
        } 
    }//GEN-LAST:event_txtBuscarKeyReleased

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        if(this.verificaCamposLlenos()== true){
        Producto prod = new Producto();
        Util util = new Util();

        if(this.btnGrabar.getText().equals("Grabar")){
        idProducto = util.idNext("producto","productoID");
        
        prod.setProductoID(idProducto);
        
        if (this.cmbPlato.getSelectedItem().equals("entrada")){
        
            prod.setPlato(30);
        }else if(this.cmbPlato.getSelectedItem().equals("menú")){
        
            prod.setPlato(31);
        }else if(this.cmbPlato.getSelectedItem().equals("sopa")){
        
            prod.setPlato(32);
        }
        
        if(this.cmbTipProduct.getSelectedItem().equals("envasado")){
            prod.setTipo(40);
        }else{
            prod.setTipo(41);
        }
        
        prod.setDescripProducto(this.txtDescripcion.getText());
        prod.setCantidad(Integer.parseInt(this.txtCantidad.getText()));
        prod.setPrecioUnit(Double.parseDouble(this.txtPrecioUnit.getText()));
        

        this.prodDao.insertaProducto(prod);
        }else{
            
            prod.setProductoID(Integer.parseInt(this.txtProductoID.getText()));
                   if (this.cmbPlato.getSelectedItem().equals("entrada")){
        
            prod.setPlato(30);
        }else if(this.cmbPlato.getSelectedItem().equals("menú")){
        
            prod.setPlato(31);
        }else if(this.cmbPlato.getSelectedItem().equals("sopa")){
        
            prod.setPlato(32);
        }
        
        if(this.cmbTipProduct.getSelectedItem().equals("envasado")){
            prod.setTipo(40);
        }else{
            prod.setTipo(41);
        }
        
        prod.setDescripProducto(this.txtDescripcion.getText());
        prod.setCantidad(Integer.parseInt(this.txtCantidad.getText()));
        prod.setPrecioUnit(Double.parseDouble(this.txtPrecioUnit.getText()));
        
           
           this.prodDao.actualizarProducto(prod);
           this.btnGrabar.setText("Grabar");
        }
        this.llenaTblProducto(false,"");
        this.limpiar();
        }
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblProductoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProductoMouseClicked
     int idx;
     idx = this.tblProducto.getSelectedRow();
     this.idProducto = Integer.parseInt(dtm.getValueAt(idx, 0).toString());
     this.txtProductoID.setText(dtm.getValueAt(idx, 0).toString());
     this.txtDescripcion.setText(dtm.getValueAt(idx, 1).toString());
     this.txtCantidad.setText(dtm.getValueAt(idx, 2).toString());
     this.txtPrecioUnit.setText(dtm.getValueAt(idx, 3).toString());
     /*this.cmbPlato.setSelectedItem(tblProducto.getValueAt(idx, 4).toString());
     this.cmbTipProduct.setSelectedItem(tblProducto.getValueAt(idx, 5).toString());
     */
     if(dtm.getValueAt(idx, 4).equals(10)){
         this.cmbPlato.setSelectedItem("Fondo");
     }else if (dtm.getValueAt(idx, 4).equals(11)){
         this.cmbPlato.setSelectedItem("Entrada");
     }else{
         this.cmbPlato.setSelectedItem("Postre");
     }
     
     if(dtm.getValueAt(idx, 5).equals(1)){
         this.cmbTipProduct.setSelectedItem("perecible");
     }else{
         this.cmbTipProduct.setSelectedItem("No perecible");
     }

     this.btnGrabar.setText("Actualizar");
    }//GEN-LAST:event_tblProductoMouseClicked
    
    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
              if (this.verificaCamposLlenos() == true){
            Producto prod = new Producto();
        
            prod.setProductoID(this.idProducto);
            this.prodDao.eliminarProducto(prod);
       }
       this.llenaTblProducto(false, "");
       this.limpiar();
       this.btnGrabar.setText("GRABAR"); 
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void txtBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtBuscarActionPerformed
    
    private boolean verificaCamposLlenos(){
        boolean valida = false;
        if(this.txtDescripcion.getText().isEmpty() && this.txtCantidad.getText().isEmpty()
           && this.txtPrecioUnit.getText().isEmpty() && this.cmbPlato.getSelectedItem().toString().isEmpty()
           && this.cmbTipProduct.getSelectedItem().toString().isEmpty()){
        JOptionPane.showMessageDialog(this, "FALTA LLENAR TODOS LOS CAMPOS...");
        }else{
            if(this.txtDescripcion.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "FALTA LLENAR LA DESCRIPCION DEL PRODUCTO...");
            }else if(this.txtCantidad.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "FALTA LLENAR LA CANTIDAD...");
            }else if(this.txtPrecioUnit.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "FALTA LLENAR EL PRECIO UNITARIO...");
            }
            else if(this.cmbPlato.getSelectedItem().toString().isEmpty()){
            JOptionPane.showMessageDialog(this, "FALTA LLENAR EL TIPO DE PLATO...");
            }
            else if(this.cmbTipProduct.getSelectedItem().toString().isEmpty()){
            JOptionPane.showMessageDialog(this, "FALTA LLENAR EL TIPO DE PRODUCTO...");
            }else{
                valida = true;
            }
        }
        return valida;
    } 
    
    private void limpiar(){
        this.txtProductoID.setText("");
        this.txtDescripcion.setText("");
        this.txtCantidad.setText("");
        this.txtPrecioUnit.setText("");
        this.cmbPlato.setSelectedItem("");
        this.cmbTipProduct.setSelectedItem("");
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
