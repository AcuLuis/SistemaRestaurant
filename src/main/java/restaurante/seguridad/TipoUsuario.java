package restaurante.seguridad;

/**
 * Perfiles de usuario y los módulos del menú a los que tienen acceso.
 */
public enum TipoUsuario {

    ADMINISTRADOR(1, "Administrador"),
    MANTENIMIENTO(2, "Mantenimiento"),
    REPORTES(3, "Reportes"),
    TRANSACCIONES(4, "Transacciones");

    private final int codigo;
    private final String descripcion;

    TipoUsuario(int codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /** Devuelve el tipo correspondiente al código; los códigos desconocidos no tienen permisos amplios. */
    public static TipoUsuario deCodigo(int codigo) {
        for (TipoUsuario t : values()) {
            if (t.codigo == codigo) {
                return t;
            }
        }
        return TRANSACCIONES;
    }

    public static TipoUsuario deDescripcion(String descripcion) {
        for (TipoUsuario t : values()) {
            if (t.descripcion.equalsIgnoreCase(descripcion)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Tipo de usuario desconocido: " + descripcion);
    }

    public boolean puedeAdministrar() {
        return this == ADMINISTRADOR;
    }

    public boolean puedeMantener() {
        return this == ADMINISTRADOR || this == MANTENIMIENTO;
    }

    public boolean puedeVerReportes() {
        return this == ADMINISTRADOR || this == REPORTES;
    }

    public boolean puedeRegistrarTransacciones() {
        return this == ADMINISTRADOR || this == TRANSACCIONES;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
