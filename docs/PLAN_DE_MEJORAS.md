# Plan de mejoras

Este documento recoge la revisión del proyecto y el plan de mejoras de lógica funcional y de experiencia de usuario (UX), organizado en fases. Las fases 0 a 4 están completadas; las fases 5 a 8 contienen las mejoras recomendadas pendientes, ordenadas por prioridad.

## Resumen del estado inicial

La revisión del código original detectó los siguientes problemas principales:

| Área | Problema | Gravedad |
|------|----------|----------|
| Seguridad | Contraseña de aplicación de Gmail y contraseña del usuario `sa` escritas en el código | Crítica |
| Seguridad | Todas las consultas concatenaban texto del usuario (inyección SQL, incluido el inicio de sesión) | Crítica |
| Seguridad | Contraseñas guardadas, comparadas e impresas en consola en texto plano | Crítica |
| Lógica | Registrar una venta fallaba siempre (columnas incorrectas en cabecera y detalle) | Alta |
| Lógica | Actualizar y eliminar productos generaban SQL inválido | Alta |
| Lógica | Actualizar una cabecera de venta no tenía `WHERE` (habría modificado todas) | Alta |
| Lógica | Un fallo de conexión llevaba a crear una base de datos nueva | Alta |
| Lógica | Reportes con rutas a `src/`: no funcionaban fuera del IDE | Alta |
| Lógica | Catálogos (tipos de plato, producto e insumo) solo existían al cargar datos de prueba | Alta |
| Lógica | Identificadores de catálogos escritos en el código y distintos a los de la base de datos | Media |
| Lógica | Conexiones sin cerrar y errores ocultos con `printStackTrace` | Media |
| Lógica | Sin transacciones en operaciones de varias tablas | Media |
| UX | Sin validación de números ni fechas (excepciones al escribir texto) | Media |
| UX | Eliminaciones sin confirmación; selección de tablas solo con ratón | Media |
| UX | Visor de reportes sin barra de título (no se podía mover ni redimensionar) | Media |
| UX | Avisos innecesarios ("Conexión exitosa", "MODO ...") en cada inicio | Baja |
| Mantenimiento | Proyecto dependiente de librerías globales del IDE y JAR copiados a mano | Media |
| Licencias | Dependencia de iText 5 (AGPL), incompatible con publicar bajo MIT | Media |

## Fase 0. Base del repositorio (completada)

- Repositorio Git con `.gitignore` y `.gitattributes`; las credenciales se retiraron antes del primer commit.
- Migración de NetBeans/Ant a Maven con Maven Wrapper y dependencias de Maven Central.
- Paquetes y clases renombrados según las convenciones de Java; recursos sin espacios ni tildes en sus nombres.

## Fase 1. Seguridad y acceso a datos (completada)

- Configuración externa (`config/app.properties`, variables de entorno) sin credenciales en el código.
- Capa `restaurante.datos`: conexiones cerradas siempre, consultas parametrizadas, transacciones y errores traducidos a mensajes claros.
- Reescritura de todos los DAO, eliminación de código muerto y corrección de las consultas erróneas.
- Contraseñas con PBKDF2 y sal por usuario, migración transparente de las contraseñas antiguas y política de contraseñas.
- Esquema versionado en `schema.sql`, instalación transaccional y migraciones idempotentes en cada arranque.
- Sustitución de iText 5 (AGPL) y de javax.mail por Jakarta Mail.

## Fase 2. Lógica funcional (completada)

- Mantenimientos (áreas, roles, productos, insumos, contratos, usuarios): validaciones, selección por identificador, confirmación al eliminar y corrección de los estados de los botones.
- Ventas: transacción única, mesa obligatoria solo al comer en el local, dirección obligatoria en delivery, acumulación de productos repetidos, total visible y precio aplicado guardado en el detalle.
- Usuarios: no se puede eliminar el propio usuario ni el último administrador; cambio de contraseña al editar.
- Reportes cargados desde el classpath, compilados una vez por ejecución y generados en segundo plano.

## Fase 3. Experiencia de usuario (completada)

- Aspecto moderno con FlatLaf, contraste corregido en botones oscuros y textos revisados (tildes, encabezados legibles).
- Escritorio principal: menús por perfil, formularios sin duplicados y centrados, menú Sesión con cierre de sesión y salida con confirmación.
- Inicio de sesión con Intro, local precargado si solo hay uno y autenticación en segundo plano.
- Tablas de solo lectura, ordenables y navegables con teclado; búsqueda diferida mientras se escribe.
- Visor de reportes redimensionable con Guardar PDF y Enviar por correo; diálogo de correo único con adjuntos.
- Plantillas de reportes con fechas y estados legibles.

## Fase 4. Calidad (completada)

- Pruebas unitarias con JUnit 5 de la lógica que no depende de la base de datos (contraseñas, validaciones, configuración, scripts SQL, traducción de errores).
- Verificación manual de todos los DAO, la instalación, las migraciones, los 13 reportes y el flujo de venta contra SQL Server 2022.

## Fase 5. Lógica funcional pendiente (prioridad alta)

1. **Gestión de empleados.** Hoy los empleados solo se crean en la configuración inicial o con los datos de ejemplo, aunque contratos y usuarios dependen de ellos. Añadir un mantenimiento de empleados (alta, edición y baja lógica).
2. **Importes con `DECIMAL`.** Precios y sueldos se guardan como `float`, que introduce errores de redondeo en dinero. Migrar a `decimal(10,2)` y usar `BigDecimal` en Java.
3. **Control de existencias.** Descontar la cantidad vendida del producto al registrar una venta (en la misma transacción) y avisar cuando no haya existencias suficientes.
4. **Anulación de ventas.** Permitir anular una venta con motivo y usuario, en lugar de borrarla, para conservar el historial.
5. **Gestión de mesas y clientes.** Mantenimiento de mesas (alta, capacidad, estado libre u ocupada) y edición de clientes.
6. **Comprobante de venta.** Generar un comprobante en PDF al registrar la venta.

## Fase 6. Lógica funcional pendiente (prioridad media)

1. **Recetas e insumos.** Las tablas `CabReceta` y `DetReceta` existen pero no tienen interfaz: permitirían calcular el consumo de insumos por producto vendido.
2. **Órdenes de producción y reservas.** Las tablas `CabOrdenDeProduccionSemanal`, `DetOrdenDeProduccionSemanal` y `Reserva` tampoco se usan.
3. **Auditoría.** Rellenar los campos `usuarioCreaID`, `usuarioModifiID`, `fechCrea` y `fechModif` con el usuario de la sesión.
4. **Identificadores generados por la base de datos.** Sustituir el cálculo `MAX + 1` por `IDENTITY` o `SEQUENCE`.
5. **Reportes de ventas.** Ventas por día, por empleado y por producto; mostrar descripciones en lugar de códigos en los reportes que aún los usan (plato y tipo de producto, área en el reporte por rol).
6. **Teléfono como texto.** Guardar el teléfono del cliente como `varchar` para admitir prefijos y ceros iniciales.

## Fase 7. Experiencia de usuario pendiente

1. **Imágenes propias o con licencia libre.** Varias imágenes de `src/main/resources/imagenes` proceden de bancos de imágenes y webs de terceros. Antes de publicar el repositorio conviene sustituirlas por iconos propios o con licencia compatible (por ejemplo, iconos SVG con licencia Apache 2.0), ya que la licencia MIT del proyecto no puede cubrir material ajeno.
2. **Selector de fecha.** Sustituir los campos de fecha de texto por un calendario.
3. **Formularios adaptables.** Rediseñar los formularios con layouts que se adapten al tamaño de la ventana y unificar la posición de los botones (Guardar, Limpiar, Eliminar, Cerrar).
4. **Panel de inicio.** Mostrar al entrar un resumen del día (ventas, total facturado, productos más vendidos).
5. **Tema oscuro opcional** con FlatLaf y preferencia guardada por usuario.
6. **Barra de estado** con usuario, local y estado de la conexión.

## Fase 8. Arquitectura y distribución

1. **Capa de servicios** entre la interfaz y los DAO, para que la lógica de negocio no dependa de los formularios y se pueda probar de forma aislada.
2. **Pruebas de integración** contra SQL Server con Testcontainers (requiere Docker) para los DAO y las migraciones.
3. **Nombres coherentes** en DAO y modelos (por ejemplo, `ListaItem`, `setTipUsuaruario`) y uso de `record` para datos inmutables.
4. **Registro en archivo** configurable (`java.util.logging`) para diagnosticar errores en producción.
5. **Instalador nativo** con `jpackage` (incluido en el JDK) para distribuir la aplicación sin requerir Java instalado.
6. **Verificación antes de publicar cambios** con un hook local de Git (`pre-push`) que ejecute `mvnw verify`, sin depender de servicios de integración continua de pago.
