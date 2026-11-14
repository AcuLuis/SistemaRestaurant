
package restaurante.ui;

import restaurante.ui.util.Dialogos;

import java.awt.Cursor;
import java.util.concurrent.ExecutionException;
import javax.swing.SwingWorker;
import restaurante.config.Configuracion;
import restaurante.datos.ConexionBD;
import restaurante.datos.MigradorBD;

public class Entrada extends javax.swing.JFrame {

    public Entrada() {
        initComponents();
        this.setTitle("Sistema Restaurant");
        this.setLocationRelativeTo(null);
        this.getRootPane().setDefaultButton(this.btnEntra);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        btnEntra = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/restaurant.jpg"))); // NOI18N

        btnEntra.setText("ENTRAR AL SISTEMA");
        btnEntra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEntraActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1)
            .addGroup(layout.createSequentialGroup()
                .addGap(157, 157, 157)
                .addComponent(btnEntra, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnEntra, javax.swing.GroupLayout.DEFAULT_SIZE, 51, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnEntraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEntraActionPerformed
        
        this.validaConexion();
    }//GEN-LAST:event_btnEntraActionPerformed
    /**
     * Comprueba la conexión en segundo plano (puede tardar varios segundos si
     * el servidor no responde) y continúa según el resultado.
     */
    public void validaConexion(){
        this.btnEntra.setEnabled(false);
        this.btnEntra.setText("CONECTANDO...");
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        new SwingWorker<ConexionBD.Estado, Void>() {
            @Override
            protected ConexionBD.Estado doInBackground() {
                ConexionBD.Estado estado = ConexionBD.verificar();
                if (estado == ConexionBD.Estado.DISPONIBLE) {
                    MigradorBD.actualizar();
                }
                return estado;
            }

            @Override
            protected void done() {
                setCursor(Cursor.getDefaultCursor());
                btnEntra.setEnabled(true);
                btnEntra.setText("ENTRAR AL SISTEMA");
                try {
                    continuar(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (ExecutionException e) {
                    Dialogos.error(Entrada.this, e.getCause());
                }
            }
        }.execute();
    }

    private void continuar(ConexionBD.Estado estado) {
        switch (estado) {
            case DISPONIBLE:
                new Login().setVisible(true);
                this.dispose();
                break;
            case SIN_BASE_DATOS:
                if (Dialogos.confirmar(this, "El servidor de base de datos está disponible, pero no existe la base de datos \""
                        + ConexionBD.nombreBaseDatos() + "\".\n\n¿Desea crearla ahora con un usuario administrador?")) {
                    new LoginDefecto().setVisible(true);
                    this.dispose();
                }
                break;
            default:
                String archivo = Configuracion.get().origen().map(Object::toString)
                        .orElse("no encontrado (copie config/app.properties.example como config/app.properties)");
                Dialogos.error(this, "No se pudo conectar con SQL Server.\n\n"
                        + "Compruebe que el servidor está en marcha y que los datos de conexión son correctos.\n"
                        + "Archivo de configuración: " + archivo + "\n\nDetalle: " + ConexionBD.ultimoError());
                break;
        }
    }

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
            java.util.logging.Logger.getLogger(Entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Entrada.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        Dialogos.instalarManejadorGlobal();

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Entrada().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnEntra;
    private javax.swing.JLabel jLabel1;
    // End of variables declaration//GEN-END:variables
}
