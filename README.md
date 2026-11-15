# Sistema Restaurant

Aplicación de escritorio para la gestión integral de un restaurante, desarrollada en Java (Swing) con SQL Server como base de datos y JasperReports para los reportes.

Cubre el día a día del local: ventas con control de existencias y comprobante, reservas de mesa, producción de cocina con recetas e inventario de insumos, personal y contratos, clientes, y reportes que se pueden guardar en PDF o enviar por correo.

## Contenido

- [Funcionalidades](#funcionalidades)
- [Tecnologías](#tecnologías)
- [Requisitos](#requisitos)
- [Instalación](#instalación)
- [Primer uso](#primer-uso)
- [Perfiles de usuario](#perfiles-de-usuario)
- [Estructura del proyecto](#estructura-del-proyecto)
- [Base de datos](#base-de-datos)
- [Desarrollo](#desarrollo)
- [Seguridad](#seguridad)
- [Hoja de ruta](#hoja-de-ruta)
- [Apoya el proyecto](#apoya-el-proyecto)
- [Licencia](#licencia)

## Funcionalidades

**Operación diaria**
- **Panel de inicio:** ventas y total facturado del día, productos más vendidos, reservas pendientes, producción pendiente y avisos de existencias bajas.
- **Ventas:** comer en el local, para llevar o delivery, con carrito, total y método de pago. Las existencias se descuentan en la misma transacción y no se permite vender más de lo disponible. Se genera un comprobante en PDF.
- **Anulación de ventas:** con motivo, usuario y fecha; se reponen las existencias y la venta se conserva en el historial.
- **Reservas:** fecha, hora, personas y mesa, con control de capacidad y de reservas superpuestas.
- **Producción:** órdenes de cocina que, al completarse, suman unidades a los productos y descuentan los insumos según sus recetas.
- **Inventario:** existencias de productos e insumos.

**Mantenimiento**
- Productos, insumos (con cantidades decimales), recetas, mesas, clientes, empleados (con baja lógica y auditoría), contratos, áreas y roles.
- Usuarios del sistema con perfiles, cambio de contraseña y protección para no dejar el sistema sin administradores.

**Reportes**
- Listados y tablas cruzadas de contratos, productos, insumos, áreas, roles y usuarios.
- Reportes por parámetros: contratos por fechas, área o rol; productos e insumos por rango de precio; ventas por día, por producto o por empleado.
- Visor con **Guardar PDF** y **Enviar por correo**.

**Experiencia de uso**
- Aspecto moderno con [FlatLaf](https://www.formdev.com/flatlaf/), tema claro u oscuro por usuario e iconos propios nítidos en pantallas con escalado.
- Validación de todos los datos con mensajes claros, calendario en los campos de fecha, búsquedas mientras se escribe y tablas ordenables que se recorren con el teclado.
- Barra de estado con usuario, local y base de datos; menús según el perfil; cierre de sesión.
- Configuración inicial guiada que crea la base de datos si no existe.

## Tecnologías

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 17 |
| Interfaz | Swing con FlatLaf |
| Base de datos | Microsoft SQL Server (driver JDBC oficial) |
| Reportes | JasperReports 6 |
| Correo | Jakarta Mail (Eclipse Angus) |
| Construcción | Maven (incluye Maven Wrapper) |
| Pruebas | JUnit 5 y Testcontainers |
| Distribución | jpackage (aplicación nativa) |

## Requisitos

- **JDK 17** o superior.
- **SQL Server** 2017 o superior (la edición gratuita Express o Developer es suficiente) con autenticación de SQL Server habilitada.
- No es necesario instalar Maven: el proyecto incluye `mvnw` / `mvnw.cmd`.

Si no tiene SQL Server instalado, puede usar Docker:

```bash
docker run -e "ACCEPT_EULA=Y" -e "MSSQL_SA_PASSWORD=<una-contraseña-segura>" \
  -p 1433:1433 -d mcr.microsoft.com/mssql/server:2022-latest
```

## Instalación

1. Clone el repositorio:

   ```bash
   git clone <url-del-repositorio>
   cd SistemaRestaurant
   ```

2. Cree el archivo de configuración a partir de la plantilla y complete los datos de conexión:

   ```bash
   cp config/app.properties.example config/app.properties
   ```

   ```properties
   db.host=localhost
   db.port=1433
   db.name=BD_Restaurant
   db.user=sa
   db.password=<su-contraseña>
   ```

   La configuración se busca, por este orden, en la ruta indicada con `-Drestaurante.config=<ruta>`, en `config/app.properties` (carpeta desde la que se ejecuta) y en `~/.sistema-restaurant/app.properties`. Cualquier clave puede definirse también con una variable de entorno con el prefijo `RESTAURANTE_` (por ejemplo, `RESTAURANTE_DB_PASSWORD`). El archivo `config/app.properties` está excluido de Git porque contiene credenciales.

3. Compile y ejecute desde la carpeta del proyecto:

   ```bash
   # Linux / macOS
   ./mvnw package
   java -jar target/sistema-restaurant.jar

   # Windows
   mvnw.cmd package
   java -jar target\sistema-restaurant.jar
   ```

   También puede ejecutarla directamente con `./mvnw exec:java`.

### Aplicación nativa (sin Java instalado)

```bash
./mvnw -P instalador package
```

Genera en `target/instalador/SistemaRestaurant` una aplicación con su propio entorno de Java. Con `-Djpackage.tipo=msi` (o `exe`, `deb`, `rpm`, `dmg`) se genera un instalador; en Windows los instaladores requieren [WiX Toolset](https://wixtoolset.org/). Para la aplicación instalada, coloque la configuración en `~/.sistema-restaurant/app.properties`.

### Envío de correos (opcional)

Para enviar reportes por correo, complete las claves `smtp.*` del archivo de configuración. Con Gmail es necesario activar la verificación en dos pasos y usar una [contraseña de aplicación](https://support.google.com/accounts/answer/185833).

### Redes sociales (opcional)

Las claves `redes.facebook`, `redes.instagram` y `redes.twitter` definen los enlaces del menú **Redes**. Si se dejan vacías, el menú no se muestra.

## Primer uso

1. Al pulsar **Entrar al sistema**, la aplicación comprueba la conexión:
   - Si el servidor no responde, muestra el motivo y el archivo de configuración utilizado.
   - Si la base de datos no existe, ofrece crearla.
2. En la **configuración inicial** indique el código del local, el usuario y la contraseña del administrador (mínimo 8 caracteres con letras y números) y sus datos personales.
3. Inicie sesión con el código del local, el usuario y la contraseña.
4. Opcionalmente, en **Administrador > Cargar datos de ejemplo** puede cargar datos ficticios para ver y probar el sistema: 14 productos, 15 insumos con sus recetas, 8 mesas, 12 clientes, 10 empleados con contratos, reservas, órdenes de producción y un historial de unas 90 ventas de las últimas cuatro semanas (incluidas ventas del día, para el panel de inicio y los reportes). Se crean estos usuarios de demostración, todos con la contraseña `Demo2024`:

   | Usuario | Perfil |
   |---------|--------|
   | `demo.admin` | Administrador |
   | `demo.mantenimiento` | Mantenimiento |
   | `demo.reportes` | Reportes |
   | `demo.ventas` | Transacciones |

   No utilice estas cuentas en un entorno real.

## Perfiles de usuario

| Perfil | Administración | Mantenimiento | Reportes | Transacciones |
|--------|:--------------:|:-------------:|:--------:|:-------------:|
| Administrador | Sí | Sí | Sí | Sí |
| Mantenimiento | No | Sí | No | No |
| Reportes | No | No | Sí | No |
| Transacciones | No | No | No | Sí |

Mantenimiento incluye productos, insumos, recetas, mesas, clientes, empleados, contratos, áreas y roles. Transacciones incluye ventas, inventario, reservas y órdenes de producción.

## Estructura del proyecto

```
SistemaRestaurant/
├── config/
│   └── app.properties.example     Plantilla de configuración
├── docs/
│   └── PLAN_DE_MEJORAS.md         Plan de mejoras por fases
├── herramientas/
│   └── GeneradorIconos.java       Genera los iconos y el logotipo
├── src/main/java/restaurante/
│   ├── App.java                   Punto de entrada
│   ├── config/                    Lectura de la configuración
│   ├── datos/                     Conexión, JDBC, instalación y migraciones
│   ├── dao/                       Acceso a datos por entidad
│   ├── modelo/                    Clases de datos
│   ├── servicio/                  Reglas de negocio (ventas, producción, reservas...)
│   ├── seguridad/                 Contraseñas, perfiles y sesión
│   ├── reportes/                  Generación y visor de reportes
│   ├── correo/                    Envío de correos
│   ├── util/                      Validaciones y registro
│   └── ui/                        Interfaz Swing
│       ├── base/                  Base de los formularios de mantenimiento
│       ├── mantenimiento/         Empleados, clientes y mesas
│       ├── produccion/            Recetas y órdenes de producción
│       ├── transacciones/         Reservas
│       ├── reportes/              Reportes de ventas
│       └── util/                  Diálogos, tablas, calendario, iconos y estilo
├── src/main/resources/
│   ├── bd/                        schema.sql y datos-ejemplo.sql
│   ├── reportes/                  Plantillas .jrxml
│   └── iconos/                    Iconos y logotipo
├── src/test/java/                 Pruebas unitarias y de integración
└── .githooks/pre-push             Verificación antes de publicar
```

Los formularios originales conservan sus archivos `.form`, por lo que se pueden editar con el GUI Builder de NetBeans; los formularios nuevos están escritos directamente en código sobre una base común.

## Base de datos

- El esquema completo está en [`src/main/resources/bd/schema.sql`](src/main/resources/bd/schema.sql). La aplicación lo ejecuta al crear la base de datos, pero también puede ejecutarse manualmente con `sqlcmd` o SQL Server Management Studio.
- Los datos de ejemplo están en [`src/main/resources/bd/datos-ejemplo.sql`](src/main/resources/bd/datos-ejemplo.sql).
- Al iniciar, la aplicación actualiza automáticamente las bases de datos creadas con versiones anteriores (tipos de datos, columnas nuevas y secuencias). Las contraseñas antiguas guardadas en texto plano se convierten en hash en el primer inicio de sesión de cada usuario.
- Los identificadores se generan con secuencias de SQL Server y los importes se guardan como `DECIMAL`.

## Desarrollo

- **IDE:** el proyecto es Maven estándar y se abre directamente en NetBeans, IntelliJ IDEA, Eclipse o VS Code.
- **Pruebas unitarias:** `./mvnw test` (no necesitan base de datos).
- **Verificación completa:** `./mvnw verify` compila, ejecuta las pruebas unitarias y las de integración, y genera el JAR ejecutable. Las pruebas de integración arrancan un SQL Server desechable con Docker mediante Testcontainers; si Docker no está disponible, se omiten.
- **Antes de publicar:** active el hook incluido con `git config core.hooksPath .githooks`; ejecutará `mvnw verify` antes de cada `git push`.
- **Registro:** los mensajes y errores se guardan en `~/.sistema-restaurant/logs` (configurable con `-Drestaurante.logs=<carpeta>`).
- **Iconos:** se generan con `java herramientas/GeneradorIconos.java src/main/resources/iconos`.

## Seguridad

- Las credenciales no se guardan en el código ni en el repositorio.
- Todas las consultas usan parámetros (`PreparedStatement`), lo que evita la inyección SQL.
- Las contraseñas se almacenan con PBKDF2-HMAC-SHA256 y una sal aleatoria por usuario.
- Las operaciones que afectan a varias tablas (ventas, anulaciones, producción, instalación) se ejecutan en una transacción.
- Los empleados dados de baja no pueden iniciar sesión.

## Hoja de ruta

La revisión del proyecto y el plan de mejoras por fases están en [`docs/PLAN_DE_MEJORAS.md`](docs/PLAN_DE_MEJORAS.md).

## Apoya el proyecto

Si este proyecto te resultó útil o te gustó, puedes regalarle una ⭐ en GitHub. Ayuda a que más personas lo encuentren.

## Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulte el archivo [LICENSE](LICENSE) para más detalles.

Copyright (c) 2023-2026 Luis Antonio Acuña Ayala.
