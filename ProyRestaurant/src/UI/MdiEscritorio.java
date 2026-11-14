
package UI;

import BEAN.LocalR;
import DAO.UsuarioDao;
import DAO.inciaBdDao;
import DAO.localDao;
import UTIL.DbBean;
import UTIL.Util;
import UTIL.creaBD;
import static com.itextpdf.text.pdf.PdfName.URI;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;

public class MdiEscritorio extends javax.swing.JFrame {
    
    UsuarioDao us = new UsuarioDao();
    FrmContrato fContrato;
    FrmContratoPorFechas frmContratoFechas;
    FrmContratoPorArea frmPorArea;
    FrmArea frmArea;
    FrmRol frmRol;
    FrmProducto frmProdu;
    FrmContratoPorRol frmPorRol;
    frmRepProdRango frmProdRango;
    FrmInsumo frmInsumo;
    FrmInsumoPorPrecioUnitario frmInsuPrecio;
    Compartir compartir;
    FrmVenta frmVenta;
    OrdenCompra frmOrdenCompra;
    FrmUsuario frmUser;
    localDao lDao = new localDao();
    
    
    
    public MdiEscritorio(int n) {
        //admin
        initComponents();
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension dim = toolkit.getScreenSize();
        
        
        if (n == 1){
            
            JOptionPane.showMessageDialog(null, "MODO ADMINISTRAR " );
        
        }else if(n == 2){
        
            JOptionPane.showMessageDialog(null, "MODO MANTENIMIENTO " );
            this.mnuAdmin.setVisible(false);
            this.mnuReportes.setVisible(false);
            this.reportesPorParametros.setVisible(false);
            this.menuTransaccion.setVisible(false);
            this.TabCruzadas.setVisible(false);
        
        }else if (n ==3){
            JOptionPane.showMessageDialog(null, "MODO REPORTES " );
            this.mnuAdmin.setVisible(false);
            this.menuTransaccion.setVisible(false);
            this.mnuMantenimiento.setVisible(false);
        }else{
            JOptionPane.showMessageDialog(null, "MODO TRANSACCION " );
            this.mnuAdmin.setVisible(false);
            this.mnuMantenimiento.setVisible(false);
            this.mnuReportes.setVisible(false);
            this.reportesPorParametros.setVisible(false);
            this.TabCruzadas.setVisible(false);
        }
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }



    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        desktopPane = new javax.swing.JDesktopPane();
        menuBar = new javax.swing.JMenuBar();
        mnuAdmin = new javax.swing.JMenu();
        menuItemAgregaUsuarios = new javax.swing.JMenuItem();
        menuItemBDPorDefecto = new javax.swing.JMenuItem();
        mnuMantenimiento = new javax.swing.JMenu();
        menuItemContrato = new javax.swing.JMenuItem();
        menuItemRol = new javax.swing.JMenuItem();
        menuItemArea = new javax.swing.JMenuItem();
        menuItemProducto = new javax.swing.JMenuItem();
        menuItemInsumo = new javax.swing.JMenuItem();
        mnuReportes = new javax.swing.JMenu();
        menuItemReporteContrato = new javax.swing.JMenuItem();
        menuItemReporteSimpleArea = new javax.swing.JMenuItem();
        menuItemSimpleRol = new javax.swing.JMenuItem();
        menuItemSimpleProducto = new javax.swing.JMenuItem();
        menuItemReporteInsumo = new javax.swing.JMenuItem();
        reportesPorParametros = new javax.swing.JMenu();
        menuItemContratoPorFechas = new javax.swing.JMenuItem();
        menuItemPorArea = new javax.swing.JMenuItem();
        menuItemContratoPorRol = new javax.swing.JMenuItem();
        menuItemProductoPorPrecio = new javax.swing.JMenuItem();
        menuItemInsumoPorPrecio = new javax.swing.JMenuItem();
        TabCruzadas = new javax.swing.JMenu();
        ProductoTipPlatoCruzado = new javax.swing.JMenuItem();
        ContratoÁreaCruzado = new javax.swing.JMenuItem();
        TipoUsuarioUbicacionCruzado = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        menuItemGmail = new javax.swing.JMenuItem();
        Redes = new javax.swing.JMenu();
        facebook = new javax.swing.JMenuItem();
        instagram = new javax.swing.JMenuItem();
        gmail = new javax.swing.JMenuItem();
        menuTransaccion = new javax.swing.JMenu();
        menuItemVenta = new javax.swing.JMenuItem();
        menuItemOrdenDeVenta = new javax.swing.JMenuItem();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        desktopPane.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0), new java.awt.Color(0, 0, 0)));

        mnuAdmin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnuAdmin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/admin.jpg"))); // NOI18N
        mnuAdmin.setText("Administrador     |");

        menuItemAgregaUsuarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/añadirUsuario.jpg"))); // NOI18N
        menuItemAgregaUsuarios.setText("Agregar Usuarios");
        menuItemAgregaUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAgregaUsuariosActionPerformed(evt);
            }
        });
        mnuAdmin.add(menuItemAgregaUsuarios);

        menuItemBDPorDefecto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/bd.jpg"))); // NOI18N
        menuItemBDPorDefecto.setText("Completar BD Por Defecto");
        menuItemBDPorDefecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBDPorDefectoActionPerformed(evt);
            }
        });
        mnuAdmin.add(menuItemBDPorDefecto);

        menuBar.add(mnuAdmin);

        mnuMantenimiento.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnuMantenimiento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/mantenimiento.jpg"))); // NOI18N
        mnuMantenimiento.setText("Mantenimiento     |");
        mnuMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMantenimientoActionPerformed(evt);
            }
        });

        menuItemContrato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/contrato.jpg"))); // NOI18N
        menuItemContrato.setText("Contrato");
        menuItemContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemContrato);

        menuItemRol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/rol.jpg"))); // NOI18N
        menuItemRol.setText("Rol");
        menuItemRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRolActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemRol);

        menuItemArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/workspace-workspace-line-black-icon-vector.jpg"))); // NOI18N
        menuItemArea.setText("Área");
        menuItemArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAreaActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemArea);

        menuItemProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/istockphoto-1282219840-612x612.jpg"))); // NOI18N
        menuItemProducto.setText("Producto");
        menuItemProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemProductoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemProducto);

        menuItemInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Captura de pantalla 2023-07-08 005643.jpg"))); // NOI18N
        menuItemInsumo.setText("Insumo");
        menuItemInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInsumoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemInsumo);

        menuBar.add(mnuMantenimiento);

        mnuReportes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnuReportes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/reportes.jpg"))); // NOI18N
        mnuReportes.setText("Reportes     |");

        menuItemReporteContrato.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/contrato.jpg"))); // NOI18N
        menuItemReporteContrato.setText("ReporteContrato");
        menuItemReporteContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteContratoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteContrato);

        menuItemReporteSimpleArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/workspace-workspace-line-black-icon-vector.jpg"))); // NOI18N
        menuItemReporteSimpleArea.setText("ReporteÁrea");
        menuItemReporteSimpleArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteSimpleAreaActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteSimpleArea);

        menuItemSimpleRol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/rol.jpg"))); // NOI18N
        menuItemSimpleRol.setText("ReporteRol");
        menuItemSimpleRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSimpleRolActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemSimpleRol);

        menuItemSimpleProducto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/istockphoto-1282219840-612x612.jpg"))); // NOI18N
        menuItemSimpleProducto.setText("ReporteProducto");
        menuItemSimpleProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSimpleProductoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemSimpleProducto);

        menuItemReporteInsumo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Captura de pantalla 2023-07-08 005643.jpg"))); // NOI18N
        menuItemReporteInsumo.setText("ReporteInsumo");
        menuItemReporteInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteInsumoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteInsumo);

        menuBar.add(mnuReportes);

        reportesPorParametros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        reportesPorParametros.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/repParametros.jpg"))); // NOI18N
        reportesPorParametros.setText("Reporte con Parametros     |");
        reportesPorParametros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportesPorParametrosActionPerformed(evt);
            }
        });

        menuItemContratoPorFechas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/tiempo.jpg"))); // NOI18N
        menuItemContratoPorFechas.setText("Contrato por Fechas");
        menuItemContratoPorFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoPorFechasActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemContratoPorFechas);

        menuItemPorArea.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/workspace-workspace-line-black-icon-vector.jpg"))); // NOI18N
        menuItemPorArea.setText("Contrato por Área");
        menuItemPorArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPorAreaActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemPorArea);

        menuItemContratoPorRol.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/rol.jpg"))); // NOI18N
        menuItemContratoPorRol.setText("Contrato por Rol");
        menuItemContratoPorRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoPorRolActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemContratoPorRol);

        menuItemProductoPorPrecio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/istockphoto-1282219840-612x612.jpg"))); // NOI18N
        menuItemProductoPorPrecio.setText("Producto por Precio");
        menuItemProductoPorPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemProductoPorPrecioActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemProductoPorPrecio);

        menuItemInsumoPorPrecio.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Captura de pantalla 2023-07-08 005643.jpg"))); // NOI18N
        menuItemInsumoPorPrecio.setText("Insumo por Precio");
        menuItemInsumoPorPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInsumoPorPrecioActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemInsumoPorPrecio);

        menuBar.add(reportesPorParametros);

        TabCruzadas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TabCruzadas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/tablacruzada.jpg"))); // NOI18N
        TabCruzadas.setText("Tablas Cruzadas     |");

        ProductoTipPlatoCruzado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/96200338-plato-vacío-de-dibujos-animados.jpg"))); // NOI18N
        ProductoTipPlatoCruzado.setText("Producto - Tipo de Plato");
        ProductoTipPlatoCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductoTipPlatoCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(ProductoTipPlatoCruzado);

        ContratoÁreaCruzado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/contrato.jpg"))); // NOI18N
        ContratoÁreaCruzado.setText("Contratos - Área");
        ContratoÁreaCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContratoÁreaCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(ContratoÁreaCruzado);

        TipoUsuarioUbicacionCruzado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/descarga.jpg"))); // NOI18N
        TipoUsuarioUbicacionCruzado.setText("TipoUsuario - Ubicación");
        TipoUsuarioUbicacionCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoUsuarioUbicacionCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(TipoUsuarioUbicacionCruzado);

        menuBar.add(TabCruzadas);

        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/correo.jpg"))); // NOI18N
        jMenu1.setText("Correo General     |");

        menuItemGmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Gmail_icon_(2020).svg (1).jpg"))); // NOI18N
        menuItemGmail.setText("Gmail");
        menuItemGmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemGmailActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemGmail);

        menuBar.add(jMenu1);

        Redes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/redes.jpg"))); // NOI18N
        Redes.setText("Redes     |");

        facebook.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Facebook_f_logo_(2019).jpg"))); // NOI18N
        facebook.setText("FACEBOOK");
        facebook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facebookActionPerformed(evt);
            }
        });
        Redes.add(facebook);

        instagram.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Instagram_logo_2016.svg.jpg"))); // NOI18N
        instagram.setText("INSTAGRAM");
        instagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instagramActionPerformed(evt);
            }
        });
        Redes.add(instagram);

        gmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/Logo_of_Twitter.svg.jpg"))); // NOI18N
        gmail.setText(" TWITTER");
        gmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gmailActionPerformed(evt);
            }
        });
        Redes.add(gmail);

        menuBar.add(Redes);

        menuTransaccion.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/transaccion.jpg"))); // NOI18N
        menuTransaccion.setText("Transacciones    Y Control |");

        menuItemVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/venta.jpg"))); // NOI18N
        menuItemVenta.setText("Venta");
        menuItemVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemVentaActionPerformed(evt);
            }
        });
        menuTransaccion.add(menuItemVenta);

        menuItemOrdenDeVenta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/IMAGENES/inventario (1).jpg"))); // NOI18N
        menuItemOrdenDeVenta.setText("Control De inventario");
        menuItemOrdenDeVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemOrdenDeVentaActionPerformed(evt);
            }
        });
        menuTransaccion.add(menuItemOrdenDeVenta);

        menuBar.add(menuTransaccion);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.DEFAULT_SIZE, 878, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(desktopPane, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 510, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuItemContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemContratoActionPerformed
        
        fContrato = new FrmContrato();
        this.desktopPane.add(fContrato);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = fContrato.getSize();
        fContrato.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        fContrato.setVisible(true);
                
    }//GEN-LAST:event_menuItemContratoActionPerformed

    private void menuItemContratoPorFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemContratoPorFechasActionPerformed
        
        frmContratoFechas = new FrmContratoPorFechas();
        this.desktopPane.add(frmContratoFechas);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmContratoFechas.getSize();
        frmContratoFechas.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmContratoFechas.setVisible(true);
        
    }//GEN-LAST:event_menuItemContratoPorFechasActionPerformed

    private void menuItemAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAreaActionPerformed
        frmArea = new FrmArea();
        this.desktopPane.add(frmArea);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmArea.getSize();
        frmArea.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmArea.setVisible(true); 
    }//GEN-LAST:event_menuItemAreaActionPerformed

    private void menuItemRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRolActionPerformed
        
        frmRol = new FrmRol();
        this.desktopPane.add(frmRol);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmRol.getSize();
        frmRol.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmRol.setVisible(true);        
    }//GEN-LAST:event_menuItemRolActionPerformed

    private void menuItemReporteContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteContratoActionPerformed

        try{
            String r = "src/REPORTES/repContratoSimple.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }

    }//GEN-LAST:event_menuItemReporteContratoActionPerformed

    private void menuItemPorAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemPorAreaActionPerformed
        frmPorArea = new FrmContratoPorArea();
        this.desktopPane.add(frmPorArea);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmPorArea.getSize();
        frmPorArea.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmPorArea.setVisible(true);
    }//GEN-LAST:event_menuItemPorAreaActionPerformed

    private void menuItemProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemProductoActionPerformed
        frmProdu = new FrmProducto();
        this.desktopPane.add(frmProdu);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmProdu.getSize();
        frmProdu.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmProdu.setVisible(true); 
        
    }//GEN-LAST:event_menuItemProductoActionPerformed

    private void mnuMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMantenimientoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuMantenimientoActionPerformed

    private void reportesPorParametrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportesPorParametrosActionPerformed

    }//GEN-LAST:event_reportesPorParametrosActionPerformed

    private void menuItemContratoPorRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemContratoPorRolActionPerformed
                frmPorRol = new FrmContratoPorRol();
        this.desktopPane.add(frmPorRol);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmPorRol.getSize();
        frmPorRol.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmPorRol.setVisible(true);
    }//GEN-LAST:event_menuItemContratoPorRolActionPerformed

    private void menuItemReporteSimpleAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteSimpleAreaActionPerformed
        try{
            String r = "src/REPORTES/repAreaSimple.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_menuItemReporteSimpleAreaActionPerformed

    private void menuItemSimpleRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSimpleRolActionPerformed
        try{
            String r = "src/REPORTES/repRolSimple.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_menuItemSimpleRolActionPerformed

    private void menuItemSimpleProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSimpleProductoActionPerformed
        
        try{
            String r = "src/REPORTES/reportProdSimp.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_menuItemSimpleProductoActionPerformed

    private void menuItemProductoPorPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemProductoPorPrecioActionPerformed
        frmProdRango = new frmRepProdRango();
        this.desktopPane.add(frmProdRango);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmProdRango.getSize();
        frmProdRango.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmProdRango.setVisible(true);
        
    }//GEN-LAST:event_menuItemProductoPorPrecioActionPerformed

    private void ProductoTipPlatoCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductoTipPlatoCruzadoActionPerformed
        
        try{
            String r = "src/REPORTES/reporTblCruzProd.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_ProductoTipPlatoCruzadoActionPerformed

    private void ContratoÁreaCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContratoÁreaCruzadoActionPerformed
        
        try{
            String r = "src/REPORTES/reportContratoTblCruz.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }
    }//GEN-LAST:event_ContratoÁreaCruzadoActionPerformed

    private void TipoUsuarioUbicacionCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoUsuarioUbicacionCruzadoActionPerformed
        
        try{
            String r = "src/REPORTES/reportTblCruzUsuarios.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        }  
    }//GEN-LAST:event_TipoUsuarioUbicacionCruzadoActionPerformed

    private void menuItemInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInsumoActionPerformed
        frmInsumo = new FrmInsumo();
        this.desktopPane.add(frmInsumo);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmInsumo.getSize();
        frmInsumo.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmInsumo.setVisible(true); 
    }//GEN-LAST:event_menuItemInsumoActionPerformed

    private void menuItemReporteInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteInsumoActionPerformed
        
        try{
            String r = "src/REPORTES/repInsSimp.jasper";
            DbBean db = new DbBean();
            db.connectRep(r, null, false);
        }catch(SQLException ex){
            ex.printStackTrace();
        }catch(JRException ex){
            ex.printStackTrace();
        } 
    }//GEN-LAST:event_menuItemReporteInsumoActionPerformed

    private void menuItemInsumoPorPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInsumoPorPrecioActionPerformed
        frmInsuPrecio = new FrmInsumoPorPrecioUnitario();
        this.desktopPane.add(frmInsuPrecio);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmInsuPrecio.getSize();
        frmInsuPrecio.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmInsuPrecio.setVisible(true);
    }//GEN-LAST:event_menuItemInsumoPorPrecioActionPerformed

    private void menuItemGmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemGmailActionPerformed
        compartir = new Compartir();
        this.desktopPane.add(compartir);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = compartir.getSize();
        compartir.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        compartir.setVisible(true);
    }//GEN-LAST:event_menuItemGmailActionPerformed

    private void facebookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facebookActionPerformed
        if(Desktop.isDesktopSupported()){
            Desktop desk = Desktop.getDesktop();
            if(desk.isSupported(Desktop.Action.BROWSE)){
                try {
                    desk.browse(new URI("https://www.facebook.com/groups/1237922966896488"));
                } catch (URISyntaxException ex) {
                    Logger.getLogger(MdiEscritorio.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MdiEscritorio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        }
    }//GEN-LAST:event_facebookActionPerformed

    private void instagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instagramActionPerformed
        if(Desktop.isDesktopSupported()){
            Desktop desk = Desktop.getDesktop();
            if(desk.isSupported(Desktop.Action.BROWSE)){
                try{
                    desk.browse(new URI("https://www.instagram.com/restaurantepoo/"));
                }catch(IOException e){
                    e.printStackTrace();
                }catch(URISyntaxException e){

                }
            }

        }
    }//GEN-LAST:event_instagramActionPerformed

    private void gmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gmailActionPerformed
        if(Desktop.isDesktopSupported()){
            Desktop desk = Desktop.getDesktop();
            if(desk.isSupported(Desktop.Action.BROWSE)){
                try{
                    desk.browse(new URI("https://twitter.com/POORestaurante_"));
                }catch(IOException e){
                    e.printStackTrace();
                }catch(URISyntaxException e){

                }
            }

        }
    }//GEN-LAST:event_gmailActionPerformed

    private void menuItemVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemVentaActionPerformed
        
        frmVenta = new FrmVenta();
        frmVenta.setVisible(true);
    }//GEN-LAST:event_menuItemVentaActionPerformed

    private void menuItemBDPorDefectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemBDPorDefectoActionPerformed
        
        inciaBdDao ini = new inciaBdDao();
        Vector<LocalR> listaLocal = new Vector<LocalR>();
        listaLocal = lDao.listaLocal();
        int id=0;
        for (int i =0; i<listaLocal.size();i++){
            id = listaLocal.get(i).getIdLocalR();
        }
        
        ini.completaBD(id);
        
        JOptionPane.showMessageDialog(null, "BD CORRECTAMENTE COMPLETADA...");
        
    }//GEN-LAST:event_menuItemBDPorDefectoActionPerformed

    private void menuItemOrdenDeVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOrdenDeVentaActionPerformed
        frmOrdenCompra = new OrdenCompra();
        this.desktopPane.add(frmOrdenCompra);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmOrdenCompra.getSize();
        frmOrdenCompra.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmOrdenCompra.setVisible(true);
    }//GEN-LAST:event_menuItemOrdenDeVentaActionPerformed

    private void menuItemAgregaUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAgregaUsuariosActionPerformed
        frmUser = new FrmUsuario();
        this.desktopPane.add(frmUser);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension ventana = frmUser.getSize();
        frmUser.setLocation((pantalla.width-ventana.width)/2, (pantalla.height-ventana.height)/2);
        frmUser.setVisible(true);
        
    }//GEN-LAST:event_menuItemAgregaUsuariosActionPerformed
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ContratoÁreaCruzado;
    private javax.swing.JMenuItem ProductoTipPlatoCruzado;
    private javax.swing.JMenu Redes;
    private javax.swing.JMenu TabCruzadas;
    private javax.swing.JMenuItem TipoUsuarioUbicacionCruzado;
    private javax.swing.JDesktopPane desktopPane;
    private javax.swing.JMenuItem facebook;
    private javax.swing.JMenuItem gmail;
    private javax.swing.JMenuItem instagram;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem menuItemAgregaUsuarios;
    private javax.swing.JMenuItem menuItemArea;
    private javax.swing.JMenuItem menuItemBDPorDefecto;
    private javax.swing.JMenuItem menuItemContrato;
    private javax.swing.JMenuItem menuItemContratoPorFechas;
    private javax.swing.JMenuItem menuItemContratoPorRol;
    private javax.swing.JMenuItem menuItemGmail;
    private javax.swing.JMenuItem menuItemInsumo;
    private javax.swing.JMenuItem menuItemInsumoPorPrecio;
    private javax.swing.JMenuItem menuItemOrdenDeVenta;
    private javax.swing.JMenuItem menuItemPorArea;
    private javax.swing.JMenuItem menuItemProducto;
    private javax.swing.JMenuItem menuItemProductoPorPrecio;
    private javax.swing.JMenuItem menuItemReporteContrato;
    private javax.swing.JMenuItem menuItemReporteInsumo;
    private javax.swing.JMenuItem menuItemReporteSimpleArea;
    private javax.swing.JMenuItem menuItemRol;
    private javax.swing.JMenuItem menuItemSimpleProducto;
    private javax.swing.JMenuItem menuItemSimpleRol;
    private javax.swing.JMenuItem menuItemVenta;
    private javax.swing.JMenu menuTransaccion;
    private javax.swing.JMenu mnuAdmin;
    private javax.swing.JMenu mnuMantenimiento;
    private javax.swing.JMenu mnuReportes;
    private javax.swing.JMenu reportesPorParametros;
    // End of variables declaration//GEN-END:variables

}
