
package restaurante.ui;


import java.util.List;
import restaurante.modelo.Empleado;
import restaurante.modelo.LocalR;
import restaurante.modelo.Usuario;
import restaurante.dao.EmpleadoDao;
import restaurante.dao.UsuarioDao;
import restaurante.dao.LocalDao;
import restaurante.util.DbBean;
import restaurante.util.Util;
import java.sql.ResultSet;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmUsuario extends javax.swing.JInternalFrame {

    DefaultTableModel dtmUsuario;
    DefaultTableModel dtmEmpleado;
    UsuarioDao uDao = new UsuarioDao();
    LocalDao lDao = new LocalDao();
    EmpleadoDao empDao = new EmpleadoDao();
    int idEmpleado = 0;
    
    public FrmUsuario() {
        initComponents();
        dtmUsuario = (DefaultTableModel) this.tblUsuario.getModel();
        dtmEmpleado = (DefaultTableModel) this.tblEmpleado.getModel();
        this.llenaCmbTipoUsuario();
        this.llenaTblUsuario(false, "");
        this.llenaTblEmpleado(false, "");
        if(this.idEmpleado == 0){
        
            this.panel1.setEnabledAt(1, false);
        }else if(this.idEmpleado != 0){
            this.panel1.setEnabledAt(1, true);
        }
    }
    
    private void llenaTblUsuario(boolean valida, String cadena){
    
        List<Usuario> listaUsuario;
        
        listaUsuario = uDao.listaUsuario(valida, cadena);
        dtmUsuario.setRowCount(0);
        
        for(int i=0;i<listaUsuario.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaUsuario.get(i).getUsuarioID());
            vector.addElement(listaUsuario.get(i).getIdLocalR());
            vector.addElement(listaUsuario.get(i).getUsuario());
            
            if(listaUsuario.get(i).getTipUsuaruario()==1){
                vector.addElement("Administrador");
            }else if(listaUsuario.get(i).getTipUsuaruario()==2){
                vector.addElement("Mantenimiento");
            }else if(listaUsuario.get(i).getTipUsuaruario()==3){
                vector.addElement("Reportes");
            }else{
                vector.addElement("Transacciones");
            }
            vector.addElement(listaUsuario.get(i).getEmpleadoID());
            
            dtmUsuario.addRow(vector);
        }
        
    
    }
   private void llenaTblEmpleado(boolean valida, String cadena){
    
        List<Empleado> listaEmp;
        
        
        listaEmp = empDao.listaEmpleadoNoUsers(valida, cadena);
        dtmEmpleado.setRowCount(0);
        
        for(int i=0;i<listaEmp.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaEmp.get(i).getEmpleadoID());
            vector.addElement(listaEmp.get(i).getNombres());
            vector.addElement(listaEmp.get(i).getApellidos());
            vector.addElement(listaEmp.get(i).getDni());
            
            dtmEmpleado.addRow(vector);
        }
    
    }
    private void llenaCmbTipoUsuario(){
    
        this.cmbTipoUsuario.addItem("");
        this.cmbTipoUsuario.addItem("Administrador");
        this.cmbTipoUsuario.addItem("Mantenimiento");
        this.cmbTipoUsuario.addItem("Reportes");
        this.cmbTipoUsuario.addItem("Transacciones");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtBuscarUsuario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnCerrar = new javax.swing.JButton();
        panel1 = new javax.swing.JTabbedPane();
        panelEmpleado = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblEmpleado = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        txtBuscarEmpleado = new javax.swing.JTextField();
        panelUsuario = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtUsuarioId = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txtIdLocal = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cmbTipoUsuario = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        txtEmpleadoID = new javax.swing.JTextField();
        btnGrabar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        btnVolver = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtPassWord = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuario = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("MANTENIMIENTO USUARIOS");

        txtBuscarUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtBuscarUsuarioActionPerformed(evt);
            }
        });
        txtBuscarUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarUsuarioKeyReleased(evt);
            }
        });

        jLabel7.setText("BUSCAR");

        btnCerrar.setBackground(new java.awt.Color(51, 51, 51));
        btnCerrar.setText("Cerrar");
        btnCerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCerrarActionPerformed(evt);
            }
        });

        tblEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "EmpleadoID", "Nombres", "Apellidos", "Dni"
            }
        ));
        tblEmpleado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmpleadoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblEmpleado);

        jLabel8.setText("Buscar");

        txtBuscarEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmpleadoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelEmpleadoLayout = new javax.swing.GroupLayout(panelEmpleado);
        panelEmpleado.setLayout(panelEmpleadoLayout);
        panelEmpleadoLayout.setHorizontalGroup(
            panelEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEmpleadoLayout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(panelEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEmpleadoLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        panelEmpleadoLayout.setVerticalGroup(
            panelEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEmpleadoLayout.createSequentialGroup()
                .addContainerGap(27, Short.MAX_VALUE)
                .addGroup(panelEmpleadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        panel1.addTab("BuscaEmpleado", panelEmpleado);

        jLabel2.setText("usuarioID");

        txtUsuarioId.setEnabled(false);

        jLabel3.setText("idLocal");

        txtIdLocal.setEnabled(false);

        jLabel4.setText("usuario");

        jLabel5.setText("Tipo Usuario");

        jLabel6.setText("empleadoID");

        txtEmpleadoID.setEnabled(false);

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

        btnVolver.setText("Volver");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        jLabel9.setText("Contraseña");

        javax.swing.GroupLayout panelUsuarioLayout = new javax.swing.GroupLayout(panelUsuario);
        panelUsuario.setLayout(panelUsuarioLayout);
        panelUsuarioLayout.setHorizontalGroup(
            panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuarioLayout.createSequentialGroup()
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelUsuarioLayout.createSequentialGroup()
                        .addGap(46, 46, 46)
                        .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)))
                    .addGroup(panelUsuarioLayout.createSequentialGroup()
                        .addGap(69, 69, 69)
                        .addComponent(btnGrabar)))
                .addGap(37, 37, 37)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtEmpleadoID, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUsuarioLayout.createSequentialGroup()
                        .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel9)
                        .addGap(18, 18, 18)
                        .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtUsuarioId, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(panelUsuarioLayout.createSequentialGroup()
                        .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnEliminar)
                            .addComponent(txtIdLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(47, 47, 47)
                        .addComponent(btnVolver)))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        panelUsuarioLayout.setVerticalGroup(
            panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelUsuarioLayout.createSequentialGroup()
                .addGap(56, 56, 56)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtUsuarioId, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtIdLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(cmbTipoUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtEmpleadoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(panelUsuarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGrabar)
                    .addComponent(btnEliminar)
                    .addComponent(btnVolver))
                .addGap(28, 28, 28))
        );

        panel1.addTab("Usuario", panelUsuario);

        tblUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "UsuarioID", "IdLocal", "Usuario", "TipoUsuario", "EmpleadoID"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuarioMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuario);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(612, 612, 612)
                        .addComponent(jLabel7)
                        .addGap(18, 18, 18)
                        .addComponent(txtBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(414, 414, 414)
                        .addComponent(jLabel1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(49, 49, 49)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 547, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(374, 374, 374)
                                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(16, 16, 16)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(38, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(panel1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(52, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtBuscarUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarUsuarioKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarUsuarioKeyReleased
        
        if(this.txtBuscarUsuario.getText().isEmpty()){
            this.llenaTblUsuario(false, "");
        }else{
            this.llenaTblUsuario(true, this.txtBuscarUsuario.getText());
        }
    }//GEN-LAST:event_txtBuscarUsuarioKeyReleased

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtBuscarEmpleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoKeyReleased
        
        if(this.txtBuscarEmpleado.getText().isEmpty()){
            this.llenaTblEmpleado(false, "");
        }else{
            this.llenaTblEmpleado(true, this.txtBuscarEmpleado.getText());
        }
    }//GEN-LAST:event_txtBuscarEmpleadoKeyReleased

    private void txtBuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarUsuarioActionPerformed
        if(this.txtBuscarUsuario.getText().isEmpty()){
            this.llenaTblUsuario(false, "");
        }else{
            this.llenaTblUsuario(true, this.txtBuscarUsuario.getText());
        }
    }//GEN-LAST:event_txtBuscarUsuarioActionPerformed

    private void tblEmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadoMouseClicked
        
        int idx = this.tblEmpleado.getSelectedRow();
        
        idEmpleado  = (int) this.dtmEmpleado.getValueAt(idx, 0);
        
        this.panel1.setEnabledAt(1, true);
        this.panel1.setSelectedIndex(1);
                List<LocalR> listaLocal;
        listaLocal = lDao.listaLocal();
        int id=0;
        for (int i =0; i<listaLocal.size();i++){
            id = listaLocal.get(i).getIdLocalR();
        }
        
        Util u = new Util();
        this.txtUsuarioId.setText(Integer.toString(u.idNext("Usuario", "usuarioID")));
        this.txtIdLocal.setText(Integer.toString(id));
        this.txtEmpleadoID.setText(Integer.toString(idEmpleado));
        this.panel1.setEnabledAt(0, false);
    }//GEN-LAST:event_tblEmpleadoMouseClicked

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.limpiar();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        if(this.valida()==true){
        
            if(this.btnGrabar.getText().equals("Grabar")){
            
                Usuario user = new Usuario();
                
                user.setUsuarioID(Integer.parseInt(this.txtUsuarioId.getText()));
                user.setIdLocalR(Integer.parseInt(this.txtIdLocal.getText()));
                user.setUsuario(this.txtUsuario.getText());
                user.setContraseña(this.txtPassWord.getText());
                if(this.cmbTipoUsuario.getSelectedItem().equals("Administrador")){
                    user.setTipUsuaruario(1);
                }else if(this.cmbTipoUsuario.getSelectedItem().equals("Mantenimiento")){
                    user.setTipUsuaruario(2);
                }else if(this.cmbTipoUsuario.getSelectedItem().equals("Reportes")){
                    user.setTipUsuaruario(3);
                }else{
                    user.setTipUsuaruario(4);
                }
                user.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));
                
                this.uDao.insertaUsuario(user);
                this.limpiar();
                this.llenaTblUsuario(false, "");
                this.llenaTblEmpleado(false, "");
                
            }else{
                Usuario user = new Usuario();
                
                user.setUsuarioID(Integer.parseInt(this.txtUsuarioId.getText()));
                user.setIdLocalR(Integer.parseInt(this.txtIdLocal.getText()));
                user.setUsuario(this.txtUsuario.getText());
                if(this.cmbTipoUsuario.getSelectedItem().equals("Administrador")){
                    user.setTipUsuaruario(1);
                }else if(this.cmbTipoUsuario.getSelectedItem().equals("Mantenimiento")){
                    user.setTipUsuaruario(2);
                }else if(this.cmbTipoUsuario.getSelectedItem().equals("Reportes")){
                    user.setTipUsuaruario(3);
                }else{
                    user.setTipUsuaruario(4);
                }
                user.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));
                
                this.uDao.actualizarUsuario(user);
                this.llenaTblUsuario(false, "");
                this.limpiar();
                
            }
        
        }
        
        
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuarioMouseClicked
        
        this.btnGrabar.setText("Actualizar");
        this.panel1.setSelectedIndex(1);
        this.panel1.setEnabledAt(1, true);
        int idx = this.tblUsuario.getSelectedRow();
        
        this.txtUsuarioId.setText(this.dtmUsuario.getValueAt(idx,0).toString());
        this.txtIdLocal.setText(this.dtmUsuario.getValueAt(idx, 1).toString());
        this.txtUsuario.setText(this.dtmUsuario.getValueAt(idx, 2).toString());
        this.cmbTipoUsuario.setSelectedItem(this.dtmUsuario.getValueAt(idx, 3).toString());
        this.txtEmpleadoID.setText(this.dtmUsuario.getValueAt(idx, 4).toString());
        this.txtPassWord.setEnabled(false);
        this.panel1.setEnabledAt(0, false);
    }//GEN-LAST:event_tblUsuarioMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        int idx = this.tblUsuario.getSelectedRow();
        Usuario user = new Usuario();
        
        user.setUsuarioID(Integer.parseInt(this.dtmUsuario.getValueAt(idx, 0).toString()));
        
        this.uDao.eliminarUsuario(user);
        this.llenaTblUsuario(false, "");
        this.llenaTblEmpleado(false, "");
        this.limpiar();
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private boolean valida(){
    
        if (this.txtUsuario.getText().isEmpty() && this.cmbTipoUsuario.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR LOS CAMPOS");
            
        }else if(this.txtUsuario.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR USUARIO");
        }else if(this.cmbTipoUsuario.getSelectedItem().equals("")){
        
            JOptionPane.showMessageDialog(null, "FALTA SELECCIONAR TIPO USUARIO");
        }else {
            return true;
        }
        return false;
    }
    
    
    private void limpiar(){
    
        this.txtUsuario.setText("");
        this.cmbTipoUsuario.setSelectedItem("");
        this.panel1.setSelectedIndex(0);
        idEmpleado =0;
        this.panel1.setEnabledAt(1, false);
        this.panel1.setEnabledAt(0, true);
        this.btnGrabar.setText("Grabar");
        this.txtPassWord.setEnabled(true);
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbTipoUsuario;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane panel1;
    private javax.swing.JPanel panelEmpleado;
    private javax.swing.JPanel panelUsuario;
    private javax.swing.JTable tblEmpleado;
    private javax.swing.JTable tblUsuario;
    private javax.swing.JTextField txtBuscarEmpleado;
    private javax.swing.JTextField txtBuscarUsuario;
    private javax.swing.JTextField txtEmpleadoID;
    private javax.swing.JTextField txtIdLocal;
    private javax.swing.JTextField txtPassWord;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtUsuarioId;
    // End of variables declaration//GEN-END:variables
}
