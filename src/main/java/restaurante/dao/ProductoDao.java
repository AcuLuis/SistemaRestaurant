package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Producto;

public class ProductoDao {

    private static final String SELECT =
            "SELECT productoID, idPlato, idTipProducto, descripProducto, cantidad, precioUnit FROM Producto";

    /** Inserta el producto asignándole un identificador nuevo, que se devuelve. */
    public int insertaProducto(Producto p) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Producto", "productoID");
            Jdbc.actualizar(c, "INSERT INTO Producto (productoID, idPlato, idTipProducto, descripProducto, cantidad,"
                    + " precioUnit) VALUES (?, ?, ?, ?, ?, ?)",
                    id, p.getPlato(), p.getTipo(), p.getDescripProducto(), p.getCantidad(), p.getPrecioUnit());
            p.setProductoID(id);
            return id;
        });
    }

    public void actualizarProducto(Producto p) {
        Jdbc.actualizar("UPDATE Producto SET idPlato = ?, idTipProducto = ?, descripProducto = ?, cantidad = ?,"
                + " precioUnit = ? WHERE productoID = ?",
                p.getPlato(), p.getTipo(), p.getDescripProducto(), p.getCantidad(), p.getPrecioUnit(), p.getProductoID());
    }

    public void eliminarProducto(Producto p) {
        Jdbc.actualizar("DELETE FROM Producto WHERE productoID = ?", p.getProductoID());
    }

    public List<Producto> ListaProducto(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY descripProducto", ProductoDao::mapear);
        }
        return Jdbc.lista(SELECT + " WHERE descripProducto LIKE ? ORDER BY descripProducto",
                ProductoDao::mapear, Jdbc.empiezaPor(texto));
    }

    /**
     * Productos incluidos en una orden de venta, con la cantidad vendida y el
     * precio aplicado en la venta (o el precio actual si la venta es antigua).
     */
    public List<Producto> ListaProducto2(int ordenDeVentaID) {
        return Jdbc.lista("SELECT p.productoID, p.descripProducto, p.idTipProducto, p.idPlato, d.cantidad,"
                + " COALESCE(d.precioUnit, p.precioUnit) AS precioUnit"
                + " FROM DetOrdenDeVenta d INNER JOIN Producto p ON p.productoID = d.productoID"
                + " WHERE d.ordenDeVentaID = ? ORDER BY p.descripProducto", rs -> {
                    Producto pro = new Producto();
                    pro.setProductoID(rs.getInt("productoID"));
                    pro.setDescripProducto(rs.getString("descripProducto"));
                    pro.setTipo(rs.getInt("idTipProducto"));
                    pro.setPlato(rs.getInt("idPlato"));
                    pro.setCantidad(rs.getInt("cantidad"));
                    pro.setPrecioUnit(rs.getDouble("precioUnit"));
                    return pro;
                }, ordenDeVentaID);
    }

    public int siguienteId() {
        return Jdbc.siguienteIdSugerido("Producto", "productoID");
    }

    private static Producto mapear(ResultSet rs) throws SQLException {
        Producto pro = new Producto();
        pro.setProductoID(rs.getInt("productoID"));
        pro.setPlato(rs.getInt("idPlato"));
        pro.setTipo(rs.getInt("idTipProducto"));
        pro.setDescripProducto(rs.getString("descripProducto"));
        pro.setCantidad(rs.getInt("cantidad"));
        pro.setPrecioUnit(rs.getDouble("precioUnit"));
        return pro;
    }
}
