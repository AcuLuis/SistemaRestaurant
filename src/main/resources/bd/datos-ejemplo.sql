-- =============================================================================
-- Sistema Restaurant - Datos de ejemplo
--
-- Datos ficticios para probar la aplicación. Se cargan desde el menú
-- Administrador > "Cargar datos de ejemplo" y solo en una base de datos sin
-- datos de negocio. ${idLocal} se sustituye por el local del usuario conectado.
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
    (403, 'Personal de Limpieza')
GO

INSERT INTO Area (areaID, descripArea) VALUES
    (500, 'Recepción'),
    (501, 'Atención al Cliente'),
    (502, 'Preparación'),
    (503, 'Limpieza')
GO

INSERT INTO Producto (productoID, idPlato, idTipProducto, descripProducto, cantidad, precioUnit) VALUES
    (50, 30, 41, 'Tequeños', 10, 12.50),
    (51, 31, 41, 'Lomo saltado', 15, 28.00),
    (52, 32, 41, 'Sopa criolla', 12, 18.00),
    (53, 30, 40, 'Agua mineral', 40, 3.50)
GO

INSERT INTO Insumo (insumoID, idTipInsumo, unidadMedida, descripInsumo, precioUnit, cantidad) VALUES
    (20, 10, 'kg', 'Pollo', 10.50, 15),
    (21, 10, 'kg', 'Pescado', 18.00, 8),
    (22, 10, 'kg', 'Embutidos', 22.00, 5),
    (23, 11, 'lata', 'Atún enlatado', 6.20, 30),
    (24, 11, 'kg', 'Arroz', 4.10, 50),
    (25, 11, 'kg', 'Frijol', 7.30, 20)
GO

INSERT INTO Mesa (mesaID, ubic, numSillas, estado) VALUES
    (1, 'Salón - Mesa 1', 4, 0),
    (2, 'Salón - Mesa 2', 4, 0),
    (3, 'Salón - Mesa 3', 2, 0),
    (4, 'Terraza - Mesa 4', 6, 0),
    (5, 'Terraza - Mesa 5', 6, 0)
GO

INSERT INTO Empleado (empleadoID, usuarioModifiID, usuarioCreaID, nombres, apellidos, fechaNacimiento, fechCrea,
                      genero, fechModif, dni, estadoCivil) VALUES
    (1002, 1001, 1001, 'Ana', 'Torres Ruiz', '1995-03-14', '2024-01-08', 0, '2024-01-08', '40000001', 'SOLTERO'),
    (1003, 1001, 1001, 'Bruno', 'Díaz Campos', '1990-07-22', '2024-01-08', 1, '2024-01-08', '40000002', 'CASADO'),
    (1004, 1001, 1001, 'Carla', 'Rojas Vega', '1998-11-02', '2024-01-08', 0, '2024-01-08', '40000003', 'SOLTERO'),
    (1005, 1001, 1001, 'Diego', 'Salas Mora', '1993-05-30', '2024-01-08', 1, '2024-01-08', '40000004', 'SOLTERO'),
    (1006, 1001, 1001, 'Elena', 'Quispe León', '1997-09-18', '2024-01-08', 0, '2024-01-08', '40000005', 'SOLTERO'),
    (1007, 1001, 1001, 'Fernando', 'Huamán Paz', '1988-12-04', '2024-01-08', 1, '2024-01-08', '40000006', 'DIVORCIADO')
GO

INSERT INTO Usuario (usuarioID, idLocalR, usuario, contraseña, tipoUsuario, empleadoID) VALUES
    (102, ${idLocal}, 'demo.admin',         'pbkdf2$310000$qsiK1CUqqtyEHP1kZ1YgCA==$i8u/Dv9eeMF3oCoI6xlZEYKzxO55ie1e9eqDKs3loC0=', 1, 1002),
    (103, ${idLocal}, 'demo.mantenimiento', 'pbkdf2$310000$V/Jn/ScTrJuG8qF3tABbaQ==$PF7i3WueHVH0Nvtn1xDaa5OAJstvk5pdIP0On5+CTls=', 2, 1003),
    (104, ${idLocal}, 'demo.reportes',      'pbkdf2$310000$6dGTOEm52GC8UobCyf+vEg==$SWSBvk0OIOO21ux+v3CcEe1gLJUp0UHN6kA2IvXNN9g=', 3, 1004),
    (105, ${idLocal}, 'demo.ventas',        'pbkdf2$310000$3PDly8UraZCoahWs7ChG1A==$K/Tw9smg+FPHpUomem5NcG+sDFifvv+612kEk5XIPrs=', 4, 1005)
GO

INSERT INTO Contrato (contratoID, empleadoID, rolID, areaID, fechaIni, fechaFin, estado, sueldo) VALUES
    (1, 1002, 400, 500, '2024-01-08', '2026-12-31', 1, 1800),
    (2, 1003, 401, 501, '2024-01-08', '2026-12-31', 1, 1500),
    (3, 1004, 402, 502, '2024-01-08', '2026-12-31', 1, 2200),
    (4, 1005, 403, 503, '2024-01-08', '2025-12-31', 0, 1300)
GO

INSERT INTO Cliente (clienteID, nombres, apellidos, dni, genero, estadoCivil, telefono, correo, fechNaci, estado,
                     direccion) VALUES
    (90, 'Lucía', 'Fernández Soto', '70000001', 0, 'SOLTERO', '987000001', 'lucia@example.com', '2001-04-10', 1,
         'Av. Los Olivos 123'),
    (91, 'Marco', 'Paredes Luna', '70000002', 1, 'CASADO', '987000002', 'marco@example.com', '1987-08-25', 1,
         'Jr. Las Flores 456')
GO

INSERT INTO CabReceta (recetaID, empleadoID, productoID, fechaElab, preparacion) VALUES
    (1, 1004, 50, '2024-01-08', 'Rellenar la masa con queso, freír y servir con salsa.'),
    (2, 1004, 51, '2024-01-08', 'Saltear la carne con cebolla y tomate; servir con arroz.'),
    (3, 1004, 52, '2024-01-08', 'Cocer el pollo con verduras y fideos.')
GO

INSERT INTO DetReceta (recetaID, insumoID, cantidad) VALUES
    (1, 22, 0.050),
    (2, 24, 0.150),
    (2, 22, 0.100),
    (3, 20, 0.120),
    (3, 25, 0.050)
GO

INSERT INTO CabOrdenDeProduccionSemanal (ordenProduccionID, empleadoID, descripArea, fechaxSem, estado) VALUES
    (1, 1004, 'Preparación', CAST(GETDATE() AS date), 0)
GO

INSERT INTO DetOrdenDeProduccionSemanal (ordenProduccionID, productoID, cantidad, estado) VALUES
    (1, 51, 10, 0),
    (1, 52, 8, 0)
GO

INSERT INTO Reserva (reservaID, empleadoID, clienteID, fecha, estado, hora, personas, mesaID, observacion) VALUES
    (1, 1002, 90, CAST(DATEADD(day, 1, GETDATE()) AS date), 1, '20:00', 4, 4, 'Cumpleaños'),
    (2, 1002, 91, CAST(GETDATE() AS date), 1, '13:30', 2, 1, NULL)
GO
