package restaurante.servicio;

import java.time.LocalDate;
import restaurante.dao.EmpleadoDao;
import restaurante.datos.DatosException;
import restaurante.modelo.Empleado;
import restaurante.seguridad.Sesion;
import restaurante.util.Validaciones;

/**
 * Reglas de negocio de los empleados: validación, DNI único y registro de
 * auditoría (quién y cuándo creó o modificó cada empleado).
 */
public class EmpleadoServicio {

    /** Edad mínima para registrar a un empleado. */
    public static final int EDAD_MINIMA = 14;

    private final EmpleadoDao dao = new EmpleadoDao();

    /** Crea o actualiza el empleado según tenga o no identificador. */
    public void guardar(Empleado e) {
        validar(e);
        int usuario = Sesion.actual().map(Sesion::getUsuarioID)
                .orElseThrow(() -> new DatosException("No hay una sesión iniciada."));
        if (e.getEmpleadoID() == 0) {
            dao.insertar(e, usuario);
        } else {
            dao.actualizar(e, usuario);
        }
    }

    static void validarDatos(Empleado e) {
        if (Validaciones.vacio(e.getNombres()) || e.getNombres().length() > 40) {
            throw new DatosException("Indique los nombres (máximo 40 caracteres).");
        }
        if (Validaciones.vacio(e.getApellidos()) || e.getApellidos().length() > 40) {
            throw new DatosException("Indique los apellidos (máximo 40 caracteres).");
        }
        if (!Validaciones.esDni(e.getDni())) {
            throw new DatosException("El DNI debe tener 8 dígitos.");
        }
        LocalDate nacimiento = Validaciones.fecha(e.getFechaNacimiento())
                .orElseThrow(() -> new DatosException("Indique una fecha de nacimiento válida ("
                        + Validaciones.FORMATO_FECHA + ")."));
        if (nacimiento.isAfter(LocalDate.now().minusYears(EDAD_MINIMA))) {
            throw new DatosException("El empleado debe tener al menos " + EDAD_MINIMA + " años.");
        }
    }

    private void validar(Empleado e) {
        validarDatos(e);
        if (dao.existeDni(e.getDni(), e.getEmpleadoID())) {
            throw new DatosException("Ya existe otro empleado con el DNI " + e.getDni() + ".");
        }
        if (e.getEmpleadoID() != 0 && e.getEstado() == EmpleadoDao.ESTADO_BAJA
                && Sesion.actual().map(Sesion::getEmpleadoID).map(id -> id == e.getEmpleadoID()).orElse(false)) {
            throw new DatosException("No puede dar de baja al empleado con el que ha iniciado sesión.");
        }
    }
}
