package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Area;

public class AreaDao {

    private static final String SELECT = "SELECT areaID, descripArea FROM Area";

    public List<Area> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY descripArea", AreaDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE CAST(areaID AS varchar(11)) LIKE ? OR descripArea LIKE ?"
                + " ORDER BY descripArea", AreaDao::mapear, patron, patron);
    }

    public List<Area> listarTodas() {
        return listar(false, "");
    }

    /** Inserta el área asignándole un identificador nuevo, que se devuelve. */
    public int insertar(Area area) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Area", "areaID");
            Jdbc.actualizar(c, "INSERT INTO Area (areaID, descripArea) VALUES (?, ?)", id, area.getDescripArea());
            area.setAreaID(id);
            return id;
        });
    }

    public void actualizar(Area area) {
        Jdbc.actualizar("UPDATE Area SET descripArea = ? WHERE areaID = ?", area.getDescripArea(), area.getAreaID());
    }

    public void eliminar(Area area) {
        Jdbc.actualizar("DELETE FROM Area WHERE areaID = ?", area.getAreaID());
    }

    public int siguienteId() {
        return Jdbc.siguienteIdSugerido("Area", "areaID");
    }

    private static Area mapear(ResultSet rs) throws SQLException {
        Area area = new Area();
        area.setAreaID(rs.getInt("areaID"));
        area.setDescripArea(rs.getString("descripArea"));
        return area;
    }
}
