package restaurante.datos;

import java.sql.SQLException;

/**
 * Error de acceso a datos con un mensaje comprensible para el usuario.
 */
public class DatosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /** Violación de clave foránea o restricción REFERENCE. */
    private static final int SQL_REFERENCIA = 547;
    /** Violación de clave primaria o restricción UNIQUE. */
    private static final int SQL_DUPLICADO_PK = 2627;
    /** Violación de índice único. */
    private static final int SQL_DUPLICADO_INDICE = 2601;
    /** Cadena o dato binario truncado. */
    private static final int SQL_TRUNCADO = 8152;
    private static final int SQL_TRUNCADO_2019 = 2628;

    private final String mensajeUsuario;

    public DatosException(String mensajeUsuario, Throwable causa) {
        super(mensajeUsuario, causa);
        this.mensajeUsuario = mensajeUsuario;
    }

    public DatosException(String mensajeUsuario) {
        super(mensajeUsuario);
        this.mensajeUsuario = mensajeUsuario;
    }

    /** Traduce una {@link SQLException} a un mensaje entendible. */
    public static DatosException de(SQLException e) {
        return new DatosException(traducir(e), e);
    }

    static String traducir(SQLException e) {
        switch (e.getErrorCode()) {
            case SQL_REFERENCIA:
                return "No se puede completar la operación porque el registro está relacionado "
                        + "con otros datos (por ejemplo, contratos, ventas o usuarios que lo usan).";
            case SQL_DUPLICADO_PK:
            case SQL_DUPLICADO_INDICE:
                return "Ya existe un registro con ese identificador o valor.";
            case SQL_TRUNCADO:
            case SQL_TRUNCADO_2019:
                return "Alguno de los textos es demasiado largo para guardarse.";
            default:
                break;
        }
        String estado = e.getSQLState();
        if (estado != null && estado.startsWith("08")) {
            return "No se pudo conectar con la base de datos. Compruebe que el servidor está "
                    + "disponible y la configuración en config/app.properties.";
        }
        return "Se produjo un error al acceder a la base de datos: " + e.getMessage();
    }

    public String getMensajeUsuario() {
        return mensajeUsuario;
    }
}
