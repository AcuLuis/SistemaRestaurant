package restaurante.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import restaurante.datos.Jdbc;
import restaurante.modelo.Producto;

public class ProductoDao {

    private static final String SELECT =
            "SELECT productoID, idPlato, idTipProducto, descripProducto, cantidad, precioUnit FROM Producto";

    /** Inserta el producto asignándole un identificador nuevo, que se devuelve. */
    public int insertar(Producto p) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Producto", "productoID");
            Jdbc.actualizar(c, "INSERT INTO Producto (productoID, idPlato, idTipProducto, descripProducto, cantidad,"
                    + " precioUnit) VALUES (?, ?, ?, ?, ?, ?)",
                    id, p.getPlato(), p.getTipo(), p.getDescripProducto(), p.getCantidad(), p.getPrecioUnit());
            p.setProductoID(id);
            return id;
        });
    }

    public void actualizar(Producto p) {
        Jdbc.actualizar("UPDATE Producto SET idPlato = ?, idTipProducto = ?, descripProducto = ?, cantidad = ?,"
                + " precioUnit = ? WHERE productoID = ?",
                p.getPlato(), p.getTipo(), p.getDescripProducto(), p.getCantidad(), p.getPrecioUnit(), p.getProductoID());
    }

    public void eliminar(Producto p) {
        Jdbc.actualizar("DELETE FROM Producto WHERE productoID = ?", p.getProductoID());
    }

    public List<Producto> listar(boolean filtrar, String texto) {
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
    public List<Producto> listarPorVenta(int ordenDeVentaID) {
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
                    pro.setPrecioUnit(rs.getBigDecimal("precioUnit"));
                    return pro;
                }, ordenDeVentaID);
    }


    /**
     * Resta existencias solo si hay suficientes. Devuelve false si no las hay
     * (la comprobación y la actualización son una única sentencia atómica).
     */
    public boolean descontarExistencias(Connection c, int productoID, int cantidad) throws SQLException {
        return Jdbc.actualizar(c, "UPDATE Producto SET cantidad = cantidad - ? WHERE productoID = ? AND cantidad >= ?",
                cantidad, productoID, cantidad) == 1;
    }

    public void sumarExistencias(Connection c, int productoID, int cantidad) throws SQLException {
        Jdbc.actualizar(c, "UPDATE Producto SET cantidad = cantidad + ? WHERE productoID = ?", cantidad, productoID);
    }

    public Optional<Producto> buscar(Connection c, int productoID) throws SQLException {
        return Jdbc.uno(c, SELECT + " WHERE productoID = ?", ProductoDao::mapear, productoID);
    }

    /** Productos con existencias iguales o inferiores al mínimo indicado. */
    public List<Producto> listarConPocasExistencias(int minimo) {
        return Jdbc.lista(SELECT + " WHERE cantidad <= ? ORDER BY cantidad, descripProducto", ProductoDao::mapear, minimo);
    }

    private static Producto mapear(ResultSet rs) throws SQLException {
        Producto pro = new Producto();
        pro.setProductoID(rs.getInt("productoID"));
        pro.setPlato(rs.getInt("idPlato"));
        pro.setTipo(rs.getInt("idTipProducto"));
        pro.setDescripProducto(rs.getString("descripProducto"));
        pro.setCantidad(rs.getInt("cantidad"));
        pro.setPrecioUnit(rs.getBigDecimal("precioUnit"));
        return pro;
    }
}
