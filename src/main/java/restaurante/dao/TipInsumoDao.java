package restaurante.dao;

import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.TipInsumo;

public class TipInsumoDao {

    public List<TipInsumo> llenaTipInsumo() {
        return Jdbc.lista("SELECT idTipInsumo, descripcion FROM TipInsumo ORDER BY descripcion", rs -> {
            TipInsumo tipo = new TipInsumo();
            tipo.setIdInsumo(rs.getInt("idTipInsumo"));
            tipo.setDescripcion(rs.getString("descripcion"));
            return tipo;
        });
    }

    public String devTipInsumo(int idTipInsumo) {
        return Jdbc.uno("SELECT descripcion FROM TipInsumo WHERE idTipInsumo = ?",
                rs -> rs.getString(1), idTipInsumo).orElse("");
    }

    public int devIDTipInsumo(String descripcion) {
        return Jdbc.uno("SELECT idTipInsumo FROM TipInsumo WHERE descripcion = ?",
                rs -> rs.getInt(1), descripcion).orElse(0);
    }
}
