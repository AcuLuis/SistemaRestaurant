package restaurante.dao;

import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.LocalR;

public class LocalDao {

    public List<LocalR> listaLocal() {
        return Jdbc.lista("SELECT idLocalR, Ubicacion FROM LocalR ORDER BY idLocalR", rs -> {
            LocalR local = new LocalR();
            local.setIdLocalR(rs.getInt("idLocalR"));
            local.setUbicacion(rs.getString("Ubicacion"));
            return local;
        });
    }
}
