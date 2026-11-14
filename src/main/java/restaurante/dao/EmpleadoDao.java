package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Empleado;

public class EmpleadoDao {

    private static final String COLUMNAS = "e.empleadoID, e.nombres, e.apellidos, e.fechaNacimiento, e.genero, e.dni,"
            + " e.estadoCivil";
    private static final String FILTRO = " (CAST(e.empleadoID AS varchar(11)) LIKE ? OR e.nombres LIKE ?"
            + " OR e.apellidos LIKE ?)";
    private static final String ORDEN = " ORDER BY e.apellidos, e.nombres";

    /** Empleados que todavía no tienen contrato. */
    public List<Empleado> listarSinContrato(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e"
                + " WHERE NOT EXISTS (SELECT 1 FROM Contrato c WHERE c.empleadoID = e.empleadoID)";
        return filtrar ? listaConFiltro(sql + " AND" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    public List<Empleado> listar(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e";
        return filtrar ? listaConFiltro(sql + " WHERE" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    /** Empleados que todavía no tienen usuario del sistema. */
    public List<Empleado> listarSinUsuario(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e"
                + " WHERE NOT EXISTS (SELECT 1 FROM Usuario u WHERE u.empleadoID = e.empleadoID)";
        return filtrar ? listaConFiltro(sql + " AND" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    private List<Empleado> listaConFiltro(String sql, String texto) {
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(sql, EmpleadoDao::mapear, patron, patron, patron);
    }

    private static Empleado mapear(ResultSet rs) throws SQLException {
        Empleado emp = new Empleado();
        emp.setEmpleadoID(rs.getInt("empleadoID"));
        emp.setNombres(rs.getString("nombres"));
        emp.setApellidos(rs.getString("apellidos"));
        emp.setFechaNacimiento(rs.getString("fechaNacimiento"));
        emp.setGenero(rs.getInt("genero"));
        emp.setDni(rs.getString("dni"));
        emp.setEstadoCivil(rs.getString("estadoCivil"));
        return emp;
    }
}
