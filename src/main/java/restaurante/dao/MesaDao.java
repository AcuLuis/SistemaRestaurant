package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Mesa;

public class MesaDao {

    public static final int ESTADO_DISPONIBLE = 0;
    public static final int ESTADO_FUERA_DE_SERVICIO = 1;

    private static final String SELECT = "SELECT mesaID, ubic, numSillas, estado FROM Mesa";

    public List<Mesa> listar() {
        return Jdbc.lista(SELECT + " ORDER BY mesaID", MesaDao::mapear);
    }

    public List<Mesa> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return listar();
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE ubic LIKE ? OR CAST(mesaID AS varchar(11)) LIKE ? ORDER BY mesaID",
                MesaDao::mapear, patron, patron);
    }

    /** Mesas que se pueden asignar a una venta o a una reserva. */
    public List<Mesa> listarDisponibles() {
        return Jdbc.lista(SELECT + " WHERE estado = ? ORDER BY mesaID", MesaDao::mapear, ESTADO_DISPONIBLE);
    }

    public int insertar(Mesa m) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Mesa", "mesaID");
            Jdbc.actualizar(c, "INSERT INTO Mesa (mesaID, ubic, numSillas, estado) VALUES (?, ?, ?, ?)",
                    id, m.getUbic(), m.getNumSillas(), m.getEstado());
            m.setMesaID(id);
            return id;
        });
    }

    public void actualizar(Mesa m) {
        Jdbc.actualizar("UPDATE Mesa SET ubic = ?, numSillas = ?, estado = ? WHERE mesaID = ?",
                m.getUbic(), m.getNumSillas(), m.getEstado(), m.getMesaID());
    }

    public void eliminar(Mesa m) {
        Jdbc.actualizar("DELETE FROM Mesa WHERE mesaID = ?", m.getMesaID());
    }

    private static Mesa mapear(ResultSet rs) throws SQLException {
        Mesa mesa = new Mesa();
        mesa.setMesaID(rs.getInt("mesaID"));
        mesa.setUbic(rs.getString("ubic"));
        mesa.setNumSillas(rs.getInt("numSillas"));
        mesa.setEstado(rs.getInt("estado"));
        return mesa;
    }
}
