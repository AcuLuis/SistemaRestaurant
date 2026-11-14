package restaurante.dao;

import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Mesa;

public class MesaDao {

    public List<Mesa> ListaMesa() {
        return Jdbc.lista("SELECT mesaID, ubic, numSillas, estado FROM Mesa ORDER BY mesaID", rs -> {
            Mesa mesa = new Mesa();
            mesa.setMesaID(rs.getInt("mesaID"));
            mesa.setUbic(rs.getString("ubic"));
            mesa.setNumSillas(rs.getInt("numSillas"));
            mesa.setEstado(rs.getInt("estado"));
            return mesa;
        });
    }

    public int devuelveIdMesa(String ubicacion) {
        return Jdbc.uno("SELECT mesaID FROM Mesa WHERE ubic = ?", rs -> rs.getInt(1), ubicacion).orElse(0);
    }
}
