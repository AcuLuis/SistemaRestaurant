package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Rol;

public class RolDao {

    private static final String SELECT = "SELECT rolID, descripRol FROM Rol";

    public List<Rol> listaRol(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY descripRol", RolDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE CAST(rolID AS varchar(11)) LIKE ? OR descripRol LIKE ?"
                + " ORDER BY descripRol", RolDao::mapear, patron, patron);
    }

    public List<Rol> llenaRol() {
        return listaRol(false, "");
    }

    /** Inserta el rol asignándole un identificador nuevo, que se devuelve. */
    public int insertaRol(Rol rol) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Rol", "rolID");
            Jdbc.actualizar(c, "INSERT INTO Rol (rolID, descripRol) VALUES (?, ?)", id, rol.getDescripRol());
            rol.setRolID(id);
            return id;
        });
    }

    public void actualizarRol(Rol rol) {
        Jdbc.actualizar("UPDATE Rol SET descripRol = ? WHERE rolID = ?", rol.getDescripRol(), rol.getRolID());
    }

    public void eliminarRol(Rol rol) {
        Jdbc.actualizar("DELETE FROM Rol WHERE rolID = ?", rol.getRolID());
    }

    public String devRol(int idRol) {
        return Jdbc.uno("SELECT descripRol FROM Rol WHERE rolID = ?", rs -> rs.getString(1), idRol).orElse("");
    }

    public int devIDRol(String rol) {
        return Jdbc.uno("SELECT rolID FROM Rol WHERE descripRol = ?", rs -> rs.getInt(1), rol).orElse(0);
    }

    public int siguienteId() {
        return Jdbc.siguienteIdSugerido("Rol", "rolID");
    }

    private static Rol mapear(ResultSet rs) throws SQLException {
        Rol rol = new Rol();
        rol.setRolID(rs.getInt("rolID"));
        rol.setDescripRol(rs.getString("descripRol"));
        return rol;
    }
}
