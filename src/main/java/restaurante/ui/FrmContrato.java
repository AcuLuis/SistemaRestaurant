package restaurante.ui;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import restaurante.dao.AreaDao;
import restaurante.dao.ContratoDao;
import restaurante.dao.EmpleadoDao;
import restaurante.dao.RolDao;
import restaurante.modelo.Area;
import restaurante.modelo.Contrato;
import restaurante.modelo.Empleado;
import restaurante.modelo.Rol;
import restaurante.ui.util.BusquedaDiferida;
import restaurante.ui.util.Dialogos;
import restaurante.ui.util.Tablas;
import restaurante.ui.util.Validaciones;

/**
 * Gestión de contratos: se elige un empleado sin contrato para crear uno
 * nuevo, o un contrato existente para modificarlo.
 */
public class FrmContrato extends javax.swing.JInternalFrame {

    private static final int PESTANA_SIN_CONTRATO = 1;
    private static final int PESTANA_CONTRATO = 2;
    private static final String ACTIVO = "Activo";
    private static final String NO_ACTIVO = "No Activo";

    private final EmpleadoDao empDao = new EmpleadoDao();
    private final ContratoDao contratoDao = new ContratoDao();
    private final List<Rol> roles;
    private final List<Area> areas;
    private final BusquedaDiferida busquedaEmpleados;
    private final BusquedaDiferida busquedaSinContrato;
    private final BusquedaDiferida busquedaContratos;
    private List<Empleado> sinContrato = List.of();
    private List<Contrato> contratos = List.of();
    private Contrato seleccionado;

    public FrmContrato() {
        initComponents();
        this.roles = new RolDao().listarTodos();
        this.areas = new AreaDao().listarTodas();
        this.cmbRol.addItem("");
        this.roles.forEach(r -> this.cmbRol.addItem(r.getDescripRol()));
        this.cmbArea.addItem("");
        this.areas.forEach(a -> this.cmbArea.addItem(a.getDescripArea()));
        this.cmbEstado.addItem("");
        this.cmbEstado.addItem(ACTIVO);
        this.cmbEstado.addItem(NO_ACTIVO);
        for (JTable tabla : new JTable[] {tblBuscarEmpleado, tblBuscarEmpleadoSinContrato, tblContratos}) {
            Tablas.configurar(tabla);
        }
        Tablas.alSeleccionar(this.tblBuscarEmpleadoSinContrato, this::nuevoContrato);
        Tablas.alSeleccionar(this.tblContratos, this::cargarContrato);
        this.txtContratoID.setEditable(false);
        this.txtEmpleadoID.setEditable(false);
        String formato = "Formato " + Validaciones.FORMATO_FECHA + " (por ejemplo, 2024-01-31)";
        this.txtFechaInicio.setToolTipText(formato);
        this.txtFechaFin.setToolTipText(formato);
        this.busquedaEmpleados = new BusquedaDiferida(this.txtBuscarEmpleado, this::llenaTblEmpleado);
        this.busquedaSinContrato = new BusquedaDiferida(this.txtBuscarEmpleadoSinContrato, this::llenaTblEmpleadoSinContrato);
        this.busquedaContratos = new BusquedaDiferida(this.txtContratos, this::llenaTblContratos);
        this.llenaTblEmpleado("");
        this.llenaTblEmpleadoSinContrato("");
        this.llenaTblContratos("");
        this.limpia();
    }

    private void llenaTblEmpleado(String texto) {
        llenarEmpleados(this.tblBuscarEmpleado, empDao.listar(!texto.isEmpty(), texto));
    }

    private void llenaTblEmpleadoSinContrato(String texto) {
        sinContrato = empDao.listarSinContrato(!texto.isEmpty(), texto);
        llenarEmpleados(this.tblBuscarEmpleadoSinContrato, sinContrato);
    }

    private static void llenarEmpleados(JTable tabla, List<Empleado> empleados) {
        DefaultTableModel modelo = Tablas.vaciar(tabla);
        for (Empleado e : empleados) {
            modelo.addRow(new Object[] {e.getEmpleadoID(), e.getNombres(), e.getApellidos(), e.getFechaNacimiento(),
                e.getGenero() == 1 ? "Masculino" : "Femenino", e.getDni(), e.getEstadoCivil()});
        }
    }

    private void llenaTblContratos(String texto) {
        contratos = contratoDao.listar(!texto.isEmpty(), texto);
        DefaultTableModel modelo = Tablas.vaciar(this.tblContratos);
        for (Contrato c : contratos) {
            modelo.addRow(new Object[] {c.getContratoID(), c.getEmpleadoID(), descripcionRol(c.getRolID()),
                descripcionArea(c.getAreaID()), c.getFechaIni(), c.getFechaFin(),
                c.getEstado() == 1 ? ACTIVO : NO_ACTIVO, Tablas.importe(c.getSueldo())});
        }
    }

    private String descripcionRol(int id) {
        return roles.stream().filter(r -> r.getRolID() == id).map(Rol::getDescripRol).findFirst().orElse("");
    }

    private String descripcionArea(int id) {
        return areas.stream().filter(a -> a.getAreaID() == id).map(Area::getDescripArea).findFirst().orElse("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblContratos = new javax.swing.JTable();
        jLabel6 = new javax.swing.JLabel();
        txtContratos = new javax.swing.JTextField();
        tabPBuscaEmpleado = new javax.swing.JTabbedPane();
        panelEmpleadoGeneral = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblBuscarEmpleado = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        txtBuscarEmpleado = new javax.swing.JTextField();
        panelEmpleadoSinContrato = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtBuscarEmpleadoSinContrato = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        tblBuscarEmpleadoSinContrato = new javax.swing.JTable();
        panelContrato = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        txtFechaInicio = new javax.swing.JTextField();
        txtFechaFin = new javax.swing.JTextField();
        txtSueldo = new javax.swing.JTextField();
        cmbEstado = new javax.swing.JComboBox<>();
        btnGrabar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtEmpleadoID = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnEliminar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        txtContratoID = new javax.swing.JTextField();
        cmbRol = new javax.swing.JComboBox<>();
        cmbArea = new javax.swing.JComboBox<>();
        btnCerrar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("FORMULARIO CONTRATO");

        tblContratos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Empleado", "Rol", "Área", "Inicio", "Fin", "Estado", "Sueldo"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblContratos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblContratosMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(tblContratos);

        jLabel6.setText("Contratos");

        txtContratos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtContratosKeyReleased(evt);
            }
        });

        tabPBuscaEmpleado.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        tblBuscarEmpleado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empleado", "Nombres", "Apellidos", "Nacimiento", "Género", "DNI", "Estado civil"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(tblBuscarEmpleado);

        jLabel7.setText("Buscar");

        txtBuscarEmpleado.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmpleadoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout panelEmpleadoGeneralLayout = new javax.swing.GroupLayout(panelEmpleadoGeneral);
        panelEmpleadoGeneral.setLayout(panelEmpleadoGeneralLayout);
        panelEmpleadoGeneralLayout.setHorizontalGroup(
            panelEmpleadoGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEmpleadoGeneralLayout.createSequentialGroup()
                .addGroup(panelEmpleadoGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelEmpleadoGeneralLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jLabel7)
                        .addGap(28, 28, 28)
                        .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelEmpleadoGeneralLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 569, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        panelEmpleadoGeneralLayout.setVerticalGroup(
            panelEmpleadoGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEmpleadoGeneralLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(panelEmpleadoGeneralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtBuscarEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(26, 26, 26))
        );

        tabPBuscaEmpleado.addTab("Todos los empleados", panelEmpleadoGeneral);

        jLabel22.setText("Buscar");

        txtBuscarEmpleadoSinContrato.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBuscarEmpleadoSinContratoKeyReleased(evt);
            }
        });

        tblBuscarEmpleadoSinContrato.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Empleado", "Nombres", "Apellidos", "Nacimiento", "Género", "DNI", "Estado civil"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblBuscarEmpleadoSinContrato.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBuscarEmpleadoSinContratoMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(tblBuscarEmpleadoSinContrato);

        javax.swing.GroupLayout panelEmpleadoSinContratoLayout = new javax.swing.GroupLayout(panelEmpleadoSinContrato);
        panelEmpleadoSinContrato.setLayout(panelEmpleadoSinContratoLayout);
        panelEmpleadoSinContratoLayout.setHorizontalGroup(
            panelEmpleadoSinContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEmpleadoSinContratoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(panelEmpleadoSinContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEmpleadoSinContratoLayout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(53, 53, 53)
                        .addComponent(txtBuscarEmpleadoSinContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(356, 356, 356))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelEmpleadoSinContratoLayout.createSequentialGroup()
                        .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 570, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(80, 80, 80))))
        );
        panelEmpleadoSinContratoLayout.setVerticalGroup(
            panelEmpleadoSinContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelEmpleadoSinContratoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelEmpleadoSinContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(txtBuscarEmpleadoSinContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );

        tabPBuscaEmpleado.addTab("Empleados sin contrato", panelEmpleadoSinContrato);

        jLabel10.setText("Fecha Inicio");

        jLabel11.setText("Fecha Fin");

        jLabel12.setText("Sueldo");

        jLabel13.setText("Estado");

        btnGrabar.setText("Grabar");
        btnGrabar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGrabarActionPerformed(evt);
            }
        });

        jLabel2.setText("Empleado");

        jLabel3.setText("Rol");

        jLabel4.setText("Área");

        txtEmpleadoID.setEnabled(false);

        jLabel5.setText("DATOS GENERALES");
        jLabel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        btnLimpiar.setText("Volver");
        btnLimpiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimpiarActionPerformed(evt);
            }
        });

        jLabel14.setText("Código");

        txtContratoID.setEnabled(false);

        javax.swing.GroupLayout panelContratoLayout = new javax.swing.GroupLayout(panelContrato);
        panelContrato.setLayout(panelContratoLayout);
        panelContratoLayout.setHorizontalGroup(
            panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContratoLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(btnGrabar, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(97, 97, 97)
                .addComponent(btnLimpiar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(102, 102, 102))
            .addGroup(panelContratoLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel10)
                    .addComponent(jLabel11)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13))
                .addGap(18, 18, 18)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSueldo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(74, 74, 74)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addGroup(panelContratoLayout.createSequentialGroup()
                        .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3))
                        .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelContratoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(txtEmpleadoID, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelContratoLayout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addComponent(jLabel5)
                    .addGroup(panelContratoLayout.createSequentialGroup()
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addComponent(txtContratoID, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        panelContratoLayout.setVerticalGroup(
            panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelContratoLayout.createSequentialGroup()
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelContratoLayout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(txtFechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(1, 1, 1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelContratoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addGap(18, 18, 18)))
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(txtContratoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(txtFechaFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(txtEmpleadoID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(cmbEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbRol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(31, 31, 31)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(cmbArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(txtSueldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, Short.MAX_VALUE)
                .addGroup(panelContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnGrabar)
                    .addComponent(btnEliminar)
                    .addComponent(btnLimpiar))
                .addGap(57, 57, 57))
        );

        tabPBuscaEmpleado.addTab("Contrato", panelContrato);

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
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(tabPBuscaEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addGap(27, 27, 27)
                                .addComponent(txtContratos, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 658, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(494, 494, 494)
                        .addComponent(jLabel1)))
                .addContainerGap(22, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel1)
                .addGap(40, 40, 40)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtContratos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addComponent(tabPBuscaEmpleado, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnCerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtBuscarEmpleadoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoKeyReleased
        busquedaEmpleados.reiniciar();
    }//GEN-LAST:event_txtBuscarEmpleadoKeyReleased

    private void txtBuscarEmpleadoSinContratoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoSinContratoKeyReleased
        busquedaSinContrato.reiniciar();
    }//GEN-LAST:event_txtBuscarEmpleadoSinContratoKeyReleased

    private void txtContratosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContratosKeyReleased
        busquedaContratos.reiniciar();
    }//GEN-LAST:event_txtContratosKeyReleased

    private void tblBuscarEmpleadoSinContratoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscarEmpleadoSinContratoMouseClicked
        this.nuevoContrato();
    }//GEN-LAST:event_tblBuscarEmpleadoSinContratoMouseClicked

    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        this.limpia();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        this.grabar();
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblContratosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContratosMouseClicked
        this.cargarContrato();
    }//GEN-LAST:event_tblContratosMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        this.eliminar();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCerrarActionPerformed

    /** Prepara el alta de un contrato para el empleado elegido. */
    private void nuevoContrato() {
        int fila = Tablas.filaSeleccionada(this.tblBuscarEmpleadoSinContrato);
        if (fila < 0) {
            return;
        }
        limpiarCampos();
        seleccionado = null;
        this.txtContratoID.setText(String.valueOf(contratoDao.siguienteId()));
        this.txtEmpleadoID.setText(String.valueOf(sinContrato.get(fila).getEmpleadoID()));
        this.btnGrabar.setText("Grabar");
        abrirPestanaContrato();
    }

    private void cargarContrato() {
        int fila = Tablas.filaSeleccionada(this.tblContratos);
        if (fila < 0) {
            return;
        }
        seleccionado = contratos.get(fila);
        this.txtContratoID.setText(String.valueOf(seleccionado.getContratoID()));
        this.txtEmpleadoID.setText(String.valueOf(seleccionado.getEmpleadoID()));
        this.cmbRol.setSelectedItem(descripcionRol(seleccionado.getRolID()));
        this.cmbArea.setSelectedItem(descripcionArea(seleccionado.getAreaID()));
        this.txtFechaInicio.setText(seleccionado.getFechaIni());
        this.txtFechaFin.setText(seleccionado.getFechaFin());
        this.cmbEstado.setSelectedItem(seleccionado.getEstado() == 1 ? ACTIVO : NO_ACTIVO);
        this.txtSueldo.setText(Tablas.importe(seleccionado.getSueldo()));
        this.btnGrabar.setText("Actualizar");
        abrirPestanaContrato();
    }

    private void abrirPestanaContrato() {
        this.tabPBuscaEmpleado.setEnabledAt(PESTANA_CONTRATO, true);
        this.tabPBuscaEmpleado.setSelectedIndex(PESTANA_CONTRATO);
        this.tabPBuscaEmpleado.setEnabledAt(PESTANA_SIN_CONTRATO, false);
        this.cmbRol.requestFocusInWindow();
    }

    private void grabar() {
        if (this.txtEmpleadoID.getText().isEmpty()) {
            Dialogos.aviso(this, "Seleccione un empleado sin contrato o un contrato existente.");
            return;
        }
        Optional<LocalDate> inicio = Validaciones.fecha(this.txtFechaInicio.getText());
        Optional<LocalDate> fin = Validaciones.fecha(this.txtFechaFin.getText());
        Optional<Double> sueldo = Validaciones.decimalNoNegativo(this.txtSueldo.getText());
        if (this.cmbRol.getSelectedIndex() <= 0) {
            invalido(this.cmbRol, "Seleccione el rol.");
        } else if (this.cmbArea.getSelectedIndex() <= 0) {
            invalido(this.cmbArea, "Seleccione el área.");
        } else if (inicio.isEmpty()) {
            invalido(this.txtFechaInicio, "Indique una fecha de inicio válida (" + Validaciones.FORMATO_FECHA + ").");
        } else if (fin.isEmpty()) {
            invalido(this.txtFechaFin, "Indique una fecha de fin válida (" + Validaciones.FORMATO_FECHA + ").");
        } else if (fin.get().isBefore(inicio.get())) {
            invalido(this.txtFechaFin, "La fecha de fin no puede ser anterior a la de inicio.");
        } else if (this.cmbEstado.getSelectedIndex() <= 0) {
            invalido(this.cmbEstado, "Seleccione el estado del contrato.");
        } else if (sueldo.isEmpty() || sueldo.get() <= 0) {
            invalido(this.txtSueldo, "El sueldo debe ser un número mayor que cero.");
        } else {
            Contrato contrato = new Contrato();
            contrato.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));
            contrato.setRolID(roles.get(this.cmbRol.getSelectedIndex() - 1).getRolID());
            contrato.setAreaID(areas.get(this.cmbArea.getSelectedIndex() - 1).getAreaID());
            contrato.setFechaIni(inicio.get().toString());
            contrato.setFechaFin(fin.get().toString());
            contrato.setEstado(ACTIVO.equals(this.cmbEstado.getSelectedItem()) ? 1 : 0);
            contrato.setSueldo(sueldo.get());
            if (seleccionado == null) {
                this.contratoDao.insertar(contrato);
                Dialogos.info(this, "Contrato registrado con el código " + contrato.getContratoID() + ".");
            } else {
                contrato.setContratoID(seleccionado.getContratoID());
                this.contratoDao.actualizar(contrato);
                Dialogos.info(this, "Contrato actualizado.");
            }
            refrescar();
            this.limpia();
        }
    }

    private void eliminar() {
        if (seleccionado == null) {
            Dialogos.aviso(this, "Seleccione en la tabla de contratos el contrato que desea eliminar.");
            return;
        }
        if (!Dialogos.confirmar(this, "¿Desea eliminar el contrato " + seleccionado.getContratoID()
                + " del empleado " + seleccionado.getEmpleadoID() + "?")) {
            return;
        }
        this.contratoDao.eliminar(seleccionado);
        refrescar();
        this.limpia();
    }

    private void refrescar() {
        this.llenaTblContratos(this.txtContratos.getText().trim());
        this.llenaTblEmpleado(this.txtBuscarEmpleado.getText().trim());
        this.llenaTblEmpleadoSinContrato(this.txtBuscarEmpleadoSinContrato.getText().trim());
    }

    private void invalido(javax.swing.JComponent campo, String mensaje) {
        Dialogos.aviso(this, mensaje);
        campo.requestFocusInWindow();
    }

    private void limpiarCampos() {
        this.txtContratoID.setText("");
        this.txtEmpleadoID.setText("");
        this.cmbRol.setSelectedIndex(0);
        this.cmbArea.setSelectedIndex(0);
        this.txtFechaInicio.setText("");
        this.txtFechaFin.setText("");
        this.txtSueldo.setText("");
        this.cmbEstado.setSelectedIndex(0);
    }

    private void limpia() {
        seleccionado = null;
        limpiarCampos();
        this.tblContratos.clearSelection();
        this.tblBuscarEmpleadoSinContrato.clearSelection();
        this.btnGrabar.setText("Grabar");
        this.tabPBuscaEmpleado.setEnabledAt(PESTANA_CONTRATO, false);
        this.tabPBuscaEmpleado.setEnabledAt(PESTANA_SIN_CONTRATO, true);
        this.tabPBuscaEmpleado.setSelectedIndex(PESTANA_SIN_CONTRATO);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCerrar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnGrabar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JComboBox<String> cmbArea;
    private javax.swing.JComboBox<String> cmbEstado;
    private javax.swing.JComboBox<String> cmbRol;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JPanel panelContrato;
    private javax.swing.JPanel panelEmpleadoGeneral;
    private javax.swing.JPanel panelEmpleadoSinContrato;
    private javax.swing.JTabbedPane tabPBuscaEmpleado;
    private javax.swing.JTable tblBuscarEmpleado;
    private javax.swing.JTable tblBuscarEmpleadoSinContrato;
    private javax.swing.JTable tblContratos;
    private javax.swing.JTextField txtBuscarEmpleado;
    private javax.swing.JTextField txtBuscarEmpleadoSinContrato;
    private javax.swing.JTextField txtContratoID;
    private javax.swing.JTextField txtContratos;
    private javax.swing.JTextField txtEmpleadoID;
    private javax.swing.JTextField txtFechaFin;
    private javax.swing.JTextField txtFechaInicio;
    private javax.swing.JTextField txtSueldo;
    // End of variables declaration//GEN-END:variables
}
