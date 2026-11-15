package restaurante.servicio;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import restaurante.dao.MesaDao;
import restaurante.dao.ReservaDao;
import restaurante.datos.DatosException;
import restaurante.modelo.Mesa;
import restaurante.modelo.Reserva;
import restaurante.seguridad.Sesion;
import restaurante.util.Validaciones;

/**
 * Reglas de las reservas: fecha y hora válidas, capacidad de la mesa y que
 * una mesa no tenga dos reservas pendientes a horas cercanas.
 */
public class ReservaServicio {

    public static final int MAXIMO_PERSONAS = 100;
    public static final int MAXIMO_OBSERVACION = 200;

    private final ReservaDao dao = new ReservaDao();
    private final MesaDao mesaDao = new MesaDao();

    public void guardar(Reserva r) {
        if (r.getClienteID() <= 0) {
            throw new DatosException("Seleccione el cliente.");
        }
        LocalDate fecha = Validaciones.fecha(r.getFecha())
                .orElseThrow(() -> new DatosException("Indique una fecha válida (" + Validaciones.FORMATO_FECHA + ")."));
        LocalTime hora = hora(r.getHora());
        boolean nueva = r.getReservaID() == 0;
        if (nueva && fecha.isBefore(LocalDate.now())) {
            throw new DatosException("La fecha de la reserva no puede ser anterior a hoy.");
        }
        if (r.getPersonas() < 1 || r.getPersonas() > MAXIMO_PERSONAS) {
            throw new DatosException("El número de personas debe estar entre 1 y " + MAXIMO_PERSONAS + ".");
        }
        if (r.getObservacion() != null && r.getObservacion().length() > MAXIMO_OBSERVACION) {
            throw new DatosException("La observación admite como máximo " + MAXIMO_OBSERVACION + " caracteres.");
        }
        if (r.getMesaID() != null && r.getEstado() == Reserva.PENDIENTE) {
            Mesa mesa = mesaDao.listar().stream().filter(m -> m.getMesaID() == r.getMesaID()).findFirst()
                    .orElseThrow(() -> new DatosException("La mesa elegida no existe."));
            if (mesa.getNumSillas() > 0 && r.getPersonas() > mesa.getNumSillas()) {
                throw new DatosException("La mesa \"" + mesa.getUbic() + "\" tiene " + mesa.getNumSillas()
                        + " sillas y la reserva es para " + r.getPersonas() + " personas.");
            }
            if (dao.mesaOcupada(r.getMesaID(), fecha, hora, r.getReservaID())) {
                throw new DatosException("La mesa \"" + mesa.getUbic() + "\" ya tiene otra reserva ese día a una hora"
                        + " cercana (menos de " + ReservaDao.DURACION_MINUTOS / 60 + " horas de diferencia).");
            }
        }
        r.setFecha(fecha.toString());
        r.setHora(hora.toString());
        if (nueva) {
            r.setEmpleadoID(Sesion.actual().map(Sesion::getEmpleadoID).orElse(0));
            dao.insertar(r);
        } else {
            dao.actualizar(r);
        }
    }

    static LocalTime hora(String texto) {
        try {
            return LocalTime.parse(texto == null ? "" : texto.trim());
        } catch (DateTimeParseException e) {
            throw new DatosException("Indique la hora con el formato hh:mm (por ejemplo, 20:30).");
        }
    }
}
