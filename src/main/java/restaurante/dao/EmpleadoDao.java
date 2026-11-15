package restaurante.dao;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Empleado;

public class EmpleadoDao {

    public static final int ESTADO_ACTIVO = 1;
    public static final int ESTADO_BAJA = 0;

    private static final String COLUMNAS = "e.empleadoID, e.nombres, e.apellidos, e.fechaNacimiento, e.genero, e.dni,"
            + " e.estadoCivil, e.estado";
    private static final String FILTRO = " (CAST(e.empleadoID AS varchar(11)) LIKE ? OR e.nombres LIKE ?"
            + " OR e.apellidos LIKE ? OR e.dni LIKE ?)";
    private static final String ORDEN = " ORDER BY e.apellidos, e.nombres";
    private static final String ACTIVOS = " e.estado = " + ESTADO_ACTIVO;

    /** Todos los empleados, incluidos los dados de baja. */
    public List<Empleado> listar(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e";
        return filtrar ? listaConFiltro(sql + " WHERE" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    /** Empleados en activo (los que pueden atender ventas, tener contrato o usuario). */
    public List<Empleado> listarActivos() {
        return Jdbc.lista("SELECT " + COLUMNAS + " FROM Empleado e WHERE" + ACTIVOS + ORDEN, EmpleadoDao::mapear);
    }

    /** Empleados en activo que todavía no tienen contrato. */
    public List<Empleado> listarSinContrato(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e WHERE" + ACTIVOS
                + " AND NOT EXISTS (SELECT 1 FROM Contrato c WHERE c.empleadoID = e.empleadoID)";
        return filtrar ? listaConFiltro(sql + " AND" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    /** Empleados en activo que todavía no tienen usuario del sistema. */
    public List<Empleado> listarSinUsuario(boolean filtrar, String texto) {
        String sql = "SELECT " + COLUMNAS + " FROM Empleado e WHERE" + ACTIVOS
                + " AND NOT EXISTS (SELECT 1 FROM Usuario u WHERE u.empleadoID = e.empleadoID)";
        return filtrar ? listaConFiltro(sql + " AND" + FILTRO + ORDEN, texto)
                : Jdbc.lista(sql + ORDEN, EmpleadoDao::mapear);
    }

    /** Indica si otro empleado ya tiene ese DNI. */
    public boolean existeDni(String dni, int excluirEmpleadoID) {
        return Jdbc.uno("SELECT 1 FROM Empleado WHERE dni = ? AND empleadoID <> ?", rs -> true, dni, excluirEmpleadoID)
                .isPresent();
    }

    /** Registra el empleado con los datos de auditoría y devuelve su identificador. */
    public int insertar(Empleado e, int usuarioID) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Empleado", "empleadoID");
            Jdbc.actualizar(c, "INSERT INTO Empleado (empleadoID, usuarioModifiID, usuarioCreaID, nombres, apellidos,"
                    + " fechaNacimiento, fechCrea, genero, fechModif, dni, estadoCivil, estado)"
                    + " VALUES (?, ?, ?, ?, ?, ?, CAST(GETDATE() AS date), ?, CAST(GETDATE() AS date), ?, ?, ?)",
                    id, usuarioID, usuarioID, e.getNombres(), e.getApellidos(), Date.valueOf(e.getFechaNacimiento()),
                    e.getGenero(), e.getDni(), e.getEstadoCivil(), e.getEstado());
            e.setEmpleadoID(id);
            return id;
        });
    }

    /** Actualiza el empleado registrando quién y cuándo lo modificó. */
    public void actualizar(Empleado e, int usuarioID) {
        Jdbc.actualizar("UPDATE Empleado SET nombres = ?, apellidos = ?, fechaNacimiento = ?, genero = ?, dni = ?,"
                + " estadoCivil = ?, estado = ?, usuarioModifiID = ?, fechModif = CAST(GETDATE() AS date)"
                + " WHERE empleadoID = ?",
                e.getNombres(), e.getApellidos(), Date.valueOf(e.getFechaNacimiento()), e.getGenero(), e.getDni(),
                e.getEstadoCivil(), e.getEstado(), usuarioID, e.getEmpleadoID());
    }

    private List<Empleado> listaConFiltro(String sql, String texto) {
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(sql, EmpleadoDao::mapear, patron, patron, patron, patron);
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
        emp.setEstado(rs.getInt("estado"));
        return emp;
    }
}
