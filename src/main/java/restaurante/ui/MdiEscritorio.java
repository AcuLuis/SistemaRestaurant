package restaurante.ui;

import java.awt.Desktop;
import java.awt.Window;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.WindowConstants;
import restaurante.config.Configuracion;
import restaurante.config.Configuracion.Claves;
import restaurante.datos.InstaladorBD;
import restaurante.reportes.Reportes;
import restaurante.seguridad.Sesion;
import restaurante.seguridad.TipoUsuario;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Estilo;
import restaurante.ui.util.Iconos;
import restaurante.ui.mantenimiento.FrmCliente;
import restaurante.ui.mantenimiento.FrmEmpleado;
import restaurante.ui.mantenimiento.FrmMesa;
import restaurante.ui.produccion.FrmProduccion;
import restaurante.ui.produccion.FrmReceta;
import restaurante.ui.transacciones.FrmReserva;

/**
 * Ventana principal. Muestra los menús según el perfil del usuario y abre
 * cada formulario una sola vez dentro del escritorio.
 */
public class MdiEscritorio extends javax.swing.JFrame {

    private final Map<Class<?>, JInternalFrame> abiertos = new HashMap<>();
    private FrmVenta frmVenta;

    public MdiEscritorio() {
        initComponents();
        Sesion sesion = Sesion.actual().orElseThrow(() -> new IllegalStateException("No hay una sesión iniciada"));
        TipoUsuario tipo = sesion.getTipo();
        this.setTitle("Sistema Restaurant - " + sesion.getUsuario() + " (" + tipo.getDescripcion() + ") - Local "
                + sesion.getIdLocal());
        this.mnuAdmin.setVisible(tipo.puedeAdministrar());
        this.mnuMantenimiento.setVisible(tipo.puedeMantener());
        this.mnuReportes.setVisible(tipo.puedeVerReportes());
        this.reportesPorParametros.setVisible(tipo.puedeVerReportes());
        this.TabCruzadas.setVisible(tipo.puedeVerReportes());
        this.menuTransaccion.setVisible(tipo.puedeRegistrarTransacciones());
        configurarRedes();
        agregarMenusNuevos();
        agregarMenuSesion();
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salir();
            }
        });
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    /** Abre el formulario o, si ya está abierto, lo trae al frente. */
    private void abrir(Class<? extends JInternalFrame> tipo, Supplier<? extends JInternalFrame> fabrica) {
        JInternalFrame existente = abiertos.get(tipo);
        if (existente != null && existente.isDisplayable() && !existente.isClosed()) {
            existente.toFront();
            seleccionar(existente);
            return;
        }
        JInternalFrame ventana = fabrica.get();
        Estilo.ajustarContraste(ventana);
        this.desktopPane.add(ventana);
        int x = Math.max(0, (this.desktopPane.getWidth() - ventana.getWidth()) / 2);
        int y = Math.max(0, (this.desktopPane.getHeight() - ventana.getHeight()) / 2);
        ventana.setLocation(x, y);
        ventana.setVisible(true);
        seleccionar(ventana);
        abiertos.put(tipo, ventana);
    }

    private static void seleccionar(JInternalFrame ventana) {
        try {
            ventana.setSelected(true);
        } catch (PropertyVetoException e) {
            // La ventana rechazó la selección: no es un error.
        }
    }

    /** Opciones añadidas a los menús del diseño original. */
    private void agregarMenusNuevos() {
        this.mnuMantenimiento.addSeparator();
        this.mnuMantenimiento.add(opcion("Empleados", "empleado", () -> abrir(FrmEmpleado.class, FrmEmpleado::new)));
        this.mnuMantenimiento.add(opcion("Clientes", "cliente", () -> abrir(FrmCliente.class, FrmCliente::new)));
        this.mnuMantenimiento.add(opcion("Mesas", "mesa", () -> abrir(FrmMesa.class, FrmMesa::new)));
        this.mnuMantenimiento.add(opcion("Recetas", "receta", () -> abrir(FrmReceta.class, FrmReceta::new)));
        this.menuTransaccion.addSeparator();
        this.menuTransaccion.add(opcion("Reservas", "reserva", () -> abrir(FrmReserva.class, FrmReserva::new)));
        this.menuTransaccion.add(opcion("Órdenes de producción", "produccion",
                () -> abrir(FrmProduccion.class, FrmProduccion::new)));
    }

    private static JMenuItem opcion(String texto, String icono, Runnable accion) {
        JMenuItem item = new JMenuItem(texto, Iconos.icono(icono));
        item.addActionListener(e -> accion.run());
        return item;
    }

    private void configurarRedes() {
        boolean alguna = configurarEnlace(this.facebook, Claves.REDES_FACEBOOK, "Facebook");
        alguna |= configurarEnlace(this.instagram, Claves.REDES_INSTAGRAM, "Instagram");
        alguna |= configurarEnlace(this.gmail, Claves.REDES_TWITTER, "X (Twitter)");
        this.Redes.setVisible(alguna);
    }

    private static boolean configurarEnlace(JMenuItem item, String clave, String texto) {
        item.setText(texto);
        boolean definido = Configuracion.get().definido(clave);
        item.setVisible(definido);
        return definido;
    }

    private void abrirEnlace(String clave) {
        String url = Configuracion.get().texto(clave, "");
        if (url.isEmpty()) {
            return;
        }
        try {
            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                Dialogos.info(this, "Abra este enlace en su navegador:\n" + url);
                return;
            }
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            Dialogos.error(this, "No se pudo abrir el enlace " + url + ": " + e.getMessage());
        }
    }

    private void agregarMenuSesion() {
        JMenu menuSesion = new JMenu("Sesión");
        menuSesion.setMnemonic(KeyEvent.VK_S);
        JMenuItem cerrarSesion = new JMenuItem("Cerrar sesión");
        cerrarSesion.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        cerrarSesion.addActionListener(e -> cerrarSesion());
        JMenuItem salir = new JMenuItem("Salir");
        salir.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK));
        salir.addActionListener(e -> salir());
        menuSesion.add(cerrarSesion);
        menuSesion.addSeparator();
        menuSesion.add(salir);
        this.menuBar.add(menuSesion);
    }

    private void cerrarSesion() {
        if (!Dialogos.confirmar(this, "¿Desea cerrar la sesión?")) {
            return;
        }
        for (Window ventana : Window.getWindows()) {
            if (ventana != this) {
                ventana.dispose();
            }
        }
        Sesion.cerrar();
        new Login().setVisible(true);
        this.dispose();
    }

    private void salir() {
        if (Dialogos.confirmar(this, "¿Desea salir de Sistema Restaurant?")) {
            System.exit(0);
        }
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
        mnuAdmin.setIcon(restaurante.ui.util.Iconos.icono("administracion")); // NOI18N
        mnuAdmin.setText("Administrador");

        menuItemAgregaUsuarios.setIcon(restaurante.ui.util.Iconos.icono("usuarios")); // NOI18N
        menuItemAgregaUsuarios.setText("Usuarios");
        menuItemAgregaUsuarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAgregaUsuariosActionPerformed(evt);
            }
        });
        mnuAdmin.add(menuItemAgregaUsuarios);

        menuItemBDPorDefecto.setIcon(restaurante.ui.util.Iconos.icono("datos")); // NOI18N
        menuItemBDPorDefecto.setText("Cargar datos de ejemplo");
        menuItemBDPorDefecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemBDPorDefectoActionPerformed(evt);
            }
        });
        mnuAdmin.add(menuItemBDPorDefecto);

        menuBar.add(mnuAdmin);

        mnuMantenimiento.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnuMantenimiento.setIcon(restaurante.ui.util.Iconos.icono("mantenimiento")); // NOI18N
        mnuMantenimiento.setText("Mantenimiento");
        mnuMantenimiento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMantenimientoActionPerformed(evt);
            }
        });

        menuItemContrato.setIcon(restaurante.ui.util.Iconos.icono("contrato")); // NOI18N
        menuItemContrato.setText("Contrato");
        menuItemContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemContrato);

        menuItemRol.setIcon(restaurante.ui.util.Iconos.icono("rol")); // NOI18N
        menuItemRol.setText("Rol");
        menuItemRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemRolActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemRol);

        menuItemArea.setIcon(restaurante.ui.util.Iconos.icono("area")); // NOI18N
        menuItemArea.setText("Área");
        menuItemArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemAreaActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemArea);

        menuItemProducto.setIcon(restaurante.ui.util.Iconos.icono("producto")); // NOI18N
        menuItemProducto.setText("Producto");
        menuItemProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemProductoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemProducto);

        menuItemInsumo.setIcon(restaurante.ui.util.Iconos.icono("insumo")); // NOI18N
        menuItemInsumo.setText("Insumo");
        menuItemInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInsumoActionPerformed(evt);
            }
        });
        mnuMantenimiento.add(menuItemInsumo);

        menuBar.add(mnuMantenimiento);

        mnuReportes.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        mnuReportes.setIcon(restaurante.ui.util.Iconos.icono("reportes")); // NOI18N
        mnuReportes.setText("Reportes");

        menuItemReporteContrato.setIcon(restaurante.ui.util.Iconos.icono("contrato")); // NOI18N
        menuItemReporteContrato.setText("Contratos");
        menuItemReporteContrato.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteContratoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteContrato);

        menuItemReporteSimpleArea.setIcon(restaurante.ui.util.Iconos.icono("area")); // NOI18N
        menuItemReporteSimpleArea.setText("Áreas");
        menuItemReporteSimpleArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteSimpleAreaActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteSimpleArea);

        menuItemSimpleRol.setIcon(restaurante.ui.util.Iconos.icono("rol")); // NOI18N
        menuItemSimpleRol.setText("Roles");
        menuItemSimpleRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSimpleRolActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemSimpleRol);

        menuItemSimpleProducto.setIcon(restaurante.ui.util.Iconos.icono("producto")); // NOI18N
        menuItemSimpleProducto.setText("Productos");
        menuItemSimpleProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemSimpleProductoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemSimpleProducto);

        menuItemReporteInsumo.setIcon(restaurante.ui.util.Iconos.icono("insumo")); // NOI18N
        menuItemReporteInsumo.setText("Insumos");
        menuItemReporteInsumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemReporteInsumoActionPerformed(evt);
            }
        });
        mnuReportes.add(menuItemReporteInsumo);

        menuBar.add(mnuReportes);

        reportesPorParametros.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        reportesPorParametros.setIcon(restaurante.ui.util.Iconos.icono("parametros")); // NOI18N
        reportesPorParametros.setText("Reportes con parámetros");
        reportesPorParametros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportesPorParametrosActionPerformed(evt);
            }
        });

        menuItemContratoPorFechas.setIcon(restaurante.ui.util.Iconos.icono("calendario")); // NOI18N
        menuItemContratoPorFechas.setText("Contratos por fechas");
        menuItemContratoPorFechas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoPorFechasActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemContratoPorFechas);

        menuItemPorArea.setIcon(restaurante.ui.util.Iconos.icono("area")); // NOI18N
        menuItemPorArea.setText("Contratos por área");
        menuItemPorArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemPorAreaActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemPorArea);

        menuItemContratoPorRol.setIcon(restaurante.ui.util.Iconos.icono("rol")); // NOI18N
        menuItemContratoPorRol.setText("Contratos por rol");
        menuItemContratoPorRol.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemContratoPorRolActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemContratoPorRol);

        menuItemProductoPorPrecio.setIcon(restaurante.ui.util.Iconos.icono("producto")); // NOI18N
        menuItemProductoPorPrecio.setText("Productos por precio");
        menuItemProductoPorPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemProductoPorPrecioActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemProductoPorPrecio);

        menuItemInsumoPorPrecio.setIcon(restaurante.ui.util.Iconos.icono("insumo")); // NOI18N
        menuItemInsumoPorPrecio.setText("Insumos por precio");
        menuItemInsumoPorPrecio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemInsumoPorPrecioActionPerformed(evt);
            }
        });
        reportesPorParametros.add(menuItemInsumoPorPrecio);

        menuBar.add(reportesPorParametros);

        TabCruzadas.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        TabCruzadas.setIcon(restaurante.ui.util.Iconos.icono("tabla")); // NOI18N
        TabCruzadas.setText("Tablas cruzadas");

        ProductoTipPlatoCruzado.setIcon(restaurante.ui.util.Iconos.icono("plato")); // NOI18N
        ProductoTipPlatoCruzado.setText("Productos por tipo de plato");
        ProductoTipPlatoCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProductoTipPlatoCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(ProductoTipPlatoCruzado);

        ContratoÁreaCruzado.setIcon(restaurante.ui.util.Iconos.icono("contrato")); // NOI18N
        ContratoÁreaCruzado.setText("Contratos por área");
        ContratoÁreaCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ContratoÁreaCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(ContratoÁreaCruzado);

        TipoUsuarioUbicacionCruzado.setIcon(restaurante.ui.util.Iconos.icono("ubicacion")); // NOI18N
        TipoUsuarioUbicacionCruzado.setText("Usuarios por tipo y ubicación");
        TipoUsuarioUbicacionCruzado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoUsuarioUbicacionCruzadoActionPerformed(evt);
            }
        });
        TabCruzadas.add(TipoUsuarioUbicacionCruzado);

        menuBar.add(TabCruzadas);

        jMenu1.setIcon(restaurante.ui.util.Iconos.icono("correo")); // NOI18N
        jMenu1.setText("Correo");

        menuItemGmail.setIcon(restaurante.ui.util.Iconos.icono("enviar")); // NOI18N
        menuItemGmail.setText("Enviar correo...");
        menuItemGmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemGmailActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemGmail);

        menuBar.add(jMenu1);

        Redes.setIcon(restaurante.ui.util.Iconos.icono("redes")); // NOI18N
        Redes.setText("Redes");

        facebook.setIcon(restaurante.ui.util.Iconos.icono("enlace")); // NOI18N
        facebook.setText("FACEBOOK");
        facebook.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                facebookActionPerformed(evt);
            }
        });
        Redes.add(facebook);

        instagram.setIcon(restaurante.ui.util.Iconos.icono("enlace")); // NOI18N
        instagram.setText("INSTAGRAM");
        instagram.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                instagramActionPerformed(evt);
            }
        });
        Redes.add(instagram);

        gmail.setIcon(restaurante.ui.util.Iconos.icono("enlace")); // NOI18N
        gmail.setText(" TWITTER");
        gmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gmailActionPerformed(evt);
            }
        });
        Redes.add(gmail);

        menuBar.add(Redes);

        menuTransaccion.setIcon(restaurante.ui.util.Iconos.icono("transacciones")); // NOI18N
        menuTransaccion.setText("Transacciones");

        menuItemVenta.setIcon(restaurante.ui.util.Iconos.icono("venta")); // NOI18N
        menuItemVenta.setText("Venta");
        menuItemVenta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemVentaActionPerformed(evt);
            }
        });
        menuTransaccion.add(menuItemVenta);

        menuItemOrdenDeVenta.setIcon(restaurante.ui.util.Iconos.icono("inventario")); // NOI18N
        menuItemOrdenDeVenta.setText("Control de inventario");
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
        abrir(FrmContrato.class, FrmContrato::new);
    }//GEN-LAST:event_menuItemContratoActionPerformed

    private void menuItemContratoPorFechasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemContratoPorFechasActionPerformed
        abrir(FrmContratoPorFechas.class, FrmContratoPorFechas::new);
    }//GEN-LAST:event_menuItemContratoPorFechasActionPerformed

    private void menuItemAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAreaActionPerformed
        abrir(FrmArea.class, FrmArea::new);
    }//GEN-LAST:event_menuItemAreaActionPerformed

    private void menuItemRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemRolActionPerformed
        abrir(FrmRol.class, FrmRol::new);
    }//GEN-LAST:event_menuItemRolActionPerformed

    private void menuItemPorAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemPorAreaActionPerformed
        abrir(FrmContratoPorArea.class, FrmContratoPorArea::new);
    }//GEN-LAST:event_menuItemPorAreaActionPerformed

    private void menuItemProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemProductoActionPerformed
        abrir(FrmProducto.class, FrmProducto::new);
    }//GEN-LAST:event_menuItemProductoActionPerformed

    private void menuItemContratoPorRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemContratoPorRolActionPerformed
        abrir(FrmContratoPorRol.class, FrmContratoPorRol::new);
    }//GEN-LAST:event_menuItemContratoPorRolActionPerformed

    private void menuItemProductoPorPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemProductoPorPrecioActionPerformed
        abrir(FrmProductoPorPrecio.class, FrmProductoPorPrecio::new);
    }//GEN-LAST:event_menuItemProductoPorPrecioActionPerformed

    private void menuItemInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInsumoActionPerformed
        abrir(FrmInsumo.class, FrmInsumo::new);
    }//GEN-LAST:event_menuItemInsumoActionPerformed

    private void menuItemInsumoPorPrecioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemInsumoPorPrecioActionPerformed
        abrir(FrmInsumoPorPrecio.class, FrmInsumoPorPrecio::new);
    }//GEN-LAST:event_menuItemInsumoPorPrecioActionPerformed

    private void menuItemOrdenDeVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemOrdenDeVentaActionPerformed
        abrir(FrmInventario.class, FrmInventario::new);
    }//GEN-LAST:event_menuItemOrdenDeVentaActionPerformed

    private void menuItemAgregaUsuariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemAgregaUsuariosActionPerformed
        abrir(FrmUsuario.class, FrmUsuario::new);
    }//GEN-LAST:event_menuItemAgregaUsuariosActionPerformed

    private void menuItemReporteContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteContratoActionPerformed
        Reportes.mostrar(this, "repContratoSimple", "Contratos");
    }//GEN-LAST:event_menuItemReporteContratoActionPerformed

    private void menuItemReporteSimpleAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteSimpleAreaActionPerformed
        Reportes.mostrar(this, "repAreaSimple", "Áreas");
    }//GEN-LAST:event_menuItemReporteSimpleAreaActionPerformed

    private void menuItemSimpleRolActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSimpleRolActionPerformed
        Reportes.mostrar(this, "repRolSimple", "Roles");
    }//GEN-LAST:event_menuItemSimpleRolActionPerformed

    private void menuItemSimpleProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemSimpleProductoActionPerformed
        Reportes.mostrar(this, "reportProdSimp", "Productos");
    }//GEN-LAST:event_menuItemSimpleProductoActionPerformed

    private void ProductoTipPlatoCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProductoTipPlatoCruzadoActionPerformed
        Reportes.mostrar(this, "reporTblCruzProd", "Productos por tipo y plato");
    }//GEN-LAST:event_ProductoTipPlatoCruzadoActionPerformed

    private void ContratoÁreaCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ContratoÁreaCruzadoActionPerformed
        Reportes.mostrar(this, "reportContratoTblCruz", "Contratos por área");
    }//GEN-LAST:event_ContratoÁreaCruzadoActionPerformed

    private void TipoUsuarioUbicacionCruzadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoUsuarioUbicacionCruzadoActionPerformed
        Reportes.mostrar(this, "reportTblCruzUsuarios", "Usuarios por tipo y ubicación");
    }//GEN-LAST:event_TipoUsuarioUbicacionCruzadoActionPerformed

    private void menuItemReporteInsumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemReporteInsumoActionPerformed
        Reportes.mostrar(this, "repInsSimp", "Insumos");
    }//GEN-LAST:event_menuItemReporteInsumoActionPerformed

    private void mnuMantenimientoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMantenimientoActionPerformed
    }//GEN-LAST:event_mnuMantenimientoActionPerformed

    private void reportesPorParametrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportesPorParametrosActionPerformed
    }//GEN-LAST:event_reportesPorParametrosActionPerformed

    private void menuItemGmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemGmailActionPerformed
        EnviarCorreoDialog.abrir(this, "", List.of());
    }//GEN-LAST:event_menuItemGmailActionPerformed

    private void facebookActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_facebookActionPerformed
        abrirEnlace(Claves.REDES_FACEBOOK);
    }//GEN-LAST:event_facebookActionPerformed

    private void instagramActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_instagramActionPerformed
        abrirEnlace(Claves.REDES_INSTAGRAM);
    }//GEN-LAST:event_instagramActionPerformed

    private void gmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gmailActionPerformed
        abrirEnlace(Claves.REDES_TWITTER);
    }//GEN-LAST:event_gmailActionPerformed

    private void menuItemVentaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemVentaActionPerformed
        if (frmVenta == null || !frmVenta.isDisplayable()) {
            frmVenta = new FrmVenta();
        }
        frmVenta.setVisible(true);
        frmVenta.toFront();
    }//GEN-LAST:event_menuItemVentaActionPerformed

    private void menuItemBDPorDefectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemBDPorDefectoActionPerformed
        if (!Dialogos.confirmar(this, "Se cargarán datos ficticios de prueba (empleados, productos, clientes,\n"
                + "contratos y usuarios de demostración con la contraseña Demo2024).\n\n"
                + "Solo se permite en una base de datos recién creada. ¿Desea continuar?")) {
            return;
        }
        int idLocal = Sesion.actual().map(Sesion::getIdLocal).orElseThrow();
        InstaladorBD.cargarDatosEjemplo(idLocal);
        Dialogos.info(this, "Datos de ejemplo cargados correctamente.");
    }//GEN-LAST:event_menuItemBDPorDefectoActionPerformed

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
