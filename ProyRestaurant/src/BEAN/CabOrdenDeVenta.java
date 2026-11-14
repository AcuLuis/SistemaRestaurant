
package BEAN;

public class CabOrdenDeVenta {
    
    int OrdenDeVentaID;
    int empleadoID;
    int clienteID;
    int mesaID;
    String fechaVenta;
    int metodoPago;
    String tipEntrega;
            
    public CabOrdenDeVenta() {
    }

    public CabOrdenDeVenta(int OrdenDeVentaID) {
        this.OrdenDeVentaID = OrdenDeVentaID;
    }

    public CabOrdenDeVenta(int OrdenDeVentaID, int empleadoID, int clienteID, int mesaID) {
        this.OrdenDeVentaID = OrdenDeVentaID;
        this.empleadoID = empleadoID;
        this.clienteID = clienteID;
        this.mesaID = mesaID;
    }

    public CabOrdenDeVenta(int OrdenDeVentaID, int empleadoID, int clienteID, int mesaID, String fechaVenta, int metodoPago, String tipEntrega) {
        this.OrdenDeVentaID = OrdenDeVentaID;
        this.empleadoID = empleadoID;
        this.clienteID = clienteID;
        this.mesaID = mesaID;
        this.fechaVenta = fechaVenta;
        this.metodoPago = metodoPago;
        this.tipEntrega = tipEntrega;
    }

    public String getTipEntrega() {
        return tipEntrega;
    }

    public void setTipEntrega(String tipEntrega) {
        this.tipEntrega = tipEntrega;
    }
    
    public int getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(int metodoPago) {
        this.metodoPago = metodoPago;
    }

    public int getOrdenDeVentaID() {
        return OrdenDeVentaID;
    }

    public void setOrdenDeVentaID(int OrdenDeVentaID) {
        this.OrdenDeVentaID = OrdenDeVentaID;
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

    public int getMesaID() {
        return mesaID;
    }

    public void setMesaID(int mesaID) {
        this.mesaID = mesaID;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(String fechaVenta) {
        this.fechaVenta = fechaVenta;
    }
    
}
