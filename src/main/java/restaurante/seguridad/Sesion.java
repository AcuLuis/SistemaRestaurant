package restaurante.seguridad;

import java.util.Objects;
import java.util.Optional;

/**
 * Usuario que ha iniciado sesión en la aplicación.
 */
public final class Sesion {

    private static volatile Sesion actual;

    private final int usuarioID;
    private final String usuario;
    private final TipoUsuario tipo;
    private final Integer empleadoID;
    private final int idLocal;

    public Sesion(int usuarioID, String usuario, TipoUsuario tipo, Integer empleadoID, int idLocal) {
        this.usuarioID = usuarioID;
        this.usuario = Objects.requireNonNull(usuario);
        this.tipo = Objects.requireNonNull(tipo);
        this.empleadoID = empleadoID;
        this.idLocal = idLocal;
    }

    public static void iniciar(Sesion sesion) {
        actual = Objects.requireNonNull(sesion);
    }

    public static void cerrar() {
        actual = null;
    }

    public static Optional<Sesion> actual() {
        return Optional.ofNullable(actual);
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public String getUsuario() {
        return usuario;
    }

    public TipoUsuario getTipo() {
        return tipo;
    }

    public Integer getEmpleadoID() {
        return empleadoID;
    }

    public int getIdLocal() {
        return idLocal;
    }
}
