package restaurante.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Contrato;

public class ContratoDao {

    private static final String SELECT = "SELECT contratoID, empleadoID, rolID, areaID, fechaIni, fechaFin, estado,"
            + " sueldo FROM Contrato";

    public List<Contrato> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY contratoID", ContratoDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE CAST(contratoID AS varchar(11)) LIKE ?"
                + " OR CONVERT(varchar(10), fechaIni, 23) LIKE ? OR CONVERT(varchar(10), fechaFin, 23) LIKE ?"
                + " OR CAST(CAST(sueldo AS decimal(18, 2)) AS varchar(30)) LIKE ? ORDER BY contratoID",
                ContratoDao::mapear, patron, patron, patron, patron);
    }

    /** Registra el contrato asignándole un identificador nuevo, que se devuelve. */
    public int insertar(Contrato c) {
        return Jdbc.transaccion(con -> {
            int id = Jdbc.siguienteId(con, "Contrato", "contratoID");
            Jdbc.actualizar(con, "INSERT INTO Contrato (contratoID, empleadoID, rolID, areaID, fechaIni, fechaFin, estado,"
                    + " sueldo) VALUES (?, ?, ?, ?, ?, ?, ?, ?)",
                    id, c.getEmpleadoID(), c.getRolID(), c.getAreaID(), Date.valueOf(c.getFechaIni()),
                    Date.valueOf(c.getFechaFin()), c.getEstado(), c.getSueldo());
            c.setContratoID(id);
            return id;
        });
    }

    public void actualizar(Contrato c) {
        Jdbc.actualizar("UPDATE Contrato SET empleadoID = ?, rolID = ?, areaID = ?, fechaIni = ?, fechaFin = ?,"
                + " estado = ?, sueldo = ? WHERE contratoID = ?",
                c.getEmpleadoID(), c.getRolID(), c.getAreaID(), Date.valueOf(c.getFechaIni()),
                Date.valueOf(c.getFechaFin()), c.getEstado(), c.getSueldo(), c.getContratoID());
    }

    public void eliminar(Contrato contrato) {
        Jdbc.actualizar("DELETE FROM Contrato WHERE contratoID = ?", contrato.getContratoID());
    }


    private static Contrato mapear(ResultSet rs) throws SQLException {
        Contrato contrato = new Contrato();
        contrato.setContratoID(rs.getInt("contratoID"));
        contrato.setEmpleadoID(rs.getInt("empleadoID"));
        contrato.setRolID(rs.getInt("rolID"));
        contrato.setAreaID(rs.getInt("areaID"));
        contrato.setFechaIni(rs.getString("fechaIni"));
        contrato.setFechaFin(rs.getString("fechaFin"));
        contrato.setEstado(rs.getInt("estado"));
        contrato.setSueldo(rs.getBigDecimal("sueldo"));
        return contrato;
    }
}
