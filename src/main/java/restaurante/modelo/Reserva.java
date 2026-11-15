package restaurante.modelo;

/** Reserva de mesa de un cliente. */
public class Reserva {

    public static final int CANCELADA = 0;
    public static final int PENDIENTE = 1;
    public static final int ATENDIDA = 2;

    private int reservaID;
    private int empleadoID;
    private int clienteID;
    /** Fecha (aaaa-mm-dd). */
    private String fecha;
    /** Hora (hh:mm). */
    private String hora;
    private int personas;
    private Integer mesaID;
    private int estado = PENDIENTE;
    private String observacion;
    /** Datos para mostrar. */
    private String cliente;
    private String mesa;

    public int getReservaID() {
        return reservaID;
    }

    public void setReservaID(int reservaID) {
        this.reservaID = reservaID;
    }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public int getClienteID() {
        return clienteID;
    }

    public void setClienteID(int clienteID) {
        this.clienteID = clienteID;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getPersonas() {
        return personas;
    }

    public void setPersonas(int personas) {
        this.personas = personas;
    }

    public Integer getMesaID() {
        return mesaID;
    }

    public void setMesaID(Integer mesaID) {
        this.mesaID = mesaID;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }
}
