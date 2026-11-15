package restaurante.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import restaurante.datos.Jdbc;
import restaurante.modelo.CabReceta;
import restaurante.modelo.DetReceta;

/** Recetas: insumos necesarios para preparar una unidad de cada producto. */
public class RecetaDao {

    private static final String SELECT = "SELECT recetaID, empleadoID, productoID,"
            + " CONVERT(varchar(10), fechaElab, 23) AS fechaElab, preparacion FROM CabReceta";

    public Optional<CabReceta> buscarPorProducto(int productoID) {
        return Jdbc.uno(SELECT + " WHERE productoID = ?", RecetaDao::mapear, productoID);
    }

    public Optional<CabReceta> buscarPorProducto(Connection c, int productoID) throws SQLException {
        return Jdbc.uno(c, SELECT + " WITH (UPDLOCK) WHERE productoID = ?", RecetaDao::mapear, productoID);
    }

    /** Identificadores de los productos que tienen receta. */
    public Set<Integer> productosConReceta() {
        return new HashSet<>(Jdbc.lista("SELECT DISTINCT productoID FROM CabReceta", rs -> rs.getInt(1)));
    }

    public List<DetReceta> listarDetalle(int recetaID) {
        return Jdbc.transaccion(c -> listarDetalle(c, recetaID));
    }

    public List<DetReceta> listarDetalle(Connection c, int recetaID) throws SQLException {
        return Jdbc.lista(c, "SELECT d.recetaID, d.insumoID, d.cantidad, i.descripInsumo, i.unidadMedida"
                + " FROM DetReceta d INNER JOIN Insumo i ON i.insumoID = d.insumoID WHERE d.recetaID = ?"
                + " ORDER BY i.descripInsumo", rs -> {
                    DetReceta d = new DetReceta(rs.getInt("insumoID"), rs.getBigDecimal("cantidad"));
                    d.setRecetaID(rs.getInt("recetaID"));
                    d.setDescripInsumo(rs.getString("descripInsumo"));
                    d.setUnidadMedida(rs.getString("unidadMedida"));
                    return d;
                }, recetaID);
    }

    public int insertarCabecera(Connection c, CabReceta r) throws SQLException {
        int id = Jdbc.siguienteId(c, "CabReceta", "recetaID");
        Jdbc.actualizar(c, "INSERT INTO CabReceta (recetaID, empleadoID, productoID, fechaElab, preparacion)"
                + " VALUES (?, ?, ?, CAST(GETDATE() AS date), ?)", id, r.getEmpleadoID() == 0 ? null : r.getEmpleadoID(),
                r.getProductoID(), r.getPreparacion());
        r.setRecetaID(id);
        return id;
    }

    public void actualizarCabecera(Connection c, CabReceta r) throws SQLException {
        Jdbc.actualizar(c, "UPDATE CabReceta SET empleadoID = ?, fechaElab = CAST(GETDATE() AS date), preparacion = ?"
                + " WHERE recetaID = ?", r.getEmpleadoID() == 0 ? null : r.getEmpleadoID(), r.getPreparacion(),
                r.getRecetaID());
    }

    public void borrarDetalle(Connection c, int recetaID) throws SQLException {
        Jdbc.actualizar(c, "DELETE FROM DetReceta WHERE recetaID = ?", recetaID);
    }

    public void insertarDetalle(Connection c, int recetaID, DetReceta d) throws SQLException {
        Jdbc.actualizar(c, "INSERT INTO DetReceta (recetaID, insumoID, cantidad) VALUES (?, ?, ?)",
                recetaID, d.getInsumoID(), d.getCantidad());
    }

    public void eliminar(Connection c, int recetaID) throws SQLException {
        borrarDetalle(c, recetaID);
        Jdbc.actualizar(c, "DELETE FROM CabReceta WHERE recetaID = ?", recetaID);
    }

    private static CabReceta mapear(java.sql.ResultSet rs) throws SQLException {
        CabReceta r = new CabReceta();
        r.setRecetaID(rs.getInt("recetaID"));
        r.setEmpleadoID(rs.getInt("empleadoID"));
        r.setProductoID(rs.getInt("productoID"));
        r.setFechaElab(rs.getString("fechaElab"));
        r.setPreparacion(rs.getString("preparacion"));
        return r;
    }
}
