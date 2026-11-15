-- =============================================================================
-- Sistema Restaurant - Datos de ejemplo
--
-- Datos ficticios para ver y probar la aplicación: catálogo de productos e
-- insumos con recetas, mesas, clientes, personal con contratos, reservas,
-- órdenes de producción y un historial de ventas de las últimas semanas
-- (incluidas ventas del día y dos anuladas).
--
-- Se cargan desde el menú Administrador > "Cargar datos de ejemplo" y solo en
-- una base de datos sin datos de negocio. ${idLocal} se sustituye por el local
-- del usuario conectado. Las fechas se calculan respecto al día de la carga.
--
-- Usuarios de demostración (contraseña: Demo2024):
--   demo.admin (Administrador), demo.mantenimiento (Mantenimiento),
--   demo.reportes (Reportes) y demo.ventas (Transacciones).
-- No utilice estas cuentas en un entorno real.
-- =============================================================================

INSERT INTO Rol (rolID, descripRol) VALUES
    (400, 'Recepcionista'),
    (401, 'Mesero'),
    (402, 'Cocinero'),
    (403, 'Personal de Limpieza'),
    (404, 'Cajero'),
    (405, 'Ayudante de Cocina')
GO

INSERT INTO Area (areaID, descripArea) VALUES
    (500, 'Recepción'),
    (501, 'Atención al Cliente'),
    (502, 'Preparación'),
    (503, 'Limpieza')
GO

-- Tipos de plato: 30 = entrada, 31 = menú, 32 = sopa. Tipos de producto: 40 = envasado, 41 = preparado.
INSERT INTO Producto (productoID, idPlato, idTipProducto, descripProducto, cantidad, precioUnit) VALUES
    (50, 30, 41, 'Tequeños', 10, 12.50),
    (51, 31, 41, 'Lomo saltado', 15, 28.00),
    (52, 32, 41, 'Sopa criolla', 12, 18.00),
    (53, 30, 40, 'Agua mineral', 40, 3.50),
    (54, 30, 41, 'Ceviche de pescado', 14, 32.00),
    (55, 30, 41, 'Papa a la huancaína', 18, 15.00),
    (56, 31, 41, 'Ají de gallina', 16, 26.00),
    (57, 31, 41, 'Arroz con pollo', 20, 25.00),
    (58, 31, 41, 'Tallarines verdes', 3, 24.00),
    (59, 32, 41, 'Chupe de camarones', 8, 34.00),
    (60, 32, 41, 'Sopa de quinua', 10, 16.00),
    (61, 30, 40, 'Chicha morada 1 L', 25, 9.00),
    (62, 30, 40, 'Gaseosa 500 ml', 60, 4.50),
    (63, 30, 40, 'Limonada 1 L', 4, 10.00)
GO

INSERT INTO Insumo (insumoID, idTipInsumo, unidadMedida, descripInsumo, precioUnit, cantidad) VALUES
    (20, 10, 'kg', 'Pollo', 10.50, 15),
    (21, 10, 'kg', 'Pescado', 18.00, 8),
    (22, 10, 'kg', 'Embutidos', 22.00, 5),
    (23, 11, 'lata', 'Atún enlatado', 6.20, 30),
    (24, 11, 'kg', 'Arroz', 4.10, 50),
    (25, 11, 'kg', 'Frijol', 7.30, 20),
    (26, 10, 'kg', 'Papa amarilla', 3.80, 25),
    (27, 10, 'kg', 'Ají amarillo', 9.50, 1.5),
    (28, 10, 'kg', 'Cebolla roja', 2.90, 12),
    (29, 10, 'kg', 'Limón', 4.50, 6),
    (30, 10, 'kg', 'Queso fresco', 21.00, 4),
    (31, 11, 'lata', 'Leche evaporada', 4.20, 36),
    (32, 11, 'kg', 'Fideos', 5.60, 15),
    (33, 10, 'kg', 'Camarones', 45.00, 1.8),
    (34, 11, 'kg', 'Quinua', 12.00, 10)
GO

INSERT INTO Mesa (mesaID, ubic, numSillas, estado) VALUES
    (1, 'Salón - Mesa 1', 4, 0),
    (2, 'Salón - Mesa 2', 4, 0),
    (3, 'Salón - Mesa 3', 2, 0),
    (4, 'Terraza - Mesa 4', 6, 0),
    (5, 'Terraza - Mesa 5', 6, 0),
    (6, 'Salón - Mesa 6', 8, 0),
    (7, 'Terraza - Mesa 7', 2, 0),
    (8, 'Barra - Mesa 8', 2, 1)
GO

-- genero: 1 = masculino, 0 = femenino. estado: 1 = activo, 0 = de baja.
INSERT INTO Empleado (empleadoID, usuarioModifiID, usuarioCreaID, nombres, apellidos, fechaNacimiento, fechCrea,
                      genero, fechModif, dni, estadoCivil, estado) VALUES
    (1002, 101, 101, 'Ana', 'Torres Ruiz', '1995-03-14', '2024-01-08', 0, '2024-01-08', '40000001', 'SOLTERO', 1),
    (1003, 101, 101, 'Bruno', 'Díaz Campos', '1990-07-22', '2024-01-08', 1, '2024-01-08', '40000002', 'CASADO', 1),
    (1004, 101, 101, 'Carla', 'Rojas Vega', '1998-11-02', '2024-01-08', 0, '2024-01-08', '40000003', 'SOLTERO', 1),
    (1005, 101, 101, 'Diego', 'Salas Mora', '1993-05-30', '2024-01-08', 1, '2024-01-08', '40000004', 'SOLTERO', 1),
    (1006, 101, 101, 'Elena', 'Quispe León', '1997-09-18', '2024-01-08', 0, '2024-01-08', '40000005', 'SOLTERO', 1),
    (1007, 101, 101, 'Fernando', 'Huamán Paz', '1988-12-04', '2024-01-08', 1, '2024-01-08', '40000006', 'DIVORCIADO', 1),
    (1008, 101, 101, 'Gabriela', 'Mendoza Ríos', '1992-02-11', '2024-03-01', 0, '2024-03-01', '40000007', 'CASADO', 1),
    (1009, 101, 101, 'Héctor', 'Castillo Núñez', '1985-06-25', '2024-03-01', 1, '2025-02-28', '40000008', 'CASADO', 0),
    (1010, 101, 101, 'Isabel', 'Chávez Romero', '2000-10-09', '2024-06-15', 0, '2024-06-15', '40000009', 'SOLTERO', 1),
    (1011, 101, 101, 'Jorge', 'Ramírez Soto', '1999-04-17', '2025-01-10', 1, '2025-01-10', '40000010', 'SOLTERO', 1)
GO

INSERT INTO Usuario (usuarioID, idLocalR, usuario, contraseña, tipoUsuario, empleadoID) VALUES
    (102, ${idLocal}, 'demo.admin',         'pbkdf2$310000$qsiK1CUqqtyEHP1kZ1YgCA==$i8u/Dv9eeMF3oCoI6xlZEYKzxO55ie1e9eqDKs3loC0=', 1, 1002),
    (103, ${idLocal}, 'demo.mantenimiento', 'pbkdf2$310000$V/Jn/ScTrJuG8qF3tABbaQ==$PF7i3WueHVH0Nvtn1xDaa5OAJstvk5pdIP0On5+CTls=', 2, 1003),
    (104, ${idLocal}, 'demo.reportes',      'pbkdf2$310000$6dGTOEm52GC8UobCyf+vEg==$SWSBvk0OIOO21ux+v3CcEe1gLJUp0UHN6kA2IvXNN9g=', 3, 1004),
    (105, ${idLocal}, 'demo.ventas',        'pbkdf2$310000$3PDly8UraZCoahWs7ChG1A==$K/Tw9smg+FPHpUomem5NcG+sDFifvv+612kEk5XIPrs=', 4, 1005)
GO

-- estado: 1 = activo, 0 = no activo. Los empleados 1007 y 1011 quedan sin contrato para poder probar el alta.
INSERT INTO Contrato (contratoID, empleadoID, rolID, areaID, fechaIni, fechaFin, estado, sueldo) VALUES
    (1, 1002, 400, 500, '2024-01-08', '2026-12-31', 1, 1800),
    (2, 1003, 401, 501, '2024-01-08', '2026-12-31', 1, 1500),
    (3, 1004, 402, 502, '2024-01-08', '2026-12-31', 1, 2200),
    (4, 1005, 403, 503, '2024-01-08', '2025-12-31', 0, 1300),
    (5, 1006, 401, 501, '2024-02-01', '2026-12-31', 1, 1500),
    (6, 1008, 402, 502, '2024-03-01', '2027-02-28', 1, 2400),
    (7, 1009, 405, 502, '2024-03-01', '2025-02-28', 0, 1350),
    (8, 1010, 404, 500, '2024-06-15', '2026-06-14', 1, 1650)
GO

-- estado: 1 = activo, 0 = inactivo.
INSERT INTO Cliente (clienteID, nombres, apellidos, dni, genero, estadoCivil, telefono, correo, fechNaci, estado,
                     direccion) VALUES
    (90, 'Lucía', 'Fernández Soto', '70000001', 0, 'SOLTERO', '987000001', 'lucia@example.com', '2001-04-10', 1,
         'Av. Los Olivos 123'),
    (91, 'Marco', 'Paredes Luna', '70000002', 1, 'CASADO', '987000002', 'marco@example.com', '1987-08-25', 1,
         'Jr. Las Flores 456'),
    (92, 'Natalia', 'Vargas Ortiz', '70000003', 0, 'CASADO', '987000003', 'natalia@example.com', '1990-01-19', 1,
         'Calle Los Pinos 789'),
    (93, 'Óscar', 'Gutiérrez Silva', '70000004', 1, 'SOLTERO', '987000004', NULL, '1995-12-03', 1,
         'Av. Primavera 1020'),
    (94, 'Patricia', 'Medina Cruz', NULL, 0, NULL, '987000005', 'patricia@example.com', NULL, 1, 'Jr. Ayacucho 315'),
    (95, 'Ricardo', 'Flores Aguilar', '70000006', 1, 'CASADO', '+51987000006', NULL, '1982-05-28', 1,
         'Av. Arequipa 2450'),
    (96, 'Sofía', 'Navarro Peña', '70000007', 0, 'SOLTERO', NULL, 'sofia@example.com', '2003-09-14', 1, NULL),
    (97, 'Tomás', 'Herrera Lozano', '70000008', 1, 'SOLTERO', '987000008', NULL, '1998-07-07', 1,
         'Calle Las Gardenias 88'),
    (98, 'Úrsula', 'Campos Ibáñez', '70000009', 0, 'DIVORCIADO', '987000009', 'ursula@example.com', '1979-11-21', 1,
         'Av. La Marina 560'),
    (99, 'Víctor', 'Morales Ponce', '70000010', 1, 'CASADO', '987000010', NULL, '1985-03-02', 1,
         'Jr. Junín 742'),
    (100, 'Wendy', 'Espinoza Reyes', '70000011', 0, 'SOLTERO', '987000011', 'wendy@example.com', '1999-06-30', 1,
         'Av. Brasil 1300'),
    (101, 'Xavier', 'Ruiz Delgado', '70000012', 1, 'SOLTERO', '987000012', NULL, '1993-08-16', 0,
         'Calle Lima 100')
GO

-- Insumos necesarios para preparar una unidad de cada producto.
INSERT INTO CabReceta (recetaID, empleadoID, productoID, fechaElab, preparacion) VALUES
    (1, 1004, 50, '2024-01-08', 'Rellenar la masa con queso, freír y servir con salsa.'),
    (2, 1004, 51, '2024-01-08', 'Saltear la carne con cebolla y tomate; servir con arroz.'),
    (3, 1004, 52, '2024-01-08', 'Cocer el pollo con verduras y fideos.'),
    (4, 1008, 54, '2024-03-05', 'Cortar el pescado en cubos y marinar con limón, cebolla y ají.'),
    (5, 1008, 55, '2024-03-05', 'Licuar queso, ají y leche; servir sobre papa sancochada.'),
    (6, 1008, 56, '2024-03-05', 'Deshilachar el pollo y cocinar en crema de ají; acompañar con arroz.'),
    (7, 1004, 57, '2024-03-05', 'Cocinar el arroz con culantro y presas de pollo.'),
    (8, 1004, 58, '2024-03-05', 'Mezclar los fideos con salsa de albahaca y queso.'),
    (9, 1008, 59, '2024-03-05', 'Preparar el caldo con camarones, papa y leche.'),
    (10, 1004, 60, '2024-03-05', 'Cocer la quinua con verduras y papa.')
GO

INSERT INTO DetReceta (recetaID, insumoID, cantidad) VALUES
    (1, 22, 0.050),
    (2, 24, 0.150),
    (2, 22, 0.100),
    (3, 20, 0.120),
    (3, 25, 0.050),
    (4, 21, 0.200),
    (4, 29, 0.080),
    (4, 28, 0.050),
    (5, 26, 0.250),
    (5, 30, 0.080),
    (5, 27, 0.030),
    (5, 31, 0.250),
    (6, 20, 0.150),
    (6, 27, 0.040),
    (6, 31, 0.250),
    (6, 24, 0.120),
    (7, 20, 0.200),
    (7, 24, 0.150),
    (8, 32, 0.150),
    (8, 30, 0.050),
    (9, 33, 0.150),
    (9, 26, 0.100),
    (9, 31, 0.250),
    (10, 34, 0.080),
    (10, 26, 0.100)
GO

-- Órdenes de producción: la 1 está pendiente; las demás ya se completaron.
INSERT INTO CabOrdenDeProduccionSemanal (ordenProduccionID, empleadoID, descripArea, fechaxSem, estado, fechaCompletada) VALUES
    (1, 1004, 'Preparación', CAST(GETDATE() AS date), 0, NULL),
    (2, 1008, 'Preparación', CAST(DATEADD(day, -7, GETDATE()) AS date), 1, DATEADD(day, -7, GETDATE())),
    (3, 1004, 'Preparación', CAST(DATEADD(day, -14, GETDATE()) AS date), 1, DATEADD(day, -14, GETDATE()))
GO

INSERT INTO DetOrdenDeProduccionSemanal (ordenProduccionID, productoID, cantidad, estado) VALUES
    (1, 51, 10, 0),
    (1, 52, 8, 0),
    (2, 54, 15, 1),
    (2, 56, 12, 1),
    (2, 57, 20, 1),
    (3, 55, 18, 1),
    (3, 58, 10, 1),
    (3, 59, 8, 1)
GO

-- estado: 1 = pendiente, 2 = atendida, 0 = cancelada.
INSERT INTO Reserva (reservaID, empleadoID, clienteID, fecha, estado, hora, personas, mesaID, observacion) VALUES
    (1, 1002, 90, CAST(DATEADD(day, 1, GETDATE()) AS date), 1, '20:00', 4, 4, 'Cumpleaños'),
    (2, 1002, 91, CAST(GETDATE() AS date), 1, '13:30', 2, 1, NULL),
    (3, 1002, 93, CAST(GETDATE() AS date), 1, '20:30', 6, 6, 'Mesa cerca de la ventana'),
    (4, 1010, 95, CAST(DATEADD(day, 2, GETDATE()) AS date), 1, '19:00', 2, 3, NULL),
    (5, 1002, 92, CAST(DATEADD(day, -1, GETDATE()) AS date), 2, '21:00', 5, 5, 'Aniversario'),
    (6, 1010, 98, CAST(DATEADD(day, -3, GETDATE()) AS date), 0, '14:00', 3, 2, 'Canceló por teléfono')
GO

-- Historial de ventas: tres ventas diarias durante las últimas cuatro semanas
-- y varias ventas del día. Las ventas 17 y 53 están anuladas.
DECLARE @i int = 1, @j int, @lineas int, @producto int, @cantidad int, @fecha datetime, @tipo varchar(15),
        @mesa int, @cliente int, @empleado int, @ubicacion varchar(100), @estado int;
WHILE @i <= 92
BEGIN
    IF @i <= 84
        SET @fecha = DATEADD(minute, 720 + (@i * 47) % 540,
                             CAST(CAST(DATEADD(day, -(30 - (@i - 1) / 3), GETDATE()) AS date) AS datetime));
    ELSE
        SET @fecha = DATEADD(minute, -20 * (93 - @i), GETDATE());
    SET @tipo = CASE @i % 3 WHEN 0 THEN 'Delivery' WHEN 1 THEN 'ComerEnLocal' ELSE 'ParaLlevar' END;
    SET @mesa = CASE WHEN @tipo = 'ComerEnLocal' THEN 1 + @i % 7 ELSE NULL END;
    SET @cliente = 90 + @i % 11;
    SET @empleado = CASE @i % 4 WHEN 0 THEN 1002 WHEN 1 THEN 1003 WHEN 2 THEN 1005 ELSE 1006 END;
    SET @estado = CASE WHEN @i IN (17, 53) THEN 0 ELSE 1 END;
    SET @ubicacion = CASE @tipo
                         WHEN 'ComerEnLocal' THEN (SELECT ubic FROM Mesa WHERE mesaID = @mesa)
                         WHEN 'Delivery' THEN (SELECT direccion FROM Cliente WHERE clienteID = @cliente)
                         ELSE 'Para llevar' END;

    INSERT INTO CabOrdenDeVenta (ordenDeVentaID, empleadoID, clienteID, mesaID, fechaVenta, metodoPago, tipEntrega,
                                 estado, motivoAnulacion, usuarioAnulaID, fechaAnulacion)
    VALUES (@i, @empleado, @cliente, @mesa, @fecha, CASE WHEN @i % 4 = 0 THEN 0 ELSE 1 END, @tipo, @estado,
            CASE WHEN @estado = 0 THEN 'Pedido registrado por error' END,
            CASE WHEN @estado = 0 THEN 102 END,
            CASE WHEN @estado = 0 THEN DATEADD(minute, 10, @fecha) END);
    INSERT INTO Pedidos (idPedidos, empleadoID, ubic, estado) VALUES (@i, @empleado, @ubicacion, 1);

    SET @lineas = 1 + @i % 3;
    SET @j = 0;
    WHILE @j < @lineas
    BEGIN
        SET @producto = 50 + (@i * 5 + @j * 3) % 14;
        SET @cantidad = 1 + (@i + @j) % 3;
        INSERT INTO DetOrdenDeVenta (ordenDeVentaID, productoID, idPedidos, cantidad, precioUnit)
        SELECT @i, @producto, @i, @cantidad, precioUnit FROM Producto WHERE productoID = @producto;
        SET @j = @j + 1;
    END;
    SET @i = @i + 1;
END;
GO
