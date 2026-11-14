package restaurante.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Cliente;

public class ClienteDao {

    /** Estado con el que se registran los clientes nuevos. */
    public static final int ESTADO_ACTIVO = 1;

    private static final String SELECT = "SELECT clienteID, nombres, apellidos, dni, genero, estadoCivil, telefono,"
            + " correo, fechNaci, estado, direccion FROM Cliente";

    /** Todos los clientes (o los que coinciden con el nombre o apellido buscado). */
    public List<Cliente> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + " ORDER BY apellidos, nombres", ClienteDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE apellidos LIKE ? OR nombres LIKE ? ORDER BY apellidos, nombres",
                ClienteDao::mapear, patron, patron);
    }

    /** Clientes que tienen al menos una venta registrada. */
    public List<Cliente> listarConCompras(boolean filtrar, String texto) {
        String sql = "SELECT c.clienteID, c.nombres, c.apellidos, c.estado FROM Cliente c"
                + " WHERE EXISTS (SELECT 1 FROM CabOrdenDeVenta v WHERE v.clienteID = c.clienteID)";
        Jdbc.Mapeador<Cliente> mapeador = rs -> {
            Cliente clie = new Cliente();
            clie.setClienteID(rs.getInt("clienteID"));
            clie.setNombres(rs.getString("nombres"));
            clie.setApellidos(rs.getString("apellidos"));
            clie.setEstado(rs.getInt("estado"));
            return clie;
        };
        if (!filtrar) {
            return Jdbc.lista(sql + " ORDER BY c.apellidos, c.nombres", mapeador);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(sql + " AND (c.apellidos LIKE ? OR c.nombres LIKE ?) ORDER BY c.apellidos, c.nombres",
                mapeador, patron, patron);
    }

    /** Registra un cliente nuevo y devuelve su identificador. */
    public int insertar(Cliente c) {
        return Jdbc.transaccion(con -> {
            int id = Jdbc.siguienteId(con, "Cliente", "clienteID");
            Jdbc.actualizar(con, "INSERT INTO Cliente (clienteID, nombres, apellidos, dni, genero, estadoCivil, telefono,"
                    + " correo, fechNaci, estado, direccion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                    id, c.getNombres(), c.getApellidos(), vacioANulo(c.getDni()), null, vacioANulo(c.getEstadoCivil()),
                    vacioANulo(c.getTelefono()), vacioANulo(c.getCorreo()), null, ESTADO_ACTIVO,
                    vacioANulo(c.getDireccion()));
            c.setClienteID(id);
            c.setEstado(ESTADO_ACTIVO);
            return id;
        });
    }

    private static String vacioANulo(String texto) {
        return texto == null || texto.isBlank() ? null : texto.trim();
    }

    private static Cliente mapear(ResultSet rs) throws SQLException {
        Cliente clie = new Cliente();
        clie.setClienteID(rs.getInt("clienteID"));
        clie.setNombres(rs.getString("nombres"));
        clie.setApellidos(rs.getString("apellidos"));
        clie.setDni(rs.getString("dni"));
        clie.setGenero(rs.getInt("genero"));
        clie.setEstadoCivil(rs.getString("estadoCivil"));
        clie.setTelefono(rs.getString("telefono"));
        clie.setCorreo(rs.getString("correo"));
        clie.setFechNaci(rs.getString("fechNaci"));
        clie.setEstado(rs.getInt("estado"));
        clie.setDireccion(rs.getString("direccion"));
        return clie;
    }
}
