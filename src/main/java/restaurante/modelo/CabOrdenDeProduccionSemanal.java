package restaurante.modelo;

/** Orden de producción: productos que la cocina debe preparar. */
public class CabOrdenDeProduccionSemanal {

    public static final int PENDIENTE = 0;
    public static final int COMPLETADA = 1;

    private int ordenProduccionID;
    private int empleadoID;
    private String descripArea;
    private String fechaxsem;
    private int estado = PENDIENTE;
    private String fechaCompletada;
    /** Nombre del responsable, solo para mostrar. */
    private String empleado;

    public int getOrdenProduccionID() {
        return ordenProduccionID;
    }

    public void setOrdenProduccionID(int ordenProduccionID) {
        this.ordenProduccionID = ordenProduccionID;
    }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public String getDescripArea() {
        return descripArea;
    }

    public void setDescripArea(String descripArea) {
        this.descripArea = descripArea;
    }

    /** Fecha de la orden (aaaa-mm-dd). */
    public String getFechaxsem() {
        return fechaxsem;
    }

    public void setFechaxsem(String fechaxsem) {
        this.fechaxsem = fechaxsem;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getFechaCompletada() {
        return fechaCompletada;
    }

    public void setFechaCompletada(String fechaCompletada) {
        this.fechaCompletada = fechaCompletada;
    }

    public String getEmpleado() {
        return empleado;
    }

    public void setEmpleado(String empleado) {
        this.empleado = empleado;
    }
}
