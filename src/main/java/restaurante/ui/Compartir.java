package restaurante.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class Compartir extends javax.swing.JInternalFrame {

    private static String emailFrom = "";
    private static String passwordFrom = "";
    private String paraEmail;
    private String asunto;
    private String contenido;

    private Properties mProperties;
    private Session mSesion;
    private MimeMessage mCorreo;

    private List<File> mArchivoAdjuntos;
    private String nombresArchivos = "";

    public Compartir() {
        initComponents();
        mProperties = new Properties();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        mArchivoAdjuntos = new ArrayList<>();
    }

    private void crearEmail() throws AddressException, MessagingException {

        paraEmail = this.txtIngreseGmail.getText().trim();
        asunto = this.txtAsunto.getText().trim();
        contenido = this.txtAComentarios.getText().trim();

        mProperties.put("mail.smtp.host", "smtp.gmail.com");
        mProperties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        mProperties.setProperty("mail.smtp.starttls.enable", "true");
        mProperties.setProperty("mail.smtp.port", "587");
        mProperties.setProperty("mail.smtp.user", emailFrom);
        mProperties.setProperty("mail.smtp.ssl.protocols", "TLSv1.2");
        mProperties.setProperty("mail.smtp.auth", "true");

        mSesion = Session.getDefaultInstance(mProperties);

        MimeMultipart mElementosCorreo = new MimeMultipart();

        MimeBodyPart mContenido = new MimeBodyPart();
        mContenido.setContent(contenido, "text/html; charset=utf-8");
        mElementosCorreo.addBodyPart(mContenido);

        MimeBodyPart mAdjuntos;
        for (File archivo : mArchivoAdjuntos) {
            mAdjuntos = new MimeBodyPart();
            mAdjuntos.setDataHandler(new DataHandler(new FileDataSource(archivo.getAbsolutePath())));
            mAdjuntos.setFileName(archivo.getName());
            mElementosCorreo.addBodyPart(mAdjuntos);
        }

        mCorreo = new MimeMessage(mSesion);
        mCorreo.setFrom(new InternetAddress(emailFrom));
        mCorreo.setRecipient(Message.RecipientType.TO, new InternetAddress(paraEmail));
        mCorreo.setSubject(asunto);
        mCorreo.setContent(mElementosCorreo);
    }

    private void enviarEmail() throws NoSuchProviderException, MessagingException {

        Transport mTransport = mSesion.getTransport("smtp");
        mTransport.connect(emailFrom, passwordFrom);
        mTransport.sendMessage(mCorreo, mCorreo.getRecipients(Message.RecipientType.TO));
        mTransport.close();

        JOptionPane.showMessageDialog(this, "Correo Enviado...");
        lblArchi.setText("");
        nombresArchivos = "";

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAComentarios = new javax.swing.JTextArea();
        jLabel3 = new javax.swing.JLabel();
        txtIngreseGmail = new javax.swing.JTextField();
        btnCompartir = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtAsunto = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        lblArchi = new javax.swing.JLabel();
        btnAgregarArchivo = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("COMPARTIR ");

        jLabel2.setText("Ingrese Gmail: ");

        txtAComentarios.setColumns(20);
        txtAComentarios.setRows(5);
        jScrollPane1.setViewportView(txtAComentarios);

        jLabel3.setText("Comentarios: ");

        btnCompartir.setText("Compartir");
        btnCompartir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCompartirActionPerformed(evt);
            }
        });

        btnSalir.setBackground(new java.awt.Color(51, 51, 51));
        btnSalir.setText("Salir");
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jLabel4.setText("Asunto: ");

        jLabel5.setText("Archivos Adjuntos: ");

        lblArchi.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        lblArchi.setOpaque(true);

        btnAgregarArchivo.setText("Agregar Archivos Adjuntos");
        btnAgregarArchivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAgregarArchivoActionPerformed(evt);
            }
        });

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5)
                            .addComponent(btnEliminar))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblArchi, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 94, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnCompartir)
                                .addGap(40, 40, 40)
                                .addComponent(btnAgregarArchivo))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel4)
                                        .addComponent(jLabel3)))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtIngreseGmail, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, 223, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())))
            .addGroup(layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jLabel1)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addGap(43, 43, 43)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIngreseGmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtAsunto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblArchi, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(56, 56, 56)
                        .addComponent(btnEliminar)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCompartir)
                            .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(13, 13, 13))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnAgregarArchivo)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        
        this.dispose();
    }//GEN-LAST:event_btnSalirActionPerformed

    private void btnCompartirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCompartirActionPerformed
        
        try {
            crearEmail();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(Compartir.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            enviarEmail();
        } catch (MessagingException ex) {
            java.util.logging.Logger.getLogger(Compartir.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnCompartirActionPerformed

    private void btnAgregarArchivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAgregarArchivoActionPerformed
        
        JFileChooser elejir = new JFileChooser();

        elejir.setMultiSelectionEnabled(true);
        elejir.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (elejir.showOpenDialog(this) != JFileChooser.CANCEL_OPTION) {
            File[] archivosSeleccionados = elejir.getSelectedFiles();
            for (File archivo : archivosSeleccionados) {
                mArchivoAdjuntos.add(archivo);
                nombresArchivos += archivo.getName() + "<br>";
            }
            lblArchi.setText("<html><p>" + nombresArchivos + "</p></html>");
        }
        
    }//GEN-LAST:event_btnAgregarArchivoActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
                                           

        if (!mArchivoAdjuntos.isEmpty()) {
            mArchivoAdjuntos.remove(mArchivoAdjuntos.size() - 1);
            nombresArchivos = "";

            for (File archivo : mArchivoAdjuntos) {
                nombresArchivos += archivo.getName() + "<br>";
            }

            lblArchi.setText("<html><p>" + nombresArchivos + "</p></html>");
        } else {

            JOptionPane.showMessageDialog(null, "No se puede eliminar...");
        }
    }//GEN-LAST:event_btnEliminarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregarArchivo;
    private javax.swing.JButton btnCompartir;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnSalir;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblArchi;
    private javax.swing.JTextArea txtAComentarios;
    private javax.swing.JTextField txtAsunto;
    private javax.swing.JTextField txtIngreseGmail;
    // End of variables declaration//GEN-END:variables
}
