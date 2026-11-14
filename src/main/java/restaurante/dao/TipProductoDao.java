package restaurante.dao;

import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.TipProducto;

public class TipProductoDao {

    public List<TipProducto> listaTip() {
        return Jdbc.lista("SELECT idTipProducto, descripcion FROM TipProducto ORDER BY idTipProducto", rs -> {
            TipProducto tipo = new TipProducto();
            tipo.setIdTipProducto(rs.getInt("idTipProducto"));
            tipo.setDescripcion(rs.getString("descripcion"));
            return tipo;
        });
    }
}
