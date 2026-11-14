
package UI;

import BEAN.*;
import DAO.*;
import UTIL.*;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class LoginDefecto extends javax.swing.JFrame {
    
    Empleado empleado = new Empleado();
    Usuario usuario = new Usuario();
    LocalR local = new LocalR();
    Util util = new Util();
    Login login = new Login();
    Servidor servi = new Servidor();
    public int idLocal;
    
    inciaBdDao inicia = new inciaBdDao();

    public LoginDefecto() {
        
        initComponents();
        this.setLocationRelativeTo(null);
        this.llenaCmbGenero();
        this.llenaCmbEstadoCivil();
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
    
        if (this.valida()==true){
        
            empleado.setEmpleadoID(1001);
            empleado.setUsuarioModifID(1001);
            empleado.setUsuarioCreaID(1001);
            empleado.setNombres(this.txtNOMBREUSERDEFECTO.getText());
            empleado.setApellidos(this.txtAPELLIDOS.getText());
            empleado.setFechaNacimiento(this.txtNACIMIENTO.getText());
            empleado.setFechCrea("2002-01-13");
            if(this.cmbGenero.getSelectedItem().equals("MASCULINO")){
                empleado.setGenero(1);
            }else if(this.cmbGenero.getSelectedItem().equals("FEMENINO")){
                empleado.setGenero(0);
            }
            empleado.setFechModif("2002-01-13");
            empleado.setDni(this.txtDNI.getText());
            empleado.setEstadoCivil(this.cmbEstadoCivil.getSelectedItem().toString());
        
            idLocal = Integer.parseInt(this.txtIDLOCALDEFECTO.getText());
            local.setIdLocalR(Integer.parseInt(this.txtIDLOCALDEFECTO.getText()));
            local.setUbicacion(this.txtUBICACION.getText());
            local.setCapacidadMax(Integer.parseInt(this.txtCAPACIDAD.getText()));
            local.setPisos(Integer.parseInt(this.txtPISOS.getText()));
            local.setHoraApertura("23:59:59.9999999");
            local.setHoraCerrado("23:59:59.9999999");
            
            usuario.setUsuarioID(101);
            usuario.setIdLocalR(Integer.parseInt(this.txtIDLOCALDEFECTO.getText()));
            usuario.setUsuario(this.txtNOMBREUSERDEFECTO.getText());
            usuario.setContraseña(this.txtCONTRADEFECTO.getText());
            usuario.setTipUsuaruario(1);
            usuario.setEmpleadoID(1001);
            
            this.inicia.creacionBD(empleado, usuario, local);
            
            try {
                this.creaPDF();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(LoginDefecto.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "PDF DE REFERENCIA DE LA BD CREADA en DESCARGAS...");
            login.setVisible(true);
            this.dispose();
        }
    }
    
    
    public boolean valida(){
    
        if(this.txtIDLOCALDEFECTO.getText().isEmpty() && this.txtNOMBREUSERDEFECTO.getText().isEmpty() && 
           this.txtCONTRADEFECTO.getText().isEmpty() && 
           this.txtAPELLIDOS.getText().isEmpty() && this.txtNACIMIENTO.getText().isEmpty() &&
           this.cmbGenero.getSelectedItem().toString().isEmpty() && this.txtDNI.getText().isEmpty() &&
           this.cmbEstadoCivil.getSelectedItem().toString().isEmpty() && this.txtUBICACION.getText().isEmpty() &&
           this.txtCAPACIDAD.getText().isEmpty() && this.txtPISOS.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta llenar los campos...");
        }else if(this.txtIDLOCALDEFECTO.getText().isEmpty()){
            JOptionPane.showMessageDialog(null, "Falta el IDLOCAL...");
        }else if(this.txtNOMBREUSERDEFECTO.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta el NOMBRE USUARIO...");
        }else if(this.txtCONTRADEFECTO.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta la CONTRASEÑA...");
        }else  if(this.txtAPELLIDOS.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta el APELLIDO...");
        }else if(this.txtNACIMIENTO.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta FECHA DE NACIMIENTO...");
        }else if(this.cmbGenero.getSelectedItem().toString().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta GENERO...");
        }else if(this.txtDNI.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta DNI...");
        }else if(this.cmbEstadoCivil.getSelectedItem().toString().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta ESTADO CIVIL...");
        }else if(this.txtUBICACION.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta UBICACION...");
        }else if(this.txtCAPACIDAD.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta CAPACIDAD MAX...");
        }else if(this.txtPISOS.getText().isEmpty()){
        
            JOptionPane.showMessageDialog(null, "Falta NUMERO DE PISOS...");
        }else {
            
            return true;
        }
        
        return false;
    }
    
        public void creaPDF() throws FileNotFoundException{
        
        com.itextpdf.text.Document documento = new com.itextpdf.text.Document();
        DbBean con = new DbBean();
        String sql;
        String ruta = System.getProperty("user.home");
        String nombreArchivo = "BD_Restaurant.pdf";
        String rutaCompleta = ruta + "/downloads/" + nombreArchivo;

        try{

            File archivo = new File(rutaCompleta);
            int contador = 1;

            while (archivo.exists()) {
                nombreArchivo = "BD_Restaurant(" + contador + ").pdf";
                rutaCompleta = ruta + "/downloads/" + nombreArchivo;
                archivo = new File(rutaCompleta);
                contador++;
        }

        PdfWriter.getInstance(documento, new FileOutputStream(rutaCompleta));
        documento.open();
        
        Paragraph titulo = new Paragraph("'BD_RESTAURANT'", new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD));
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(20);
        documento.add(titulo);    
        
        Paragraph subtitulo = new Paragraph("\nALGUNOS INSERTS IMPORTANTES: ", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(20);
        documento.add(subtitulo); 
        
        String inserts = "insert into empleado values (empleadoID,usuarioModifID,usuarioCreaID,'nombres','apellidos','fechaNacimiento','fechaCrea',genero,\n'fechaModif','dni','estadoCivil')" + 
                          "\ninsert into localR values(idLocalR,'Ubicacion',capacidadMax,pisos,'horaApertura','horaCerrado')"+
                          "\ninsert into usuario values(usuarioID,idLocalR,'usuario','contraseña',tipoUsuario,empleadoID)\n"+
                          "\ninsert into servidor values(servidorID,nomServidor)\n";
        
        Paragraph primero = new Paragraph(inserts, new Font(Font.FontFamily.TIMES_ROMAN, 12));
        documento.add(primero);
       
        Paragraph subtitulo2 = new Paragraph("\nCUERPO DE LA BD: \n", new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD));
        subtitulo2.setAlignment(Element.ALIGN_CENTER);
        subtitulo2.setSpacingAfter(20);
        documento.add(subtitulo2);
        
        String texto = "\nuse master\n" +
                        "go\n" +
                        "create database restaurante\n" +
                        "go\n" +
                        "use restaurante\n" +
                        "go\n"+
                    "create table Area(\n" +
                    "areaID int constraint pkareaID primary key,\n" +
                    "descripArea varchar(40)\n" +
                    ")\n" +
                    "go\n" +
                    "create table CabOrdenDeProduccionSemanal(\n" +
                    "ordenProduccionID int constraint pkOrdenProduccionID primary key,\n" +
                    "empleadoID int,\n" +
                    "descripArea varchar(40),\n" +
                    "fechaxSem date\n" +
                    ")\n" +
                    "go\n"+
                "create table CabOrdenDeVenta(\n" +
                    "ordenDeVentaID int constraint pkOrdenDeVentaID primary key,\n" +
                    "empleadoID int,\n" +
                    "clienteID int,\n" +
                    "mesaID int,\n" +
                    "fechaVenta datetime,\n" +
                    "metodoPago int,\n" +
                    "tipEntrega varchar(15)\n" +
                    ")\n" +
                    "go\n" +
                    "create table CabReceta(\n" +
                    "recetaID int constraint pkRecetaID primary key,\n" +
                    "empleadoID int,\n" +
                    "productoID int,\n" +
                    "fechaElab date,\n" +
                    "preparacion varchar(300)\n" +
                    ")\n" +
                    "go\n"+
                "create table Cliente(\n" +
                    "clienteID int constraint pkClienteID primary key,\n" +
                    "nombres varchar(40),\n" +
                    "apellidos varchar(40),\n" +
                    "dni varchar(8),\n" +
                    "genero int,\n" +
                    "estadoCivil varchar(10),\n" +
                    "telefono int,\n" +
                    "correo varchar(20),\n" +
                    "fechNaci date,\n" +
                    "estado int,\n" +
                    "direccion varchar(30)\n" +
                    ")\n" +
                    "go\n" +
                    "create table Contrato(\n" +
                    "contratoID int constraint pkContratoID primary key,\n" +
                    "empleadoID int,\n" +
                    "rolID int,\n" +
                    "areaID int,\n" +
                    "fechaIni date,\n" +
                    "fechaFin date,\n" +
                    "estado int,\n" +
                    "sueldo float\n" +
                    ")\n" +
                    "go\n"+
                "create table DetOrdenDeProduccionSemanal(\n" +
                    "ordenProduccionID int,\n" +
                    "productoID int,\n" +
                    "cantidad int,\n" +
                    "estado int\n" +
                    ")\n" +
                    "go\n" +
                    "create table DetOrdenDeVenta(\n" +
                    "ordenDeVentaID int,\n" +
                    "productoID int,\n" +
                    "idPedidos int,\n" +
                    "cantidad int\n" +
                    ")\n" +
                    "go\n" +
                    "create table DetReceta(\n" +
                    "recetaID int,\n" +
                    "insumoID int,\n" +
                    "cantidad int\n" +
                    ")\n" +
                    "go\n" +
                    "create table Empleado(\n" +
                    "empleadoID int constraint pkEmpleadoID primary key,\n" +
                    "usuarioModifiID int,\n" +
                    "usuarioCreaID int,\n" +
                    "nombres varchar(40),\n" +
                    "apellidos varchar(40),\n" +
                    "fechaNacimiento date,\n" +
                    "fechCrea date,\n" +
                    "genero int,\n" +
                    "fechModif date,\n" +
                    "dni varchar(8),\n" +
                    "estadoCivil varchar(10)\n" +
                    ")\n" +
                    "go\n"+
                "create table Insumo(\n" +
                    "insumoID int constraint pkInsumoID primary key,\n" +
                    "idTipInsumo int,\n" +
                    "unidadMedida varchar(10),\n" +
                    "descripInsumo varchar(20),\n" +
                    "precioUnit float,\n" +
                    "cantidad int\n" +
                    ")\n" +
                    "go\n" +
                    "create table LocalR(\n" +
                    "idLocalR int constraint pkIdLocalR primary key,\n" +
                    "Ubicacion varchar(30),\n" +
                    "capcidadMax int,\n" +
                    "pisos int,\n" +
                    "horaApertura time,\n" +
                    "horaCerrado time\n" +
                    ")\n" +
                    "go\n" +
                    "create table Mesa(\n" +
                    "mesaID int constraint pkMesaID primary key,\n" +
                    "ubic varchar(30),\n" +
                    "numSillas int,\n" +
                    "estado int\n" +
                    ")\n" +
                    "go\n" +
                    "create table Pedidos(\n" +
                    "idPedidos int constraint pkIdPedidos primary key,\n" +
                    "empleadoID int,\n" +
                    "ubic varchar(30),\n" +
                    "estado int\n" +
                    ")\n" +
                    "go\n" +
                    "create table Plato(\n" +
                    "idPlato int constraint pkIdPlato primary key,\n" +
                    "descPlato varchar(30)\n" +
                    ")\n" +
                    "go\n"+
                "create table Producto(\n" +
                    "productoID int constraint pkProductoID primary key,\n" +
                    "idPlato int,\n" +
                    "idTipProducto int,\n" +
                    "descripProducto varchar(40),\n" +
                    "cantidad int,\n" +
                    "precioUnit float\n" +
                    ")\n" +
                    "go\n" +
                    "create table Reserva(\n" +
                    "reservaID int constraint pkReservaID primary key,\n" +
                    "empleadoID int,\n" +
                    "clienteID int,\n" +
                    "fecha date,\n" +
                    "estado int\n" +
                    ")\n" +
                    "go\n" +
                    "create table Rol(\n" +
                    "rolID int constraint pkRolID primary key,\n" +
                    "descripRol varchar(25)\n" +
                    ")\n" +
                    "go\n" +
                    "create table TipInsumo(\n" +
                    "idTipInsumo int constraint pkIdTipInsumo primary key,\n" +
                    "descripcion varchar(25)\n" +
                    ")\n" +
                    "go\n" +
                    "create table TipProducto(\n" +
                    "idTipProducto int constraint pkIdTipProducto primary key,\n" +
                    "descripcion varchar(15)\n" +
                    ")\n" +
                    "go\n" +
                    "create table Usuario(\n" +
                    "usuarioID int constraint pkUsuarioID primary key,\n" +
                    "idLocalR int,\n" +
                    "usuario varchar(25),\n" +
                    "contraseña varchar(20),\n" +
                    "tipoUsuario int\n" +
                    ")"+ "\ngo"+
                       
                    "alter table CabOrdenDeProduccionSemanal\n" +
                    "add constraint fkEmpleadoID foreign key (empleadoID) references Empleado(empleadoID)\n" +
                    "go\n" +
                    "alter table CabOrdenDeVenta\n" +
                    "add constraint fk1EmpleadoID foreign key (empleadoID) references Empleado(empleadoID),\n" +
                    "constraint fkClienteID foreign key (clienteID) references Cliente(clienteID),\n" +
                    "constraint fkMesaID foreign key (mesaID) references Mesa(mesaID)\n" +
                    "go\n" +
                    "alter table CabReceta\n" +
                    "add constraint fk2EmpleadoID foreign key (empleadoID) references Empleado(empleadoID),\n" +
                    "constraint fkProductoID foreign key (productoID) references Producto(productoID)\n" +
                    "go\n" +
                    "alter table Contrato\n" +
                    "add constraint fk3EmpleadoID foreign key (empleadoID) references Empleado(empleadoID),\n" +
                    "constraint fkRolID foreign key (rolID) references Rol(rolID),\n" +
                    "constraint fkAreaID foreign key (areaID) references Area(areaID)\n" +
                    "go\n" +
                    "alter table DetOrdenDeProduccionSemanal\n" +
                    "add constraint fk1ProductoID foreign key (productoID) references Producto(productoID),\n" +
                    "constraint fkOrdenProduccionID foreign key (ordenProduccionID) references CabOrdenDeProduccionSemanal(ordenProduccionID)\n" +
                    "go\n" +
                    "alter table DetOrdenDeVenta\n" +
                    "add constraint fk2ProductoID foreign key (productoID) references Producto(productoID),\n" +
                    "constraint fkIdPedidos foreign key (idPedidos) references Pedidos(idPedidos),\n" +
                    "constraint fkOrdenDeVentaID foreign key (ordenDeVentaID) references CabOrdenDeVenta(ordenDeVentaID)\n" +
                    "go\n" +
                    "alter table DetReceta\n" +
                    "add constraint fkInsumoID foreign key (insumoID) references Insumo(insumoID),\n" +
                    "constraint fkRecetaID foreign key (recetaID) references CabReceta(recetaID)\n" +
                    "go\n" +
                    "alter table Insumo\n" +
                    "add constraint fkIdTipInsumo foreign key (idTipInsumo) references TipInsumo(idTipInsumo)\n" +
                    "go\n" +
                    "alter table Usuario\n" +
                    "add constraint fkIdLocalR foreign key (idLocalR) references LocalR(idLocalR),\n" +
                    "empleadoID int FOREIGN KEY REFERENCES Empleado(empleadoID),\n" +
                    "CONSTRAINT FK_Usuario_Empleado UNIQUE(empleadoID)\n" +
                    "go\n" +
                    "alter table Pedidos\n" +
                    "add constraint fk4EmpleadoID foreign key (empleadoID) references Empleado(empleadoID)\n" +
                    "go\n" +
                    "alter table Producto\n" +
                    "add constraint fkIdPlato foreign key (idPlato) references Plato(idPlato),\n" +
                    "constraint fkIdTipProducto foreign key (idTipProducto) references TipProducto(idTipProducto)\n" +
                    "go\n" +
                    "alter table Reserva\n" +
                    "add constraint fk5EmpleadoID foreign key (empleadoID) references Empleado(empleadoID),\n" +
                    "constraint fk1ClienteID foreign key (clienteID) references Cliente(clienteID)\n";

        Paragraph parrafo = new Paragraph(texto, new Font(Font.FontFamily.TIMES_ROMAN, 12));
        documento.add(parrafo);        
        
    
        documento.close();
    
        }catch(DocumentException | HeadlessException | FileNotFoundException e){
        
            e.printStackTrace();  
        }
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
        txtCONTRADEFECTO = new javax.swing.JTextField();
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
    private javax.swing.JTextField txtCONTRADEFECTO;
    private javax.swing.JTextField txtDNI;
    private javax.swing.JTextField txtIDLOCALDEFECTO;
    private javax.swing.JTextField txtNACIMIENTO;
    private javax.swing.JTextField txtNOMBREUSERDEFECTO;
    private javax.swing.JTextField txtPISOS;
    private javax.swing.JTextField txtUBICACION;
    // End of variables declaration//GEN-END:variables
}
