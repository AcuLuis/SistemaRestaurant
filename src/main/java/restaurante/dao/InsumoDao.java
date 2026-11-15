package restaurante.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import restaurante.datos.Jdbc;
import restaurante.modelo.Insumo;

public class InsumoDao {

    private static final String SELECT =
            "SELECT insumoID, idTipInsumo, unidadMedida, descripInsumo, precioUnit, cantidad FROM Insumo";

    public List<Insumo> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY descripInsumo", InsumoDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE descripInsumo LIKE ? OR CAST(insumoID AS varchar(11)) LIKE ?"
                + " OR unidadMedida LIKE ? ORDER BY descripInsumo", InsumoDao::mapear, patron, patron, patron);
    }

    /** Inserta el insumo asignándole un identificador nuevo, que se devuelve. */
    public int insertar(Insumo i) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Insumo", "insumoID");
            Jdbc.actualizar(c, "INSERT INTO Insumo (insumoID, idTipInsumo, unidadMedida, descripInsumo, precioUnit, cantidad)"
                    + " VALUES (?, ?, ?, ?, ?, ?)",
                    id, i.getIdTipInsumo(), i.getUnidadMedida(), i.getDescripInsumo(), i.getPrecioUnit(), i.getCantidad());
            i.setInsumoID(id);
            return id;
        });
    }

    public void actualizar(Insumo i) {
        Jdbc.actualizar("UPDATE Insumo SET idTipInsumo = ?, unidadMedida = ?, descripInsumo = ?, precioUnit = ?,"
                + " cantidad = ? WHERE insumoID = ?",
                i.getIdTipInsumo(), i.getUnidadMedida(), i.getDescripInsumo(), i.getPrecioUnit(), i.getCantidad(),
                i.getInsumoID());
    }

    public void eliminar(int insumoID) {
        Jdbc.actualizar("DELETE FROM Insumo WHERE insumoID = ?", insumoID);
    }


    /** Resta la cantidad solo si hay suficiente (comprobación y actualización atómicas). */
    public boolean descontar(Connection c, int insumoID, BigDecimal cantidad) throws SQLException {
        return Jdbc.actualizar(c, "UPDATE Insumo SET cantidad = cantidad - ? WHERE insumoID = ? AND cantidad >= ?",
                cantidad, insumoID, cantidad) == 1;
    }

    public Optional<Insumo> buscar(Connection c, int insumoID) throws SQLException {
        return Jdbc.uno(c, SELECT + " WHERE insumoID = ?", InsumoDao::mapear, insumoID);
    }

    /** Insumos con existencias iguales o inferiores al mínimo indicado. */
    public List<Insumo> listarConPocasExistencias(BigDecimal minimo) {
        return Jdbc.lista(SELECT + " WHERE cantidad <= ? ORDER BY cantidad, descripInsumo", InsumoDao::mapear, minimo);
    }

    private static Insumo mapear(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setInsumoID(rs.getInt("insumoID"));
        insumo.setIdTipInsumo(rs.getInt("idTipInsumo"));
        insumo.setUnidadMedida(rs.getString("unidadMedida"));
        insumo.setDescripInsumo(rs.getString("descripInsumo"));
        insumo.setPrecioUnit(rs.getBigDecimal("precioUnit"));
        insumo.setCantidad(rs.getBigDecimal("cantidad"));
        return insumo;
    }
}
