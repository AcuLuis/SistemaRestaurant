package restaurante.dao;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import restaurante.datos.Jdbc;
import restaurante.modelo.Reserva;

/** Reservas de mesa. */
public class ReservaDao {

    /** Margen en minutos durante el que una mesa reservada no admite otra reserva. */
    public static final int DURACION_MINUTOS = 120;

    private static final String SELECT = "SELECT r.reservaID, r.empleadoID, r.clienteID,"
            + " CONVERT(varchar(10), r.fecha, 23) AS fecha, CONVERT(varchar(5), r.hora, 108) AS hora, r.personas,"
            + " r.mesaID, r.estado, r.observacion, c.apellidos + ', ' + c.nombres AS cliente, m.ubic AS mesa"
            + " FROM Reserva r INNER JOIN Cliente c ON c.clienteID = r.clienteID"
            + " LEFT JOIN Mesa m ON m.mesaID = r.mesaID";
    private static final String ORDEN = " ORDER BY r.fecha DESC, r.hora";

    public List<Reserva> listar(boolean filtrar, String texto) {
        if (!filtrar) {
            return Jdbc.lista(SELECT + ORDEN, ReservaDao::mapear);
        }
        String patron = Jdbc.empiezaPor(texto);
        return Jdbc.lista(SELECT + " WHERE c.apellidos LIKE ? OR c.nombres LIKE ?"
                + " OR CONVERT(varchar(10), r.fecha, 23) LIKE ?" + ORDEN, ReservaDao::mapear, patron, patron, patron);
    }

    /** Reservas pendientes de hoy (según la fecha del servidor), por hora. */
    public List<Reserva> listarPendientesDeHoy() {
        return Jdbc.lista(SELECT + " WHERE r.fecha = CAST(GETDATE() AS date) AND r.estado = ? ORDER BY r.hora",
                ReservaDao::mapear, Reserva.PENDIENTE);
    }

    /** Indica si la mesa ya tiene otra reserva pendiente a una hora cercana del mismo día. */
    public boolean mesaOcupada(int mesaID, java.time.LocalDate fecha, LocalTime hora, int excluirReservaID) {
        return Jdbc.uno("SELECT TOP 1 1 FROM Reserva WHERE mesaID = ? AND fecha = ? AND estado = ? AND reservaID <> ?"
                + " AND ABS(DATEDIFF(MINUTE, hora, CAST(? AS time))) < ?", rs -> true, mesaID, Date.valueOf(fecha),
                Reserva.PENDIENTE, excluirReservaID, Time.valueOf(hora), DURACION_MINUTOS).isPresent();
    }

    public int insertar(Reserva r) {
        return Jdbc.transaccion(c -> {
            int id = Jdbc.siguienteId(c, "Reserva", "reservaID");
            Jdbc.actualizar(c, "INSERT INTO Reserva (reservaID, empleadoID, clienteID, fecha, estado, hora, personas,"
                    + " mesaID, observacion) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", id,
                    r.getEmpleadoID() == 0 ? null : r.getEmpleadoID(), r.getClienteID(), Date.valueOf(r.getFecha()),
                    r.getEstado(), Time.valueOf(LocalTime.parse(r.getHora())), r.getPersonas(), r.getMesaID(),
                    r.getObservacion());
            r.setReservaID(id);
            return id;
        });
    }

    public void actualizar(Reserva r) {
        Jdbc.actualizar("UPDATE Reserva SET clienteID = ?, fecha = ?, estado = ?, hora = ?, personas = ?, mesaID = ?,"
                + " observacion = ? WHERE reservaID = ?", r.getClienteID(), Date.valueOf(r.getFecha()), r.getEstado(),
                Time.valueOf(LocalTime.parse(r.getHora())), r.getPersonas(), r.getMesaID(), r.getObservacion(),
                r.getReservaID());
    }

    public void eliminar(Reserva r) {
        Jdbc.actualizar("DELETE FROM Reserva WHERE reservaID = ?", r.getReservaID());
    }

    private static Reserva mapear(java.sql.ResultSet rs) throws SQLException {
        Reserva r = new Reserva();
        r.setReservaID(rs.getInt("reservaID"));
        r.setEmpleadoID(rs.getInt("empleadoID"));
        r.setClienteID(rs.getInt("clienteID"));
        r.setFecha(rs.getString("fecha"));
        r.setHora(rs.getString("hora"));
        r.setPersonas(rs.getInt("personas"));
        r.setMesaID(Jdbc.enteroONulo(rs, "mesaID"));
        r.setEstado(rs.getInt("estado"));
        r.setObservacion(rs.getString("observacion"));
        r.setCliente(rs.getString("cliente"));
        r.setMesa(rs.getString("mesa"));
        return r;
    }
}
