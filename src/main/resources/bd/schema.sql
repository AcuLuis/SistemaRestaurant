-- =============================================================================
-- Sistema Restaurant - Esquema de la base de datos (SQL Server)
--
-- La aplicación ejecuta este script al crear la base de datos desde la
-- pantalla de configuración inicial. También puede ejecutarse a mano, por
-- ejemplo con sqlcmd o SQL Server Management Studio, sobre una base de datos
-- vacía. Cada lote termina con una línea "GO".
-- =============================================================================

CREATE TABLE TipInsumo (
    idTipInsumo  int          CONSTRAINT pkIdTipInsumo PRIMARY KEY,
    descripcion  varchar(25)  NOT NULL
)
GO

CREATE TABLE TipProducto (
    idTipProducto  int          CONSTRAINT pkIdTipProducto PRIMARY KEY,
    descripcion    varchar(15)  NOT NULL
)
GO

CREATE TABLE Plato (
    idPlato    int          CONSTRAINT pkIdPlato PRIMARY KEY,
    descPlato  varchar(30)  NOT NULL
)
GO

CREATE TABLE Area (
    areaID       int          CONSTRAINT pkareaID PRIMARY KEY,
    descripArea  varchar(40)  NOT NULL
)
GO

CREATE TABLE Rol (
    rolID       int          CONSTRAINT pkRolID PRIMARY KEY,
    descripRol  varchar(25)  NOT NULL
)
GO

CREATE TABLE LocalR (
    idLocalR      int          CONSTRAINT pkIdLocalR PRIMARY KEY,
    Ubicacion     varchar(30),
    capcidadMax   int,
    pisos         int,
    horaApertura  time,
    horaCerrado   time
)
GO

CREATE TABLE Empleado (
    empleadoID       int          CONSTRAINT pkEmpleadoID PRIMARY KEY,
    usuarioModifiID  int,
    usuarioCreaID    int,
    nombres          varchar(40)  NOT NULL,
    apellidos        varchar(40)  NOT NULL,
    fechaNacimiento  date,
    fechCrea         date,
    genero           int,
    fechModif        date,
    dni              varchar(8),
    estadoCivil      varchar(10),
    -- 1 = activo, 0 = de baja (los empleados no se eliminan para conservar el historial)
    estado           int          NOT NULL CONSTRAINT dfEmpleadoEstado DEFAULT 1
)
GO

CREATE TABLE Cliente (
    clienteID    int           CONSTRAINT pkClienteID PRIMARY KEY,
    nombres      varchar(40)   NOT NULL,
    apellidos    varchar(40)   NOT NULL,
    dni          varchar(8),
    genero       int,
    estadoCivil  varchar(10),
    telefono     varchar(15),
    correo       varchar(100),
    fechNaci     date,
    estado       int,
    direccion    varchar(100)
)
GO

CREATE TABLE Mesa (
    mesaID     int          CONSTRAINT pkMesaID PRIMARY KEY,
    ubic       varchar(30)  NOT NULL,
    numSillas  int,
    -- 0 = disponible, 1 = fuera de servicio
    estado     int          NOT NULL CONSTRAINT dfMesaEstado DEFAULT 0
)
GO

CREATE TABLE Insumo (
    insumoID       int          CONSTRAINT pkInsumoID PRIMARY KEY,
    idTipInsumo    int          NOT NULL CONSTRAINT fkIdTipInsumo REFERENCES TipInsumo (idTipInsumo),
    unidadMedida   varchar(10),
    descripInsumo  varchar(60)  NOT NULL,
    precioUnit     decimal(10, 2),
    cantidad       decimal(12, 3),
    CONSTRAINT ckInsumoCantidad CHECK (cantidad >= 0)
)
GO

CREATE TABLE Producto (
    productoID       int          CONSTRAINT pkProductoID PRIMARY KEY,
    idPlato          int          CONSTRAINT fkIdPlato REFERENCES Plato (idPlato),
    idTipProducto    int          CONSTRAINT fkIdTipProducto REFERENCES TipProducto (idTipProducto),
    descripProducto  varchar(40)  NOT NULL,
    cantidad         int,
    precioUnit       decimal(10, 2),
    CONSTRAINT ckProductoCantidad CHECK (cantidad >= 0)
)
GO

CREATE TABLE Contrato (
    contratoID  int    CONSTRAINT pkContratoID PRIMARY KEY,
    empleadoID  int    NOT NULL CONSTRAINT fk3EmpleadoID REFERENCES Empleado (empleadoID),
    rolID       int    NOT NULL CONSTRAINT fkRolID REFERENCES Rol (rolID),
    areaID      int    NOT NULL CONSTRAINT fkAreaID REFERENCES Area (areaID),
    fechaIni    date   NOT NULL,
    fechaFin    date   NOT NULL,
    estado      int    NOT NULL,
    sueldo      decimal(10, 2)  NOT NULL,
    CONSTRAINT ckContratoFechas CHECK (fechaFin >= fechaIni)
)
GO

-- La contraseña se guarda como hash PBKDF2 (nunca en texto plano).
CREATE TABLE Usuario (
    usuarioID    int           CONSTRAINT pkUsuarioID PRIMARY KEY,
    idLocalR     int           NOT NULL CONSTRAINT fkIdLocalR REFERENCES LocalR (idLocalR),
    usuario      varchar(25)   NOT NULL,
    contraseña   varchar(100)  NOT NULL,
    tipoUsuario  int           NOT NULL,
    empleadoID   int           CONSTRAINT fkUsuarioEmpleado REFERENCES Empleado (empleadoID),
    CONSTRAINT FK_Usuario_Empleado UNIQUE (empleadoID),
    CONSTRAINT uqUsuarioLocal UNIQUE (usuario, idLocalR)
)
GO

CREATE TABLE Pedidos (
    idPedidos   int          CONSTRAINT pkIdPedidos PRIMARY KEY,
    empleadoID  int          CONSTRAINT fk4EmpleadoID REFERENCES Empleado (empleadoID),
    ubic        varchar(100),
    estado      int
)
GO

CREATE TABLE CabOrdenDeVenta (
    ordenDeVentaID  int          CONSTRAINT pkOrdenDeVentaID PRIMARY KEY,
    empleadoID      int          CONSTRAINT fk1EmpleadoID REFERENCES Empleado (empleadoID),
    clienteID       int          CONSTRAINT fkClienteID REFERENCES Cliente (clienteID),
    mesaID          int          CONSTRAINT fkMesaID REFERENCES Mesa (mesaID),
    fechaVenta      datetime,
    metodoPago      int,
    tipEntrega      varchar(15),
    -- 1 = registrada, 0 = anulada
    estado          int           NOT NULL CONSTRAINT dfVentaEstado DEFAULT 1,
    motivoAnulacion varchar(200),
    usuarioAnulaID  int,
    fechaAnulacion  datetime
)
GO

-- precioUnit guarda el precio aplicado en el momento de la venta.
CREATE TABLE DetOrdenDeVenta (
    ordenDeVentaID  int    CONSTRAINT fkOrdenDeVentaID REFERENCES CabOrdenDeVenta (ordenDeVentaID),
    productoID      int    CONSTRAINT fk2ProductoID REFERENCES Producto (productoID),
    idPedidos       int    CONSTRAINT fkIdPedidos REFERENCES Pedidos (idPedidos),
    cantidad        int    NOT NULL,
    precioUnit      decimal(10, 2)
)
GO

CREATE TABLE CabReceta (
    recetaID     int           CONSTRAINT pkRecetaID PRIMARY KEY,
    empleadoID   int           CONSTRAINT fk2EmpleadoID REFERENCES Empleado (empleadoID),
    productoID   int           CONSTRAINT fkProductoID REFERENCES Producto (productoID),
    fechaElab    date,
    preparacion  varchar(300)
)
GO

CREATE TABLE DetReceta (
    recetaID  int  CONSTRAINT fkRecetaID REFERENCES CabReceta (recetaID),
    insumoID  int  CONSTRAINT fkInsumoID REFERENCES Insumo (insumoID),
    cantidad  decimal(12, 3)
)
GO

CREATE TABLE CabOrdenDeProduccionSemanal (
    ordenProduccionID  int          CONSTRAINT pkOrdenProduccionID PRIMARY KEY,
    empleadoID         int          CONSTRAINT fkEmpleadoID REFERENCES Empleado (empleadoID),
    descripArea        varchar(40),
    fechaxSem          date,
    -- 0 = pendiente, 1 = completada
    estado             int          NOT NULL CONSTRAINT dfProduccionEstado DEFAULT 0,
    fechaCompletada    datetime
)
GO

CREATE TABLE DetOrdenDeProduccionSemanal (
    ordenProduccionID  int  CONSTRAINT fkOrdenProduccionID
                            REFERENCES CabOrdenDeProduccionSemanal (ordenProduccionID),
    productoID         int  CONSTRAINT fk1ProductoID REFERENCES Producto (productoID),
    cantidad           int,
    estado             int
)
GO

CREATE TABLE Reserva (
    reservaID   int   CONSTRAINT pkReservaID PRIMARY KEY,
    empleadoID  int   CONSTRAINT fk5EmpleadoID REFERENCES Empleado (empleadoID),
    clienteID   int   CONSTRAINT fk1ClienteID REFERENCES Cliente (clienteID),
    fecha       date,
    -- 1 = pendiente, 2 = atendida, 0 = cancelada
    estado      int,
    hora        time,
    personas    int,
    mesaID      int           CONSTRAINT fkReservaMesa REFERENCES Mesa (mesaID),
    observacion varchar(200)
)
GO

-- Catálogos básicos necesarios para registrar productos e insumos.
INSERT INTO Plato (idPlato, descPlato) VALUES (30, 'entrada'), (31, 'menú'), (32, 'sopa')
GO
INSERT INTO TipProducto (idTipProducto, descripcion) VALUES (40, 'envasado'), (41, 'preparado')
GO
INSERT INTO TipInsumo (idTipInsumo, descripcion) VALUES (10, 'perecible'), (11, 'No perecible')
GO
