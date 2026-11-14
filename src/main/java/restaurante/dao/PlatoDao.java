package restaurante.dao;

import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Plato;

public class PlatoDao {

    public List<Plato> listar() {
        return Jdbc.lista("SELECT idPlato, descPlato FROM Plato ORDER BY idPlato", rs -> {
            Plato plato = new Plato();
            plato.setIdPlato(rs.getInt("idPlato"));
            plato.setDescPlato(rs.getString("descPlato"));
            return plato;
        });
    }
}
