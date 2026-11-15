package restaurante.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabOrdenDeProduccionSemanal;
import restaurante.modelo.DetOrdenDeProduccionSemanal;

/** Órdenes de producción de la cocina. */
public class ProduccionDao {

    private static final String SELECT = "SELECT o.ordenProduccionID, o.empleadoID, o.descripArea,"
            + " CONVERT(varchar(10), o.fechaxSem, 23) AS fecha, o.estado,"
            + " CONVERT(varchar(16), o.fechaCompletada, 120) AS fechaCompletada,"
            + " e.nombres + ' ' + e.apellidos AS empleado"
            + " FROM CabOrdenDeProduccionSemanal o LEFT JOIN Empleado e ON e.empleadoID = o.empleadoID";

    public List<CabOrdenDeProduccionSemanal> listar() {
        return Jdbc.lista(SELECT + " ORDER BY o.estado, o.fechaxSem DESC, o.ordenProduccionID DESC",
                ProduccionDao::mapear);
    }

    public Optional<CabOrdenDeProduccionSemanal> buscar(Connection c, int ordenID) throws SQLException {
        return Jdbc.uno(c, SELECT + " WHERE o.ordenProduccionID = ?", ProduccionDao::mapear, ordenID);
    }

    /** Bloquea la orden durante la transacción para que no se complete dos veces a la vez. */
    public void bloquear(Connection c, int ordenID) throws SQLException {
        Jdbc.uno(c, "SELECT 1 FROM CabOrdenDeProduccionSemanal WITH (UPDLOCK, HOLDLOCK) WHERE ordenProduccionID = ?",
                rs -> true, ordenID);
    }

    public List<DetOrdenDeProduccionSemanal> listarDetalle(int ordenID) {
        return Jdbc.transaccion(c -> listarDetalle(c, ordenID));
    }

    public List<DetOrdenDeProduccionSemanal> listarDetalle(Connection c, int ordenID) throws SQLException {
        return Jdbc.lista(c, "SELECT d.ordenProduccionID, d.productoID, d.cantidad, d.estado, p.descripProducto"
                + " FROM DetOrdenDeProduccionSemanal d INNER JOIN Producto p ON p.productoID = d.productoID"
                + " WHERE d.ordenProduccionID = ? ORDER BY p.descripProducto", rs -> {
                    DetOrdenDeProduccionSemanal d = new DetOrdenDeProduccionSemanal(rs.getInt("productoID"),
                            rs.getInt("cantidad"));
                    d.setOrdenProduccionID(rs.getInt("ordenProduccionID"));
                    d.setEstado(rs.getInt("estado"));
                    d.setDescripProducto(rs.getString("descripProducto"));
                    return d;
                }, ordenID);
    }

    public int insertarCabecera(Connection c, CabOrdenDeProduccionSemanal o) throws SQLException {
        int id = Jdbc.siguienteId(c, "CabOrdenDeProduccionSemanal", "ordenProduccionID");
        Jdbc.actualizar(c, "INSERT INTO CabOrdenDeProduccionSemanal (ordenProduccionID, empleadoID, descripArea,"
                + " fechaxSem, estado) VALUES (?, ?, ?, ?, ?)", id, o.getEmpleadoID() == 0 ? null : o.getEmpleadoID(),
                o.getDescripArea(), Date.valueOf(o.getFechaxsem()), CabOrdenDeProduccionSemanal.PENDIENTE);
        o.setOrdenProduccionID(id);
        return id;
    }

    public void insertarDetalle(Connection c, int ordenID, DetOrdenDeProduccionSemanal d) throws SQLException {
        Jdbc.actualizar(c, "INSERT INTO DetOrdenDeProduccionSemanal (ordenProduccionID, productoID, cantidad, estado)"
                + " VALUES (?, ?, ?, ?)", ordenID, d.getProductoID(), d.getCantidad(), CabOrdenDeProduccionSemanal.PENDIENTE);
    }

    public void marcarCompletada(Connection c, int ordenID) throws SQLException {
        Jdbc.actualizar(c, "UPDATE CabOrdenDeProduccionSemanal SET estado = ?, fechaCompletada = GETDATE()"
                + " WHERE ordenProduccionID = ?", CabOrdenDeProduccionSemanal.COMPLETADA, ordenID);
        Jdbc.actualizar(c, "UPDATE DetOrdenDeProduccionSemanal SET estado = ? WHERE ordenProduccionID = ?",
                CabOrdenDeProduccionSemanal.COMPLETADA, ordenID);
    }

    public void eliminar(Connection c, int ordenID) throws SQLException {
        Jdbc.actualizar(c, "DELETE FROM DetOrdenDeProduccionSemanal WHERE ordenProduccionID = ?", ordenID);
        Jdbc.actualizar(c, "DELETE FROM CabOrdenDeProduccionSemanal WHERE ordenProduccionID = ?", ordenID);
    }

    private static CabOrdenDeProduccionSemanal mapear(java.sql.ResultSet rs) throws SQLException {
        CabOrdenDeProduccionSemanal o = new CabOrdenDeProduccionSemanal();
        o.setOrdenProduccionID(rs.getInt("ordenProduccionID"));
        o.setEmpleadoID(rs.getInt("empleadoID"));
        o.setDescripArea(rs.getString("descripArea"));
        o.setFechaxsem(rs.getString("fecha"));
        o.setEstado(rs.getInt("estado"));
        o.setFechaCompletada(rs.getString("fechaCompletada"));
        o.setEmpleado(rs.getString("empleado"));
        return o;
    }
}
