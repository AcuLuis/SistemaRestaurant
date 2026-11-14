package restaurante.datos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Utilidades de JDBC.
 *
 * <p>Todas las consultas usan {@link PreparedStatement} con parámetros, nunca
 * concatenación de valores, para evitar inyección SQL. Los recursos se cierran
 * siempre con try-with-resources.</p>
 */
public final class Jdbc {

    private static final Logger LOG = Logger.getLogger(Jdbc.class.getName());

    /**
     * Tablas cuyo identificador genera la aplicación. Se usa como lista blanca
     * porque los nombres de tabla y columna no pueden enviarse como parámetros.
     */
    private static final Map<String, String> TABLAS_CON_ID = Stream.of(
            "Area.areaID", "Rol.rolID", "TipInsumo.idTipInsumo", "Insumo.insumoID",
            "Producto.productoID", "Cliente.clienteID", "Contrato.contratoID",
            "Empleado.empleadoID", "Usuario.usuarioID", "CabOrdenDeVenta.ordenDeVentaID",
            "Pedidos.idPedidos", "Mesa.mesaID", "LocalR.idLocalR")
            .collect(Collectors.toMap(t -> t.toLowerCase(Locale.ROOT), t -> t));

    /** Convierte la fila actual de un {@link ResultSet} en un objeto. */
    @FunctionalInterface
    public interface Mapeador<T> {
        T mapear(ResultSet rs) throws SQLException;
    }

    /** Trabajo que se ejecuta con una conexión (por ejemplo, dentro de una transacción). */
    @FunctionalInterface
    public interface Trabajo<T> {
        T ejecutar(Connection c) throws SQLException;
    }

    private Jdbc() {
    }

    public static <T> List<T> lista(String sql, Mapeador<T> mapeador, Object... parametros) {
        try (Connection c = ConexionBD.abrir()) {
            return lista(c, sql, mapeador, parametros);
        } catch (SQLException e) {
            throw error(e, sql);
        }
    }

    public static <T> Optional<T> uno(String sql, Mapeador<T> mapeador, Object... parametros) {
        try (Connection c = ConexionBD.abrir()) {
            return uno(c, sql, mapeador, parametros);
        } catch (SQLException e) {
            throw error(e, sql);
        }
    }

    public static int actualizar(String sql, Object... parametros) {
        try (Connection c = ConexionBD.abrir()) {
            return actualizar(c, sql, parametros);
        } catch (SQLException e) {
            throw error(e, sql);
        }
    }

    public static <T> List<T> lista(Connection c, String sql, Mapeador<T> mapeador, Object... parametros)
            throws SQLException {
        try (PreparedStatement ps = preparar(c, sql, parametros);
             ResultSet rs = ps.executeQuery()) {
            List<T> resultado = new ArrayList<>();
            while (rs.next()) {
                resultado.add(mapeador.mapear(rs));
            }
            return resultado;
        }
    }

    public static <T> Optional<T> uno(Connection c, String sql, Mapeador<T> mapeador, Object... parametros)
            throws SQLException {
        try (PreparedStatement ps = preparar(c, sql, parametros);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? Optional.ofNullable(mapeador.mapear(rs)) : Optional.empty();
        }
    }

    public static int actualizar(Connection c, String sql, Object... parametros) throws SQLException {
        try (PreparedStatement ps = preparar(c, sql, parametros)) {
            return ps.executeUpdate();
        }
    }

    /**
     * Ejecuta el trabajo dentro de una transacción: confirma si termina bien y
     * revierte todos los cambios si se produce cualquier error.
     */
    public static <T> T transaccion(Trabajo<T> trabajo) {
        try (Connection c = ConexionBD.abrir()) {
            c.setAutoCommit(false);
            try {
                T resultado = trabajo.ejecutar(c);
                c.commit();
                return resultado;
            } catch (SQLException | RuntimeException e) {
                revertir(c);
                throw e;
            }
        } catch (SQLException e) {
            throw error(e, "transacción");
        }
    }

    /**
     * Obtiene el siguiente identificador de una tabla. Debe llamarse dentro de
     * una transacción: los bloqueos UPDLOCK/HOLDLOCK impiden que dos usuarios
     * obtengan el mismo identificador a la vez.
     */
    public static int siguienteId(Connection c, String tabla, String columna) throws SQLException {
        String canonico = TABLAS_CON_ID.get((tabla + "." + columna).toLowerCase(Locale.ROOT));
        if (canonico == null) {
            throw new IllegalArgumentException("Tabla no admitida para generar identificadores: " + tabla);
        }
        String[] partes = canonico.split("[.]");
        String sql = "SELECT ISNULL(MAX(" + partes[1] + "), 0) + 1 FROM " + partes[0] + " WITH (UPDLOCK, HOLDLOCK)";
        return uno(c, sql, rs -> rs.getInt(1)).orElse(1);
    }

    /** Siguiente identificador (solo informativo, para mostrarlo en pantalla). */
    public static int siguienteIdSugerido(String tabla, String columna) {
        try (Connection c = ConexionBD.abrir()) {
            return siguienteId(c, tabla, columna);
        } catch (SQLException e) {
            throw error(e, tabla);
        }
    }

    /** Escapa los comodines de LIKE y añade '%' al final (búsqueda "empieza por"). */
    public static String empiezaPor(String texto) {
        String limpio = texto == null ? "" : texto.trim();
        return limpio.replace("[", "[[]").replace("%", "[%]").replace("_", "[_]") + "%";
    }

    /** Lee un entero que puede ser NULL en la base de datos. */
    public static Integer enteroONulo(ResultSet rs, String columna) throws SQLException {
        int valor = rs.getInt(columna);
        return rs.wasNull() ? null : valor;
    }

    private static PreparedStatement preparar(Connection c, String sql, Object... parametros) throws SQLException {
        PreparedStatement ps = c.prepareStatement(sql);
        try {
            for (int i = 0; i < parametros.length; i++) {
                ps.setObject(i + 1, parametros[i]);
            }
            return ps;
        } catch (SQLException e) {
            ps.close();
            throw e;
        }
    }

    private static void revertir(Connection c) {
        try {
            c.rollback();
        } catch (SQLException e) {
            LOG.log(Level.WARNING, "No se pudo revertir la transacción", e);
        }
    }

    private static DatosException error(SQLException e, String contexto) {
        LOG.log(Level.SEVERE, "Error de base de datos en: " + contexto, e);
        return DatosException.de(e);
    }
}
