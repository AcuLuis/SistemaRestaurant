
package UI;

import BEAN.*;
import DAO.*;
import UTIL.Util;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class FrmContrato extends javax.swing.JInternalFrame {

    DefaultTableModel dtmEmpleado;
    DefaultTableModel dtmEmpleadoSinContrato;
    DefaultTableModel dtmContratos;
    
    EmpleadoDao empDao = new EmpleadoDao();
    RolDao rolDao = new RolDao();
    AreaDao areaDao = new AreaDao();
    ContratoDao contratoDao = new ContratoDao();
    Util u = new Util();
    
    int idEmpleado = 0;
    
    public FrmContrato() {
        initComponents();
        
        
        dtmEmpleado = (DefaultTableModel)this.tblBuscarEmpleado.getModel();
        dtmEmpleadoSinContrato = (DefaultTableModel) this.tblBuscarEmpleadoSinContrato.getModel();
        dtmContratos = (DefaultTableModel) this.tblContratos.getModel();
        
        this.llenaTblEmpleado(false, "");
        this.llenaTblEmpleadoSinContrato(false, "");
        this.llenaTblContratos(false, "");
        this.llenaCmbEstado();
        
        
        if(this.idEmpleado == 0){
        
            this.tabPBuscaEmpleado.setEnabledAt(2, false);
        }else if(this.idEmpleado != 0){
            this.tabPBuscaEmpleado.setEnabledAt(2, true);
        }
        
        this.llenaCmbRol();
        this.llenaCmbArea();
    }
    
    public void llenaCmbEstado(){
    
        this.cmbEstado.addItem("");
        this.cmbEstado.addItem("Activo");
        this.cmbEstado.addItem("No Activo");
    
    }
    
    public void llenaTblEmpleado(boolean valida, String cadena){
    
        Vector<Empleado> listaEmp;
        
        listaEmp = empDao.listaEmpleado(valida, cadena);     
        dtmEmpleado.setRowCount(0);
        
        for(int i=0;i<listaEmp.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaEmp.get(i).getEmpleadoID());
            vector.addElement(listaEmp.get(i).getNombres());
            vector.addElement(listaEmp.get(i).getApellidos());
            vector.addElement(listaEmp.get(i).getFechaNacimiento());
            if(listaEmp.get(i).getGenero() == 1){
                vector.addElement("masculino");
            }else if (listaEmp.get(i).getGenero() == 0){
                vector.addElement("femenino");
            }
            vector.addElement(listaEmp.get(i).getDni());
            vector.addElement(listaEmp.get(i).getEstadoCivil());
            
            dtmEmpleado.addRow(vector);
        }
    }
    
    
    public void llenaTblEmpleadoSinContrato(boolean valida, String cadena){
    
        Vector<Empleado> listaEmp;
        
        listaEmp = empDao.sinContrato(valida, cadena);     
        this.dtmEmpleadoSinContrato.setRowCount(0);
        
        for(int i=0;i<listaEmp.size();i++){
            
            Vector vector = new Vector();
            vector.addElement(listaEmp.get(i).getEmpleadoID());
            vector.addElement(listaEmp.get(i).getNombres());
            vector.addElement(listaEmp.get(i).getApellidos());
            vector.addElement(listaEmp.get(i).getFechaNacimiento());
            if(listaEmp.get(i).getGenero() == 1){
                vector.addElement("masculino");
            }else if (listaEmp.get(i).getGenero() == 0){
                vector.addElement("femenino");
            }
            vector.addElement(listaEmp.get(i).getDni());
            vector.addElement(listaEmp.get(i).getEstadoCivil());
            
            this.dtmEmpleadoSinContrato.addRow(vector);
        }
       
        
    }
    
    public void llenaTblContratos(boolean valida, String cadena){
    
        Vector<Contrato> listaContrato;
        Util u = new Util();
        
        listaContrato = contratoDao.ListaItem(valida, cadena);
        this.dtmContratos.setRowCount(0);
        
        for(int i=0; i<listaContrato.size(); i++){
        
            Vector vector = new Vector();
            
            vector.addElement(listaContrato.get(i).getContratoID());
            vector.addElement(listaContrato.get(i).getEmpleadoID());
            vector.addElement(this.rolDao.devRol(listaContrato.get(i).getRolID()));
            vector.addElement(this.areaDao.devArea(listaContrato.get(i).getAreaID()));
            vector.addElement(listaContrato.get(i).getFechaIni());
            vector.addElement(listaContrato.get(i).getFechaFin());
            if(listaContrato.get(i).getEstado() == 1){
                vector.addElement("Activo");
            }else{
                vector.addElement("No Activo");
            }
            vector.addElement(listaContrato.get(i).getSueldo());
            
            this.dtmContratos.addRow(vector);
        
        }
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
                "ContratoID", "EmpleadoID", "Rol", "Area", "FechaInicio", "FechaFin", "Estado", "Sueldo"
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
                "EmpleadoID", "Nombres", "Apellidos", "FechaNacimiento", "Genero", "DNI", "EstadoCivil"
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

        tabPBuscaEmpleado.addTab("Empleados General", panelEmpleadoGeneral);

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
                "EmpleadoID", "Nombres", "Apellidos", "FechaNacimiento", "Genero", "DNI", "EstadoCivil"
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

        tabPBuscaEmpleado.addTab("Buscar Empleado Sin Contrato", panelEmpleadoSinContrato);

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

        jLabel2.setText("EmpleadoID");

        jLabel3.setText("Rol");

        jLabel4.setText("Area");

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

        jLabel14.setText("ContratoID");

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
        btnCerrar.setText("CERRAR");
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
        if(this.txtBuscarEmpleado.getText().isEmpty()){
            this.llenaTblEmpleado(false,"");
        }else{
            this.llenaTblEmpleado(true,this.txtBuscarEmpleado.getText());
        }
    }//GEN-LAST:event_txtBuscarEmpleadoKeyReleased

    private void txtBuscarEmpleadoSinContratoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBuscarEmpleadoSinContratoKeyReleased
        if(this.txtBuscarEmpleadoSinContrato.getText().isEmpty()){
            this.llenaTblEmpleadoSinContrato(false,"");
        }else{
            this.llenaTblEmpleadoSinContrato(true,this.txtBuscarEmpleadoSinContrato.getText());
        }
        
    }//GEN-LAST:event_txtBuscarEmpleadoSinContratoKeyReleased

    private void txtContratosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtContratosKeyReleased
        if(this.txtContratos.getText().isEmpty()){
            this.llenaTblContratos(false,"");
        }else{
            this.llenaTblContratos(true,this.txtContratos.getText());
        }
    }//GEN-LAST:event_txtContratosKeyReleased

    private void tblBuscarEmpleadoSinContratoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBuscarEmpleadoSinContratoMouseClicked
        
        int posiFilEmpSinCon = this.tblBuscarEmpleadoSinContrato.getSelectedRow();
        
        idEmpleado = (int) this.tblBuscarEmpleadoSinContrato.getValueAt(posiFilEmpSinCon, 0);
        this.tabPBuscaEmpleado.setEnabledAt(2, true);
        
        this.tabPBuscaEmpleado.setSelectedIndex(2);
        
        this.txtContratoID.setText(Integer.toString(u.idNext("Contrato", "contratoID")));
        this.txtEmpleadoID.setText(Integer.toString(idEmpleado));
        this.tabPBuscaEmpleado.setEnabledAt(1, false);
    }//GEN-LAST:event_tblBuscarEmpleadoSinContratoMouseClicked

    private void llenaCmbRol(){
        
        Vector<Rol> listaRol = new Vector<Rol>();
        
        this.cmbRol.addItem("");
        
        listaRol = this.rolDao.llenaRol();
        
        for(int i=0; i<listaRol.size();i++){
        
            this.cmbRol.addItem(listaRol.get(i).getDescripRol());
        }
    }

    private void llenaCmbArea(){
        
        Vector<Area> listaArea = new Vector<Area>();
        
        this.cmbArea.addItem("");
        
        listaArea = this.areaDao.llenaArea();
        
        for(int i=0; i<listaArea.size();i++){
        
            this.cmbArea.addItem(listaArea.get(i).getDescripArea());
        }
    }
    
    
    
    private void btnLimpiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimpiarActionPerformed
        
        this.limpia();
    }//GEN-LAST:event_btnLimpiarActionPerformed

    private void limpia(){
        this.txtEmpleadoID.setText("");
        this.cmbRol.setSelectedItem("");
        this.cmbArea.setSelectedItem("");
        this.txtFechaInicio.setText("");
        this.txtFechaFin.setText("");
        this.txtSueldo.setText("");
        this.cmbEstado.setSelectedItem("");
        this.btnGrabar.setText("Grabar");
        this.tabPBuscaEmpleado.setEnabledAt(2, false);
        this.tabPBuscaEmpleado.setEnabledAt(1, true);
        
        this.tabPBuscaEmpleado.setSelectedIndex(1);
    }
    
    private void btnGrabarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGrabarActionPerformed
        
        if(this.valida() == true){
        
            if(this.btnGrabar.getText().equals("Grabar")){
                Contrato contrato = new Contrato();

                contrato.setContratoID(Integer.parseInt(this.txtContratoID.getText()));
                contrato.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));

                contrato.setRolID(this.rolDao.devIDRol(this.cmbRol.getSelectedItem().toString()));
                contrato.setAreaID(this.areaDao.devIDArea(this.cmbArea.getSelectedItem().toString()));
                
                contrato.setFechaIni(this.txtFechaInicio.getText());
                contrato.setFechaFin(this.txtFechaFin.getText());

                if(this.cmbEstado.getSelectedItem().equals("Activo")){
                    contrato.setEstado(1);
                }else{
                    contrato.setEstado(0);
                }

                contrato.setSueldo(Double.parseDouble(this.txtSueldo.getText()));

                this.contratoDao.insertaContrato(contrato);
                this.limpia();
                this.llenaTblContratos(false, "");
            }else{
                
                Contrato contrato = new Contrato();
                contrato.setContratoID(Integer.parseInt(this.txtContratoID.getText()));
                contrato.setEmpleadoID(Integer.parseInt(this.txtEmpleadoID.getText()));

                contrato.setRolID(this.rolDao.devIDRol(this.cmbRol.getSelectedItem().toString()));
                contrato.setAreaID(this.areaDao.devIDArea(this.cmbArea.getSelectedItem().toString()));
                
                contrato.setFechaIni(this.txtFechaInicio.getText());
                contrato.setFechaFin(this.txtFechaFin.getText());

                if(this.cmbEstado.getSelectedItem().equals("Activo")){
                    contrato.setEstado(1);
                }else{
                    contrato.setEstado(0);
                }
                contrato.setSueldo(Double.parseDouble(this.txtSueldo.getText()));
                
                this.contratoDao.actualizarContrato(contrato);
                this.llenaTblContratos(false, "");
                this.limpia();
            
            }
            this.btnGrabar.setText("Grabar");
            this.tabPBuscaEmpleado.setEnabledAt(1, true);
            this.llenaTblEmpleado(false, "");
            this.llenaTblEmpleadoSinContrato(false, "");
        }
        
    }//GEN-LAST:event_btnGrabarActionPerformed

    private void tblContratosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblContratosMouseClicked
        
        this.tabPBuscaEmpleado.setEnabledAt(2, true);
        this.tabPBuscaEmpleado.setSelectedIndex(2);
        int idx = this.tblContratos.getSelectedRow();
        
        this.txtContratoID.setText(this.dtmContratos.getValueAt(idx, 0).toString());
        this.txtEmpleadoID.setText(this.dtmContratos.getValueAt(idx, 1).toString());
        this.cmbRol.setSelectedItem(this.dtmContratos.getValueAt(idx, 2).toString());
        this.cmbArea.setSelectedItem(this.dtmContratos.getValueAt(idx, 3).toString());
        this.txtFechaInicio.setText(this.dtmContratos.getValueAt(idx, 4).toString());
        this.txtFechaFin.setText(this.dtmContratos.getValueAt(idx, 5).toString());
        this.cmbEstado.setSelectedItem(this.dtmContratos.getValueAt(idx, 6).toString());
        this.txtSueldo.setText(this.dtmContratos.getValueAt(idx, 7).toString());
        
        this.tabPBuscaEmpleado.setEnabledAt(1, false);
        this.btnGrabar.setText("Actualizar");
   
    }//GEN-LAST:event_tblContratosMouseClicked

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        
        if(this.valida()== true){
        
            Contrato contrato = new Contrato();
            
            contrato.setContratoID(Integer.parseInt(this.txtContratoID.getText()));
        
            this.contratoDao.eliminarContrato(contrato);
            
            this.llenaTblContratos(false, "");
            this.llenaTblEmpleado(false, "");
            this.llenaTblEmpleadoSinContrato(false, "");
            this.limpia();
        
        }
        
        
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnCerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCerrarActionPerformed
        this.dispose();
                
    }//GEN-LAST:event_btnCerrarActionPerformed

    private boolean valida(){
    
        if (this.txtFechaInicio.getText().isEmpty() && this.cmbEstado.getSelectedItem().equals("") && this.txtFechaFin.getText().isEmpty()  && this.txtSueldo.getText().isEmpty()
                && this.cmbRol.getSelectedItem().equals("") && this.cmbArea.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR LOS CAMPOS...");
        }else if (this.txtFechaInicio.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR LA FECHA DE INICIO...");
        }else if(this.txtFechaFin.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR LA FECHA DE FIN...");
        }else if(this.cmbEstado.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA SELECCIONAR EL ESTADO...");
        }else if(this.txtSueldo.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR EL SUELDO...");
        }else if(this.cmbRol.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR EL ROL...");
        }else if(this.cmbArea.getSelectedItem().equals("")){
            JOptionPane.showMessageDialog(null, "FALTA LLENAR EL AREA...");
        }else{
            return true;
        }
        return false;
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
