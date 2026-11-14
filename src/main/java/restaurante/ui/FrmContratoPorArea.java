

package restaurante.ui;

import java.util.Map;
import restaurante.reportes.Reportes;
import restaurante.ui.util.Dialogos;


import java.util.List;
import restaurante.modelo.Area;
import restaurante.dao.AreaDao;
import java.util.HashMap;
import java.util.Vector;

public class FrmContratoPorArea extends javax.swing.JInternalFrame {

    AreaDao areaDao = new AreaDao();
    public FrmContratoPorArea() {
        initComponents();
        this.llenaCmbArea();
    }
    
    private void llenaCmbArea(){
        
        List<Area> listaArea;
        listaArea = this.areaDao.llenaArea();
        this.cmbArea.addItem("");
        
        for(int i=0;i<listaArea.size();i++){
        
            this.cmbArea.addItem(listaArea.get(i).getDescripArea());
        }
        
    
    }
    


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        cmbArea = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        btnReporte = new javax.swing.JButton();
        btnCerrar = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("REPORTE CONTRATO POR ÁREA");

        jLabel2.setText("Área");

        btnReporte.setText("Producir Reporte");
        btnReporte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReporteActionPerformed(evt);
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
                .addGap(84, 84, 84)
                .addComponent(jLabel2)
                .addGap(48, 48, 48)
                .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 125, Short.MAX_VALUE)
                .addComponent(btnReporte)
                .addGap(67, 67, 67))
            .addGroup(layout.createSequentialGroup()
                .addGap(109, 109, 109)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(163, 163, 163))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(90, 90, 90)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReporte))
                .addGap(47, 47, 47)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void btnReporteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReporteActionPerformed
        Object area = this.cmbArea.getSelectedItem();
        if (area == null || area.toString().isEmpty()) {
            Dialogos.aviso(this, "Seleccione un área.");
            this.cmbArea.requestFocusInWindow();
            return;
        }
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("descripArea", area.toString());
        Reportes.mostrar(this, "repContratoPorArea", "Contratos del área " + area, parametros);
    }//GEN-LAST:event_btnReporteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnReporte;
    private javax.swing.JComboBox<String> cmbArea;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables

}
