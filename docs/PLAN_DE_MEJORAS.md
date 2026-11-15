# Plan de mejoras

Este documento recoge la revisión del proyecto y el plan de mejoras de lógica funcional y de experiencia de usuario (UX), organizado en fases. Todas las fases están completadas; al final se indican posibles ampliaciones futuras.

## Resumen del estado inicial

| Área | Problema | Gravedad |
|------|----------|----------|
| Seguridad | Credenciales de correo y de base de datos escritas en el código | Crítica |
| Seguridad | Consultas construidas concatenando texto del usuario (inyección SQL, incluido el inicio de sesión) | Crítica |
| Seguridad | Contraseñas guardadas, comparadas e impresas en consola en texto plano | Crítica |
| Lógica | Registrar una venta fallaba siempre (columnas incorrectas en cabecera y detalle) | Alta |
| Lógica | Actualizar y eliminar productos generaban SQL inválido | Alta |
| Lógica | Actualizar una cabecera de venta no tenía `WHERE` | Alta |
| Lógica | Un fallo de conexión llevaba a crear una base de datos nueva | Alta |
| Lógica | Reportes con rutas a `src/`: no funcionaban fuera del IDE | Alta |
| Lógica | Catálogos básicos solo existían al cargar datos de prueba | Alta |
| Lógica | Sin gestión de empleados, mesas, recetas, producción ni reservas | Alta |
| Lógica | Importes en `float`, sin control de existencias ni anulación de ventas | Media |
| Lógica | Conexiones sin cerrar, errores ocultos y operaciones sin transacciones | Media |
| UX | Sin validación de números ni fechas; eliminaciones sin confirmación | Media |
| UX | Visor de reportes sin barra de título; avisos innecesarios al entrar | Media |
| Mantenimiento | Dependencia de librerías del IDE y de JAR copiados a mano | Media |
| Licencias | iText 5 (AGPL) e imágenes de terceros, incompatibles con publicar bajo MIT | Media |

## Fase 0. Base del repositorio (completada)

- Repositorio Git con reglas de finales de línea y exclusión de credenciales y artefactos.
- Migración de NetBeans/Ant a Maven con Maven Wrapper y dependencias de Maven Central.
- Paquetes, clases y métodos con nombres coherentes y convencionales.

## Fase 1. Seguridad y acceso a datos (completada)

- Configuración externa (`config/app.properties`, `~/.sistema-restaurant`, variables de entorno).
- Consultas parametrizadas, conexiones cerradas siempre, transacciones y errores con mensajes claros.
- Contraseñas con PBKDF2 y sal por usuario, con migración transparente de las antiguas.
- Esquema versionado, instalación transaccional y migraciones idempotentes en cada arranque.
- Sustitución de iText 5 y de javax.mail; imágenes de terceros sustituidas por iconos propios.

## Fase 2. Lógica funcional de los formularios (completada)

- Corrección de todos los mantenimientos originales: validaciones, selección por identificador, confirmación al eliminar.
- Ventas en una transacción con mesa, dirección y método de pago coherentes con el tipo de entrega.
- Reportes cargados desde el classpath y generados en segundo plano.

## Fase 3. Experiencia de usuario (completada)

- FlatLaf, contraste corregido y textos revisados.
- Menús por perfil, formularios sin duplicados, cierre de sesión, inicio de sesión con Intro.
- Tablas ordenables y navegables con teclado; búsqueda diferida.
- Visor de reportes con Guardar PDF y Enviar por correo.

## Fase 4. Calidad (completada)

- Pruebas unitarias de la lógica que no depende de la base de datos.

## Fase 5. Lógica funcional de negocio (completada)

1. **Empleados:** alta, edición, baja lógica, DNI único y auditoría (quién y cuándo creó o modificó).
2. **Importes en `DECIMAL`** y `BigDecimal` en la aplicación; cantidades de insumos con decimales.
3. **Control de existencias:** las ventas descuentan existencias en la misma transacción y se rechazan si no hay suficientes.
4. **Anulación de ventas** con motivo, usuario y fecha, reponiendo existencias.
5. **Mesas y clientes:** mantenimientos completos con estados.
6. **Comprobante de venta** en PDF.

## Fase 6. Funciones ampliadas (completada)

1. **Recetas** de cada producto.
2. **Órdenes de producción** que suman existencias y consumen insumos según las recetas.
3. **Reservas** con control de capacidad y de solapamientos.
4. **Auditoría** de empleados y de anulaciones.
5. **Identificadores con secuencias** de SQL Server, sincronizadas automáticamente.
6. **Reportes de ventas** por día, producto y empleado; descripciones en lugar de códigos en los reportes existentes.
7. **Teléfono como texto.**

## Fase 7. Experiencia de usuario ampliada (completada)

1. **Iconos y logotipo propios** generados por código, nítidos con escalado de pantalla.
2. **Selector de fecha** con calendario en todos los campos de fecha.
3. **Formularios adaptables:** base común para los mantenimientos nuevos (botones uniformes, diseño que se adapta al tamaño) y todas las ventanas redimensionables y maximizables.
4. **Panel de inicio** con el resumen del día por perfil.
5. **Tema oscuro** guardado por usuario.
6. **Barra de estado** con usuario, local, base de datos y hora.

## Fase 8. Arquitectura y distribución (completada)

1. **Capa de servicios** con las reglas de negocio de ventas, producción, recetas, reservas, empleados y el panel de inicio.
2. **Pruebas de integración** con Testcontainers contra SQL Server (instalación, migración desde el esquema original, reportes y flujos de negocio).
3. **Nombres coherentes** en DAO, modelos y clases.
4. **Registro en archivos rotativos** para diagnosticar errores.
5. **Aplicación nativa** con jpackage (perfil `instalador`).
6. **Verificación antes de publicar** con un hook `pre-push` versionado que ejecuta `mvnw verify`.

## Posibles ampliaciones futuras

Mejoras opcionales que no forman parte del alcance actual:

- Rediseñar en código los formularios originales para unificarlos por completo con la base de mantenimiento (hoy conservan su diseño de NetBeans, editable con el GUI Builder).
- Gestión de varios locales desde una misma instalación (cada usuario pertenece hoy a un local).
- Impresión directa del comprobante en impresoras térmicas de tickets.
- Copias de seguridad programadas de la base de datos desde la aplicación.
