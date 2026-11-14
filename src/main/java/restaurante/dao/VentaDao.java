package restaurante.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabOrdenDeVenta;
import restaurante.modelo.DetOrdenDeVenta;

/**
 * Acceso a las ventas (cabecera, pedido y detalle). Los métodos que reciben
 * una conexión se usan dentro de transacciones de VentaServicio.
 */
public class VentaDao {

    public static final int ESTADO_REGISTRADA = 1;
    public static final int ESTADO_ANULADA = 0;

    private static final String SELECT = "SELECT ordenDeVentaID, empleadoID, clienteID, mesaID,"
            + " CONVERT(varchar(19), fechaVenta, 120) AS fechaVenta, metodoPago, tipEntrega, estado, motivoAnulacion"
            + " FROM CabOrdenDeVenta";

    /** Ventas de un cliente, de la más reciente a la más antigua. */
    public List<CabOrdenDeVenta> listarPorCliente(int clienteID) {
        return Jdbc.lista(SELECT + " WHERE clienteID = ? ORDER BY fechaVenta DESC", VentaDao::mapear, clienteID);
    }

    public Optional<CabOrdenDeVenta> buscar(Connection c, int ordenDeVentaID) throws SQLException {
        return Jdbc.uno(c, SELECT + " WITH (UPDLOCK) WHERE ordenDeVentaID = ?", VentaDao::mapear, ordenDeVentaID);
    }

    public int insertarCabecera(Connection c, CabOrdenDeVenta cab) throws SQLException {
        int id = Jdbc.siguienteId(c, "CabOrdenDeVenta", "ordenDeVentaID");
        Jdbc.actualizar(c, "INSERT INTO CabOrdenDeVenta (ordenDeVentaID, empleadoID, clienteID, mesaID, fechaVenta,"
                + " metodoPago, tipEntrega, estado) VALUES (?, ?, ?, ?, GETDATE(), ?, ?, ?)",
                id, cab.getEmpleadoID(), cab.getClienteID(), cab.getMesaID() > 0 ? cab.getMesaID() : null,
                cab.getMetodoPago(), cab.getTipEntrega(), ESTADO_REGISTRADA);
        cab.setOrdenDeVentaID(id);
        return id;
    }

    public int insertarPedido(Connection c, Integer empleadoID, String ubicacion) throws SQLException {
        int id = Jdbc.siguienteId(c, "Pedidos", "idPedidos");
        Jdbc.actualizar(c, "INSERT INTO Pedidos (idPedidos, empleadoID, ubic, estado) VALUES (?, ?, ?, ?)",
                id, empleadoID, ubicacion, 0);
        return id;
    }

    public void insertarDetalle(Connection c, int ordenDeVentaID, int idPedido, DetOrdenDeVenta det)
            throws SQLException {
        Jdbc.actualizar(c, "INSERT INTO DetOrdenDeVenta (ordenDeVentaID, productoID, idPedidos, cantidad, precioUnit)"
                + " VALUES (?, ?, ?, ?, ?)", ordenDeVentaID, det.getProductoID(), idPedido, det.getCantidad(),
                det.getPrecioUnit());
    }

    public List<DetOrdenDeVenta> listarDetalle(Connection c, int ordenDeVentaID) throws SQLException {
        return Jdbc.lista(c, "SELECT productoID, cantidad, precioUnit FROM DetOrdenDeVenta WHERE ordenDeVentaID = ?",
                rs -> {
                    DetOrdenDeVenta d = new DetOrdenDeVenta();
                    d.setOrdenDeVentaID(ordenDeVentaID);
                    d.setProductoID(rs.getInt("productoID"));
                    d.setCantidad(rs.getInt("cantidad"));
                    d.setPrecioUnit(rs.getBigDecimal("precioUnit"));
                    return d;
                }, ordenDeVentaID);
    }

    public void marcarAnulada(Connection c, int ordenDeVentaID, String motivo, int usuarioID) throws SQLException {
        Jdbc.actualizar(c, "UPDATE CabOrdenDeVenta SET estado = ?, motivoAnulacion = ?, usuarioAnulaID = ?,"
                + " fechaAnulacion = GETDATE() WHERE ordenDeVentaID = ?", ESTADO_ANULADA, motivo, usuarioID,
                ordenDeVentaID);
    }

    private static CabOrdenDeVenta mapear(java.sql.ResultSet rs) throws SQLException {
        CabOrdenDeVenta v = new CabOrdenDeVenta();
        v.setOrdenDeVentaID(rs.getInt("ordenDeVentaID"));
        v.setEmpleadoID(rs.getInt("empleadoID"));
        v.setClienteID(rs.getInt("clienteID"));
        v.setMesaID(rs.getInt("mesaID"));
        v.setFechaVenta(rs.getString("fechaVenta"));
        v.setMetodoPago(rs.getInt("metodoPago"));
        v.setTipEntrega(rs.getString("tipEntrega"));
        v.setEstado(rs.getInt("estado"));
        v.setMotivoAnulacion(rs.getString("motivoAnulacion"));
        return v;
    }
}
