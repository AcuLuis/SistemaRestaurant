package restaurante.ui;

import java.util.Arrays;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.EmpleadoDao;
import restaurante.dao.UsuarioDao;
import restaurante.modelo.Empleado;
import restaurante.modelo.Usuario;
import restaurante.seguridad.Contrasenas;
import restaurante.seguridad.Sesion;
import restaurante.seguridad.TipoUsuario;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;

/**
 * Gestión de usuarios del sistema. Cada usuario está asociado a un empleado.
 */
public class FrmUsuario extends javax.swing.JInternalFrame {

    private static final int PESTANA_EMPLEADOS = 0;
    private static final int PESTANA_USUARIO = 1;
    private static final int LONGITUD_MAXIMA_USUARIO = 25;

    private final UsuarioDao uDao = new UsuarioDao();
    private final EmpleadoDao empDao = new EmpleadoDao();
    private final BusquedaDiferida busquedaUsuarios;
    private final BusquedaDiferida busquedaEmpleados;
    private List<Usuario> usuarios = List.of();
    private List<Empleado> empleados = List.of();
    private Usuario seleccionado;

    public FrmUsuario() {
        initComponents();
        this.cmbTipoUsuario.addItem("");
        for (TipoUsuario tipo : TipoUsuario.values()) {
            this.cmbTipoUsuario.addItem(tipo.getDescripcion());
        }
        Tablas.configurar(this.tblUsuario);
        Tablas.configurar(this.tblEmpleado);
        Tablas.alSeleccionar(this.tblEmpleado, this::nuevoUsuario);
        Tablas.alSeleccionar(this.tblUsuario, this::cargarUsuario);
        this.txtUsuarioId.setEditable(false);
        this.txtIdLocal.setEditable(false);
        this.txtEmpleadoID.setEditable(false);
        this.busquedaUsuarios = new BusquedaDiferida(this.txtBuscarUsuario, this::llenaTblUsuario);
        this.busquedaEmpleados = new BusquedaDiferida(this.txtBuscarEmpleado, this::llenaTblEmpleado);
        this.llenaTblUsuario("");
        this.llenaTblEmpleado("");
        this.limpiar();
    }

    private void llenaTblUsuario(String texto) {
        usuarios = uDao.listaUsuario(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblUsuario);
        for (Usuario u : usuarios) {
            modelo.addRow(new Object[] {u.getUsuarioID(), u.getIdLocalR(), u.getUsuario(),
                TipoUsuario.deCodigo(u.getTipUsuaruario()).getDescripcion(), u.getEmpleadoID()});
        }
    }

    private void llenaTblEmpleado(String texto) {
        empleados = empDao.listaEmpleadoNoUsers(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblEmpleado);
        for (Empleado e : empleados) {
            modelo.addRow(new Object[] {e.getEmpleadoID(), e.getNombres(), e.getApellidos(), e.getDni()});
        }
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
        txtPassWord = new javax.swing.JPasswordField();
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
                "Empleado", "Nombres", "Apellidos", "DNI"
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

        panel1.addTab("Empleados sin usuario", panelEmpleado);

        jLabel2.setText("Código");

        txtUsuarioId.setEnabled(false);

        jLabel3.setText("Local");

        txtIdLocal.setEnabled(false);

        jLabel4.setText("Usuario");

        jLabel5.setText("Tipo Usuario");

        jLabel6.setText("Empleado");

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
                "Código", "Local", "Usuario", "Tipo de usuario", "Empleado"
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
        busquedaUsuarios.reiniciar();
    }//GEN-LAST:event_txtBuscarUsuarioKeyReleased

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    private void txtBuscarEmpleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoKeyReleased
        busquedaEmpleados.reiniciar();
    }//GEN-LAST:event_txtBuscarEmpleadoKeyReleased

    private void txtBuscarUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtBuscarUsuarioActionPerformed
        this.llenaTblUsuario(this.txtBuscarUsuario.getText().trim());
    }//GEN-LAST:event_txtBuscarUsuarioActionPerformed

    private void tblEmpleadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmpleadoMouseClicked
        this.nuevoUsuario();
    }//GEN-LAST:event_tblEmpleadoMouseClicked

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        this.limpiar();
    }//GEN-LAST:event_btnVolverActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuarioMouseClicked
        this.cargarUsuario();
    }//GEN-LAST:event_tblUsuarioMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    /** Prepara el alta de un usuario para el empleado elegido, en el local de la sesión actual. */
    private void nuevoUsuario() {
        int fila = Tablas.filaSeleccionada(this.tblEmpleado);
        if (fila < 0) {
            return;
        }
        seleccionado = null;
        this.tblUsuario.clearSelection();
        this.txtUsuarioId.setText(String.valueOf(uDao.siguienteId()));
        this.txtIdLocal.setText(String.valueOf(Sesion.actual().map(Sesion::getIdLocal).orElse(0)));
        this.txtEmpleadoID.setText(String.valueOf(empleados.get(fila).getEmpleadoID()));
        this.txtUsuario.setText("");
        this.txtPassWord.setText("");
        this.txtPassWord.setToolTipText("Mínimo " + Contrasenas.LONGITUD_MINIMA + " caracteres, con letras y números");
        this.cmbTipoUsuario.setSelectedIndex(0);
        this.btnGrabar.setText("Grabar");
        abrirPestanaUsuario();
    }

    private void cargarUsuario() {
        int fila = Tablas.filaSeleccionada(this.tblUsuario);
        if (fila < 0) {
            return;
        }
        seleccionado = usuarios.get(fila);
        this.txtUsuarioId.setText(String.valueOf(seleccionado.getUsuarioID()));
        this.txtIdLocal.setText(String.valueOf(seleccionado.getIdLocalR()));
        this.txtUsuario.setText(seleccionado.getUsuario());
        this.cmbTipoUsuario.setSelectedItem(TipoUsuario.deCodigo(seleccionado.getTipUsuaruario()).getDescripcion());
        this.txtEmpleadoID.setText(String.valueOf(seleccionado.getEmpleadoID()));
        this.txtPassWord.setText("");
        this.txtPassWord.setToolTipText("Déjela vacía para conservar la contraseña actual");
        this.btnGrabar.setText("Actualizar");
        abrirPestanaUsuario();
    }

    private void abrirPestanaUsuario() {
        this.panel1.setEnabledAt(PESTANA_USUARIO, true);
        this.panel1.setSelectedIndex(PESTANA_USUARIO);
        this.panel1.setEnabledAt(PESTANA_EMPLEADOS, false);
        this.txtUsuario.requestFocusInWindow();
    }

    private void grabar() {
        String nombre = this.txtUsuario.getText().trim();
        if (this.txtEmpleadoID.getText().isEmpty()) {
            Dialogos.aviso(this, "Seleccione un empleado sin usuario o un usuario existente.");
            return;
        }
        if (nombre.isEmpty() || nombre.length() > LONGITUD_MAXIMA_USUARIO || nombre.contains(" ")) {
            Dialogos.aviso(this, "Indique el nombre de usuario (sin espacios, máximo " + LONGITUD_MAXIMA_USUARIO
                    + " caracteres).");
            this.txtUsuario.requestFocusInWindow();
            return;
        }
        if (this.cmbTipoUsuario.getSelectedIndex() <= 0) {
            Dialogos.aviso(this, "Seleccione el tipo de usuario.");
            this.cmbTipoUsuario.requestFocusInWindow();
            return;
        }
        char[] contrasena = this.txtPassWord.getPassword();
        try {
            boolean cambiarContrasena = seleccionado == null || contrasena.length > 0;
            String error = cambiarContrasena ? Contrasenas.validarNueva(contrasena) : null;
            if (error != null) {
                Dialogos.aviso(this, error);
                this.txtPassWord.requestFocusInWindow();
                return;
            }
            Usuario user = new Usuario();
            user.setIdLocalR(Integer.parseInt(this.txtIdLocal.getText()));
            user.setUsuario(nombre);
            user.setTipUsuaruario(TipoUsuario.deDescripcion(this.cmbTipoUsuario.getSelectedItem().toString()).getCodigo());
            user.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));
            if (seleccionado == null) {
                this.uDao.insertaUsuario(user, contrasena);
                Dialogos.info(this, "Usuario \"" + nombre + "\" creado.");
            } else {
                user.setUsuarioID(seleccionado.getUsuarioID());
                this.uDao.actualizarUsuario(user);
                if (cambiarContrasena) {
                    this.uDao.cambiarContrasena(user.getUsuarioID(), contrasena);
                }
                Dialogos.info(this, "Usuario actualizado" + (cambiarContrasena ? " (contraseña cambiada)." : "."));
            }
        } finally {
            Arrays.fill(contrasena, '\0');
        }
        this.llenaTblUsuario(this.txtBuscarUsuario.getText().trim());
        this.llenaTblEmpleado(this.txtBuscarEmpleado.getText().trim());
        this.limpiar();
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla de usuarios el usuario que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar el usuario \"" + seleccionado.getUsuario() + "\"?")) {
            return;
        }
        this.uDao.eliminarUsuario(seleccionado);
        this.llenaTblUsuario(this.txtBuscarUsuario.getText().trim());
        this.llenaTblEmpleado(this.txtBuscarEmpleado.getText().trim());
        this.limpiar();
    }

    private void limpiar() {
        seleccionado = null;
        this.tblUsuario.clearSelection();
        this.tblEmpleado.clearSelection();
        this.txtUsuarioId.setText("");
        this.txtIdLocal.setText("");
        this.txtEmpleadoID.setText("");
        this.txtUsuario.setText("");
        this.txtPassWord.setText("");
        this.cmbTipoUsuario.setSelectedIndex(0);
        this.btnGrabar.setText("Grabar");
        this.panel1.setEnabledAt(PESTANA_EMPLEADOS, true);
        this.panel1.setSelectedIndex(PESTANA_EMPLEADOS);
        this.panel1.setEnabledAt(PESTANA_USUARIO, false);
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
    private javax.swing.JPasswordField txtPassWord;
    private javax.swing.JTextField txtUsuario;
    private javax.swing.JTextField txtUsuarioId;
    // End of variables declaration//GEN-END:variables
}
