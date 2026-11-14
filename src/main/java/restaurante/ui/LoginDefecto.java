
package restaurante.ui;

import java.awt.Cursor;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import javax.swing.JComponent;
import javax.swing.SwingWorker;
import restaurante.datos.ConexionBD;
import restaurante.datos.InstaladorBD;
import restaurante.modelo.Empleado;
import restaurante.modelo.LocalR;
import restaurante.seguridad.Contrasenas;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Validaciones;

/**
 * Configuración inicial: crea la base de datos con el local y el usuario
 * administrador. Se muestra cuando el servidor responde pero la base de
 * datos todavía no existe.
 */
public class LoginDefecto extends javax.swing.JFrame {

    public LoginDefecto() {
        initComponents();
        this.setTitle("Configuración inicial - Sistema Restaurant");
        this.setLocationRelativeTo(null);
        this.llenaCmbGenero();
        this.llenaCmbEstadoCivil();
        this.txtNACIMIENTO.setToolTipText("Formato " + Validaciones.FORMATO_FECHA + " (por ejemplo, 1990-05-31)");
        this.txtDNI.setToolTipText("8 dígitos");
        this.txtCONTRADEFECTO.setToolTipText("Mínimo " + Contrasenas.LONGITUD_MINIMA
                + " caracteres, combinando letras y números");
        this.getRootPane().setDefaultButton(this.btnGrabar);
    }

    public void llenaCmbGenero(){
        this.cmbGenero.addItem("");
        this.cmbGenero.addItem("MASCULINO");
        this.cmbGenero.addItem("FEMENINO");
    }

    public void llenaCmbEstadoCivil(){
        this.cmbEstadoCivil.addItem("");
        this.cmbEstadoCivil.addItem("SOLTERO");
        this.cmbEstadoCivil.addItem("CASADO");
        this.cmbEstadoCivil.addItem("DIVORCIADO");
    }

    public void crearBD(){
        if (!this.valida()){
            return;
        }
        Empleado empleado = new Empleado();
        empleado.setNombres(this.txtNOMBREUSERDEFECTO.getText().trim());
        empleado.setApellidos(this.txtAPELLIDOS.getText().trim());
        empleado.setFechaNacimiento(this.txtNACIMIENTO.getText().trim());
        // Se conserva la codificación original: 1 = masculino, 0 = femenino.
        empleado.setGenero("MASCULINO".equals(this.cmbGenero.getSelectedItem()) ? 1 : 0);
        empleado.setDni(this.txtDNI.getText().trim());
        empleado.setEstadoCivil(this.cmbEstadoCivil.getSelectedItem().toString());

        LocalR local = new LocalR();
        local.setIdLocalR(Integer.parseInt(this.txtIDLOCALDEFECTO.getText().trim()));
        local.setUbicacion(this.txtUBICACION.getText().trim());
        local.setCapacidadMax(Integer.parseInt(this.txtCAPACIDAD.getText().trim()));
        local.setPisos(Integer.parseInt(this.txtPISOS.getText().trim()));

        String usuario = this.txtNOMBREUSERDEFECTO.getText().trim();
        char[] contrasena = this.txtCONTRADEFECTO.getPassword();
        this.btnGrabar.setEnabled(false);
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() {
                InstaladorBD.crearBaseDatos(empleado, local, usuario, contrasena);
                return null;
            }

            @Override
            protected void done() {
                Arrays.fill(contrasena, '\0');
                setCursor(Cursor.getDefaultCursor());
                btnGrabar.setEnabled(true);
                try {
                    get();
                    Dialogos.info(LoginDefecto.this, "Base de datos \"" + ConexionBD.nombreBaseDatos()
                            + "\" creada correctamente.\n\nInicie sesión con el ID de local "
                            + local.getIdLocalR() + " y el usuario \"" + usuario + "\".");
                    new Login().setVisible(true);
                    dispose();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (java.util.concurrent.ExecutionException e) {
                    Dialogos.error(LoginDefecto.this, e.getCause());
                }
            }
        }.execute();
    }

    /** Comprueba todos los campos y señala el primero incorrecto. */
    public boolean valida(){
        if (vacio(txtIDLOCALDEFECTO, "el ID del local") || vacio(txtNOMBREUSERDEFECTO, "el nombre de usuario")
                || vacio(txtAPELLIDOS, "los apellidos") || vacio(txtNACIMIENTO, "la fecha de nacimiento")
                || vacio(txtDNI, "el DNI") || vacio(txtUBICACION, "la ubicación del local")
                || vacio(txtCAPACIDAD, "la capacidad máxima") || vacio(txtPISOS, "el número de pisos")) {
            return false;
        }
        if (Validaciones.enteroPositivo(txtIDLOCALDEFECTO.getText()).isEmpty()) {
            return invalido(txtIDLOCALDEFECTO, "El ID del local debe ser un número entero mayor que cero.");
        }
        if (txtNOMBREUSERDEFECTO.getText().trim().length() > 25) {
            return invalido(txtNOMBREUSERDEFECTO, "El nombre de usuario admite como máximo 25 caracteres.");
        }
        String errorContrasena = Contrasenas.validarNueva(txtCONTRADEFECTO.getPassword());
        if (errorContrasena != null) {
            return invalido(txtCONTRADEFECTO, errorContrasena);
        }
        Optional<LocalDate> nacimiento = Validaciones.fecha(txtNACIMIENTO.getText());
        if (nacimiento.isEmpty() || !nacimiento.get().isBefore(LocalDate.now().minusYears(14))) {
            return invalido(txtNACIMIENTO, "Indique una fecha de nacimiento válida con el formato "
                    + Validaciones.FORMATO_FECHA + ".");
        }
        if ("".equals(cmbGenero.getSelectedItem())) {
            return invalido(cmbGenero, "Seleccione el género.");
        }
        if (!Validaciones.esDni(txtDNI.getText())) {
            return invalido(txtDNI, "El DNI debe tener 8 dígitos.");
        }
        if ("".equals(cmbEstadoCivil.getSelectedItem())) {
            return invalido(cmbEstadoCivil, "Seleccione el estado civil.");
        }
        if (Validaciones.enteroPositivo(txtCAPACIDAD.getText()).isEmpty()) {
            return invalido(txtCAPACIDAD, "La capacidad máxima debe ser un número entero mayor que cero.");
        }
        if (Validaciones.enteroPositivo(txtPISOS.getText()).isEmpty()) {
            return invalido(txtPISOS, "El número de pisos debe ser un número entero mayor que cero.");
        }
        return true;
    }

    private boolean vacio(javax.swing.JTextField campo, String descripcion) {
        if (Validaciones.vacio(campo.getText())) {
            invalido(campo, "Falta indicar " + descripcion + ".");
            return true;
        }
        return false;
    }

    private boolean invalido(JComponent campo, String mensaje) {
        Dialogos.aviso(this, mensaje);
        campo.requestFocusInWindow();
        return false;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtIDLOCALDEFECTO = new javax.swing.JTextField();
        txtNOMBREUSERDEFECTO = new javax.swing.JTextField();
        txtCONTRADEFECTO = new javax.swing.JPasswordField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtAPELLIDOS = new javax.swing.JTextField();
        txtNACIMIENTO = new javax.swing.JTextField();
        txtDNI = new javax.swing.JTextField();
        txtCAPACIDAD = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        txtPISOS = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        txtUBICACION = new javax.swing.JTextField();
        btnGrabar = new javax.swing.JButton();
        cmbGenero = new javax.swing.JComboBox<>();
        cmbEstadoCivil = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("REGISTRO ADMINISTRADOR");

        jLabel2.setText("NOMBRE USUARIO");

        jLabel3.setText("CONTRASEÑA");

        jLabel4.setText("ID LOCAL");

        txtIDLOCALDEFECTO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDLOCALDEFECTOActionPerformed(evt);
            }
        });

        jLabel6.setText("Apellidos ");

        jLabel7.setText("Fecha de Nacimiento");

        jLabel8.setText("Genero");

        jLabel9.setText("DNI");

        jLabel10.setText("Estado Civil");

        jLabel11.setText("EMPLEADO:");

        jLabel12.setText("LOCAL:");

        jLabel13.setText("Capacidad max");

        jLabel14.setText("Pisos");

        jLabel17.setText("Ubicacion");

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
            .addGroup(layout.createSequentialGroup()
                .addGap(96, 96, 96)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtIDLOCALDEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(95, 95, 95)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(txtCONTRADEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(41, 41, 41)
                                .addComponent(txtNOMBREUSERDEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(2, 2, 2))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(81, 81, 81)
                                    .addComponent(txtAPELLIDOS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel7)
                                        .addComponent(jLabel8))
                                    .addGap(18, 18, 18)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtNACIMIENTO, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                        .addComponent(cmbGenero, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9)
                                    .addComponent(jLabel10))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtDNI, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(cmbEstadoCivil, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(103, 103, 103)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel14)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(txtPISOS, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel13)
                                                    .addComponent(jLabel17))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txtUBICACION, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txtCAPACIDAD, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(63, 63, 63)
                                                .addComponent(jLabel12)))
                                        .addGap(0, 4, Short.MAX_VALUE))))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(134, 134, 134)
                                .addComponent(btnGrabar)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(66, 66, 66)
                        .addComponent(jLabel11)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(93, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(190, 190, 190)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(jLabel1)
                .addGap(39, 39, 39)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtNOMBREUSERDEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIDLOCALDEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtAPELLIDOS, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(txtNACIMIENTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(24, 24, 24)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel8)
                            .addComponent(cmbGenero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(txtDNI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(22, 22, 22)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(cmbEstadoCivil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(txtCONTRADEFECTO, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(62, 62, 62)
                        .addComponent(jLabel11)
                        .addGap(14, 14, 14)
                        .addComponent(jLabel12)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel17)
                            .addComponent(txtUBICACION, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel13)
                            .addComponent(txtCAPACIDAD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(25, 25, 25)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel14)
                            .addComponent(txtPISOS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(46, 46, 46)
                        .addComponent(btnGrabar)
                        .addGap(72, 72, 72))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDLOCALDEFECTOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDLOCALDEFECTOActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDLOCALDEFECTOActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        this.crearBD();
    }//GEN-LAST:event_btnGrabarActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginDefecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginDefecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginDefecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginDefecto.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginDefecto().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnGrabar;
    private javax.swing.JComboBox<String> cmbEstadoCivil;
    private javax.swing.JComboBox<String> cmbGenero;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField txtAPELLIDOS;
    private javax.swing.JTextField txtCAPACIDAD;
    private javax.swing.JPasswordField txtCONTRADEFECTO;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtIDLOCALDEFECTO;
    private javax.swing.JTextField txtNACIMIENTO;
    private javax.swing.JTextField txtNOMBREUSERDEFECTO;
    private javax.swing.JTextField txtPISOS;
    private javax.swing.JTextField txtUBICACION;
    // End of variables declaration//GEN-END:variables
}
