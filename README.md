# Sistema Restaurant

Aplicación de escritorio para la gestión de un restaurante, desarrollada en Java (Swing) con SQL Server como base de datos y JasperReports para los reportes.

Permite administrar el personal y sus contratos, los productos de la carta y los insumos, registrar ventas, consultar el historial de compras de cada cliente y generar reportes que se pueden guardar en PDF o enviar por correo.

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

- **Configuración inicial guiada:** si la base de datos no existe, la aplicación la crea junto con el local y el usuario administrador.
- **Inicio de sesión por perfiles:** cada usuario solo ve los menús de su perfil.
- **Mantenimiento:** áreas, roles, productos, insumos y contratos del personal, con búsqueda, validación de datos y confirmación antes de eliminar.
- **Usuarios:** alta de usuarios para los empleados, cambio de contraseña y protección para no dejar el sistema sin administradores.
- **Ventas:** registro de ventas para comer en el local, para llevar o delivery, con carrito de productos, total y método de pago. Cada venta se guarda en una única transacción.
- **Historial:** compras por cliente con el detalle y el total de cada venta.
- **Inventario:** consulta de existencias de insumos y productos.
- **Reportes:** listados, reportes por parámetros (fechas, área, rol, rango de precios) y tablas cruzadas. Se pueden guardar en PDF o enviar por correo.
- **Datos de ejemplo:** carga opcional de datos ficticios para probar la aplicación.

## Tecnologías

| Componente | Tecnología |
|------------|------------|
| Lenguaje | Java 17 |
| Interfaz | Swing con [FlatLaf](https://www.formdev.com/flatlaf/) (formularios diseñados con el GUI Builder de NetBeans) |
| Base de datos | Microsoft SQL Server (driver JDBC oficial) |
| Reportes | JasperReports 6 |
| Correo | Jakarta Mail (Eclipse Angus) |
| Construcción | Maven (incluye Maven Wrapper) |
| Pruebas | JUnit 5 |

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

   El archivo `config/app.properties` está excluido de Git porque contiene credenciales. Cualquier clave también puede definirse con una variable de entorno con el prefijo `RESTAURANTE_` (por ejemplo, `RESTAURANTE_DB_PASSWORD`) o indicando otro archivo con `-Drestaurante.config=<ruta>`.

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

### Envío de correos (opcional)

Para enviar reportes por correo, complete las claves `smtp.*` en `config/app.properties`. Con Gmail es necesario activar la verificación en dos pasos y usar una [contraseña de aplicación](https://support.google.com/accounts/answer/185833).

### Redes sociales (opcional)

Las claves `redes.facebook`, `redes.instagram` y `redes.twitter` definen los enlaces del menú **Redes**. Si se dejan vacías, el menú no se muestra.

## Primer uso

1. Al pulsar **Entrar al sistema**, la aplicación comprueba la conexión:
   - Si el servidor no responde, muestra el motivo y el archivo de configuración utilizado.
   - Si la base de datos no existe, ofrece crearla.
2. En la **configuración inicial** indique el código del local, el nombre de usuario y la contraseña del administrador (mínimo 8 caracteres con letras y números) y sus datos personales.
3. Inicie sesión con el código del local, el usuario y la contraseña.
4. Opcionalmente, en **Administrador > Cargar datos de ejemplo** puede cargar datos ficticios. Se crean estos usuarios de demostración, todos con la contraseña `Demo2024`:

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

## Estructura del proyecto

```
SistemaRestaurant/
├── config/
│   └── app.properties.example     Plantilla de configuración
├── docs/
│   └── PLAN_DE_MEJORAS.md         Plan de mejoras por fases
├── src/main/java/restaurante/
│   ├── App.java                   Punto de entrada
│   ├── config/                    Lectura de la configuración
│   ├── datos/                     Conexión, utilidades JDBC, instalación y migraciones
│   ├── dao/                       Acceso a datos por entidad
│   ├── modelo/                    Clases de datos (empleado, producto, venta...)
│   ├── seguridad/                 Contraseñas, perfiles y sesión
│   ├── reportes/                  Generación y visor de reportes
│   ├── correo/                    Envío de correos
│   └── ui/                        Formularios Swing (.java + .form de NetBeans)
│       └── util/                  Diálogos, validaciones, tablas y estilo
├── src/main/resources/
│   ├── bd/                        schema.sql y datos-ejemplo.sql
│   ├── reportes/                  Plantillas .jrxml
│   └── imagenes/                  Imágenes de la interfaz
└── src/test/java/                 Pruebas unitarias
```

## Base de datos

- El esquema completo está en [`src/main/resources/bd/schema.sql`](src/main/resources/bd/schema.sql). La aplicación lo ejecuta al crear la base de datos, pero también puede ejecutarse manualmente con `sqlcmd` o SQL Server Management Studio.
- Los datos de ejemplo están en [`src/main/resources/bd/datos-ejemplo.sql`](src/main/resources/bd/datos-ejemplo.sql).
- Al iniciar, la aplicación actualiza automáticamente las bases de datos creadas con versiones anteriores (por ejemplo, amplía la columna de contraseñas). Las contraseñas antiguas guardadas en texto plano se convierten en hash en el primer inicio de sesión de cada usuario.

## Desarrollo

- **IDE:** el proyecto es Maven estándar y se abre directamente en NetBeans, IntelliJ IDEA, Eclipse o VS Code. Los formularios conservan sus archivos `.form`, por lo que se pueden seguir editando con el GUI Builder de NetBeans.
- **Pruebas:** `./mvnw test` ejecuta las pruebas unitarias (no necesitan base de datos).
- **Compilación completa:** `./mvnw verify` compila, ejecuta las pruebas y genera el JAR ejecutable en `target/`.

## Seguridad

- Las credenciales no se guardan en el código ni en el repositorio.
- Todas las consultas usan parámetros (`PreparedStatement`), lo que evita la inyección SQL.
- Las contraseñas se almacenan con PBKDF2-HMAC-SHA256 y una sal aleatoria por usuario.
- Las operaciones que afectan a varias tablas, como registrar una venta, se ejecutan en una transacción.

## Hoja de ruta

El plan de mejoras por fases, con lo ya realizado y lo pendiente, está en [`docs/PLAN_DE_MEJORAS.md`](docs/PLAN_DE_MEJORAS.md).

## Apoya el proyecto

Si este proyecto te resultó útil o te gustó, puedes regalarle una ⭐ en GitHub. Ayuda a que más personas lo encuentren.

## Licencia

Este proyecto se distribuye bajo la licencia MIT. Consulte el archivo [LICENSE](LICENSE) para más detalles.

Copyright (c) 2023-2026 Luis Antonio Acuña Ayala.
