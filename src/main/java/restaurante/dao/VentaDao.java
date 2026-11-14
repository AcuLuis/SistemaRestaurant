package restaurante.dao;

import java.util.List;
import restaurante.datos.DatosException;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabOrdenDeVenta;
import restaurante.modelo.DetOrdenDeVenta;

public class VentaDao {

    /** Ventas de un cliente, de la más reciente a la más antigua. */
    public List<CabOrdenDeVenta> listarPorCliente(int clienteID) {
        return Jdbc.lista("SELECT ordenDeVentaID, empleadoID, clienteID, mesaID,"
                + " CONVERT(varchar(19), fechaVenta, 120) AS fechaVenta, metodoPago, tipEntrega"
                + " FROM CabOrdenDeVenta WHERE clienteID = ? ORDER BY fechaVenta DESC", rs -> {
                    CabOrdenDeVenta v = new CabOrdenDeVenta();
                    v.setOrdenDeVentaID(rs.getInt("ordenDeVentaID"));
                    v.setEmpleadoID(rs.getInt("empleadoID"));
                    v.setClienteID(rs.getInt("clienteID"));
                    v.setMesaID(rs.getInt("mesaID"));
                    v.setFechaVenta(rs.getString("fechaVenta"));
                    v.setMetodoPago(rs.getInt("metodoPago"));
                    v.setTipEntrega(rs.getString("tipEntrega"));
                    return v;
                }, clienteID);
    }

    /**
     * Registra una venta completa (cabecera, pedido y detalle) en una única
     * transacción: si cualquier paso falla no queda ninguna venta a medias.
     *
     * @return identificador de la orden de venta creada
     */
    public int registrarVenta(CabOrdenDeVenta cab, String ubicacionPedido, List<DetOrdenDeVenta> detalles) {
        if (detalles == null || detalles.isEmpty()) {
            throw new DatosException("La venta debe incluir al menos un producto.");
        }
        return Jdbc.transaccion(c -> {
            int idVenta = Jdbc.siguienteId(c, "CabOrdenDeVenta", "ordenDeVentaID");
            Jdbc.actualizar(c, "INSERT INTO CabOrdenDeVenta (ordenDeVentaID, empleadoID, clienteID, mesaID, fechaVenta,"
                    + " metodoPago, tipEntrega) VALUES (?, ?, ?, ?, GETDATE(), ?, ?)",
                    idVenta, cab.getEmpleadoID(), cab.getClienteID(), cab.getMesaID() > 0 ? cab.getMesaID() : null,
                    cab.getMetodoPago(), cab.getTipEntrega());

            int idPedido = Jdbc.siguienteId(c, "Pedidos", "idPedidos");
            Jdbc.actualizar(c, "INSERT INTO Pedidos (idPedidos, empleadoID, ubic, estado) VALUES (?, ?, ?, ?)",
                    idPedido, cab.getEmpleadoID(), ubicacionPedido, 0);

            for (DetOrdenDeVenta det : detalles) {
                if (det.getCantidad() <= 0) {
                    throw new DatosException("La cantidad de cada producto debe ser mayor que cero.");
                }
                Jdbc.actualizar(c, "INSERT INTO DetOrdenDeVenta (ordenDeVentaID, productoID, idPedidos, cantidad,"
                        + " precioUnit) VALUES (?, ?, ?, ?, ?)",
                        idVenta, det.getProductoID(), idPedido, det.getCantidad(), det.getPrecioUnit());
            }
            cab.setOrdenDeVentaID(idVenta);
            return idVenta;
        });
    }
}
