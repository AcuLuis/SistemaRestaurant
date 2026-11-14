package restaurante.datos;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import restaurante.modelo.Empleado;
import restaurante.modelo.LocalR;
import restaurante.seguridad.Contrasenas;
import restaurante.seguridad.TipoUsuario;

/**
 * Crea la base de datos de la aplicación y carga los datos de ejemplo.
 */
public final class InstaladorBD {

    private static final Logger LOG = Logger.getLogger(InstaladorBD.class.getName());

    /** Identificador del empleado y del usuario administrador creados en la instalación. */
    public static final int ID_EMPLEADO_ADMIN = 1001;
    public static final int ID_USUARIO_ADMIN = 101;

    private static final List<String> TABLAS_DE_NEGOCIO = List.of(
            "Rol", "Area", "Producto", "Insumo", "Mesa", "Cliente", "Contrato", "CabOrdenDeVenta");

    private InstaladorBD() {
    }

    /**
     * Crea la base de datos, su esquema y el usuario administrador inicial.
     * Si algo falla después de crear la base de datos, se elimina para que la
     * instalación pueda repetirse desde cero.
     */
    public static void crearBaseDatos(Empleado admin, LocalR local, String usuario, char[] contrasena) {
        String error = Contrasenas.validarNueva(contrasena);
        if (error != null) {
            throw new DatosException(error);
        }
        String nombre = ConexionBD.nombreBaseDatos();
        try (Connection c = ConexionBD.abrirServidor(); Statement st = c.createStatement()) {
            st.execute("CREATE DATABASE [" + nombre + "]");
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "No se pudo crear la base de datos", e);
            throw new DatosException("No se pudo crear la base de datos: " + e.getMessage(), e);
        }
        try {
            String hash = Contrasenas.hash(contrasena);
            Jdbc.transaccion(c -> {
                ScriptSql.ejecutar(c, ScriptSql.leerRecurso("/bd/schema.sql"));
                Date hoy = Date.valueOf(LocalDate.now());
                Jdbc.actualizar(c, "INSERT INTO Empleado (empleadoID, usuarioModifiID, usuarioCreaID, nombres, apellidos,"
                        + " fechaNacimiento, fechCrea, genero, fechModif, dni, estadoCivil)"
                        + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                        ID_EMPLEADO_ADMIN, ID_EMPLEADO_ADMIN, ID_EMPLEADO_ADMIN, admin.getNombres(),
                        admin.getApellidos(), Date.valueOf(admin.getFechaNacimiento()), hoy, admin.getGenero(), hoy,
                        admin.getDni(), admin.getEstadoCivil());
                Jdbc.actualizar(c, "INSERT INTO LocalR (idLocalR, Ubicacion, capcidadMax, pisos, horaApertura, horaCerrado)"
                        + " VALUES (?, ?, ?, ?, NULL, NULL)",
                        local.getIdLocalR(), local.getUbicacion(), local.getCapacidadMax(), local.getPisos());
                Jdbc.actualizar(c, "INSERT INTO Usuario (usuarioID, idLocalR, usuario, contraseña, tipoUsuario, empleadoID)"
                        + " VALUES (?, ?, ?, ?, ?, ?)",
                        ID_USUARIO_ADMIN, local.getIdLocalR(), usuario, hash, TipoUsuario.ADMINISTRADOR.getCodigo(),
                        ID_EMPLEADO_ADMIN);
                MigradorBD.aplicar(c);
                return null;
            });
        } catch (RuntimeException e) {
            eliminarBaseDatos(nombre);
            throw e;
        }
    }

    /** Indica si la base de datos ya contiene datos de negocio. */
    public static boolean tieneDatos() {
        return Jdbc.transaccion(c -> {
            for (String tabla : TABLAS_DE_NEGOCIO) {
                if (Jdbc.uno(c, "SELECT TOP 1 1 FROM " + tabla, rs -> true).isPresent()) {
                    return true;
                }
            }
            return false;
        });
    }

    /**
     * Carga los datos de ejemplo en una base de datos vacía (todo o nada).
     *
     * @param idLocal local al que se asignan los usuarios de demostración
     */
    public static void cargarDatosEjemplo(int idLocal) {
        if (tieneDatos()) {
            throw new DatosException("La base de datos ya contiene información. Los datos de ejemplo solo se "
                    + "pueden cargar en una base de datos recién creada.");
        }
        String script = ScriptSql.sustituir(ScriptSql.leerRecurso("/bd/datos-ejemplo.sql"),
                Map.of("idLocal", Integer.toString(idLocal)));
        Jdbc.transaccion(c -> {
            ScriptSql.ejecutar(c, script);
            // Los datos de ejemplo usan identificadores fijos: se adelantan las secuencias.
            MigradorBD.aplicar(c);
            return null;
        });
    }

    private static void eliminarBaseDatos(String nombre) {
        try (Connection c = ConexionBD.abrirServidor(); Statement st = c.createStatement()) {
            st.execute("ALTER DATABASE [" + nombre + "] SET SINGLE_USER WITH ROLLBACK IMMEDIATE");
            st.execute("DROP DATABASE [" + nombre + "]");
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "No se pudo eliminar la base de datos incompleta", e);
        }
    }
}
