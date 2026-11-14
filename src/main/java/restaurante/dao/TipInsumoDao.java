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
}
