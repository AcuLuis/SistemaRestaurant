package restaurante.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import restaurante.datos.DatosException;
import restaurante.datos.Jdbc;
import restaurante.modelo.Usuario;
import restaurante.seguridad.Contrasenas;
import restaurante.seguridad.Sesion;
import restaurante.seguridad.TipoUsuario;

public class UsuarioDao {

    private static final Logger LOG = Logger.getLogger(UsuarioDao.class.getName());

    private static final String SELECT = "SELECT usuarioID, idLocalR, usuario, tipoUsuario, empleadoID FROM Usuario";

    /** Hash de referencia para igualar el tiempo de respuesta cuando el usuario no existe. */
    private static final class HashFicticio {
        static final String VALOR = Contrasenas.hash("usuario-inexistente".toCharArray());
    }

    /**
     * Comprueba las credenciales y devuelve la sesión si son correctas.
     *
     * <p>Si la contraseña estaba guardada en texto plano (bases de datos
     * creadas con versiones anteriores) se sustituye por su hash.</p>
     */
    public Optional<Sesion> autenticar(String usuario, char[] contrasena, int idLocal) {
        Optional<Object[]> fila = Jdbc.uno("SELECT usuarioID, contraseña, tipoUsuario, empleadoID FROM Usuario"
                + " WHERE usuario = ? AND idLocalR = ?", rs -> new Object[] {
                    rs.getInt("usuarioID"), rs.getString("contraseña"), rs.getInt("tipoUsuario"),
                    Jdbc.enteroONulo(rs, "empleadoID")}, usuario, idLocal);
        if (fila.isEmpty()) {
            // Se calcula un hash igualmente para no revelar por el tiempo de respuesta si el usuario existe.
            Contrasenas.verificar(contrasena, HashFicticio.VALOR);
            return Optional.empty();
        }
        Object[] datos = fila.get();
        int usuarioID = (Integer) datos[0];
        String almacenada = (String) datos[1];
        if (!Contrasenas.verificar(contrasena, almacenada)) {
            return Optional.empty();
        }
        if (Contrasenas.requiereActualizacion(almacenada)) {
            try {
                cambiarContrasena(usuarioID, contrasena);
            } catch (DatosException e) {
                LOG.log(Level.WARNING, "No se pudo actualizar el hash de la contraseña", e);
            }
        }
        return Optional.of(new Sesion(usuarioID, usuario, TipoUsuario.deCodigo((Integer) datos[2]),
                (Integer) datos[3], idLocal));
    }

    /** Lista de usuarios. Nunca incluye las contraseñas. */
    public List<Usuario> listaUsuario(boolean filtrar, String texto) {
        Jdbc.Mapeador<Usuario> mapeador = rs -> {
            Usuario u = new Usuario();
            u.setUsuarioID(rs.getInt("usuarioID"));
            u.setIdLocalR(rs.getInt("idLocalR"));
            u.setUsuario(rs.getString("usuario"));
            u.setTipUsuaruario(rs.getInt("tipoUsuario"));
            u.setEmpleadoID(rs.getInt("empleadoID"));
            return u;
        };
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY usuario", mapeador);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE CAST(usuarioID AS varchar(11)) LIKE ? OR usuario LIKE ? ORDER BY usuario",
                mapeador, patron, patron);
    }

    /** Crea el usuario guardando el hash de la contraseña y devuelve su identificador. */
    public int insertaUsuario(Usuario user, char[] contrasena) {
        String error = Contrasenas.validarNueva(contrasena);
        if (error != null) {
            throw new DatosException(error);
        }
        String hash = Contrasenas.hash(contrasena);
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Usuario", "usuarioID");
            Jdbc.actualizar(c, "INSERT INTO Usuario (usuarioID, idLocalR, usuario, contraseña, tipoUsuario, empleadoID)"
                    + " VALUES (?, ?, ?, ?, ?, ?)",
                    id, user.getIdLocalR(), user.getUsuario(), hash, user.getTipUsuaruario(), user.getEmpleadoID());
            user.setUsuarioID(id);
            return id;
        });
    }

    /** Actualiza los datos del usuario (no la contraseña). */
    public void actualizarUsuario(Usuario user) {
        Jdbc.transaccion(c -> {
            if (user.getTipUsuaruario() != TipoUsuario.ADMINISTRADOR.getCodigo()
                    && esAdministrador(c, user.getUsuarioID()) && contarAdministradores(c) <= 1) {
                throw new DatosException("Debe existir al menos un usuario Administrador.");
            }
            Jdbc.actualizar(c, "UPDATE Usuario SET idLocalR = ?, usuario = ?, tipoUsuario = ?, empleadoID = ?"
                    + " WHERE usuarioID = ?", user.getIdLocalR(), user.getUsuario(), user.getTipUsuaruario(),
                    user.getEmpleadoID(), user.getUsuarioID());
            return null;
        });
    }

    public int siguienteId() {
        return Jdbc.siguienteIdSugerido("Usuario", "usuarioID");
    }

    public void cambiarContrasena(int usuarioID, char[] nueva) {
        Jdbc.actualizar("UPDATE Usuario SET contraseña = ? WHERE usuarioID = ?", Contrasenas.hash(nueva), usuarioID);
    }

    public void eliminarUsuario(Usuario user) {
        Optional<Sesion> sesion = Sesion.actual();
        if (sesion.isPresent() && sesion.get().getUsuarioID() == user.getUsuarioID()) {
            throw new DatosException("No puede eliminar el usuario con el que ha iniciado sesión.");
        }
        Jdbc.transaccion(c -> {
            if (esAdministrador(c, user.getUsuarioID()) && contarAdministradores(c) <= 1) {
                throw new DatosException("No se puede eliminar el último usuario Administrador.");
            }
            Jdbc.actualizar(c, "DELETE FROM Usuario WHERE usuarioID = ?", user.getUsuarioID());
            return null;
        });
    }

    private static boolean esAdministrador(Connection c, int usuarioID) throws SQLException {
        return Jdbc.uno(c, "SELECT tipoUsuario FROM Usuario WHERE usuarioID = ?", rs -> rs.getInt(1), usuarioID)
                .map(t -> t == TipoUsuario.ADMINISTRADOR.getCodigo()).orElse(false);
    }

    private static int contarAdministradores(Connection c) throws SQLException {
        return Jdbc.uno(c, "SELECT COUNT(*) FROM Usuario WITH (UPDLOCK, HOLDLOCK) WHERE tipoUsuario = ?",
                rs -> rs.getInt(1), TipoUsuario.ADMINISTRADOR.getCodigo()).orElse(0);
    }
}
