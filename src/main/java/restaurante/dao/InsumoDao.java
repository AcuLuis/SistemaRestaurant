package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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

    public int siguienteId() {
        return Jdbc.siguienteIdSugerido("Insumo", "insumoID");
    }

    private static Insumo mapear(ResultSet rs) throws SQLException {
        Insumo insumo = new Insumo();
        insumo.setInsumoID(rs.getInt("insumoID"));
        insumo.setIdTipInsumo(rs.getInt("idTipInsumo"));
        insumo.setUnidadMedida(rs.getString("unidadMedida"));
        insumo.setDescripInsumo(rs.getString("descripInsumo"));
        insumo.setPrecioUnit(rs.getDouble("precioUnit"));
        insumo.setCantidad(rs.getInt("cantidad"));
        return insumo;
    }
}
