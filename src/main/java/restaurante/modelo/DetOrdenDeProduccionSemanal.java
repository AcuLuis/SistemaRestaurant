package restaurante.modelo;

/** Producto y cantidad de una orden de producción. */
public class DetOrdenDeProduccionSemanal {

    private int ordenProduccionID;
    private int productoID;
    private int cantidad;
    private int estado;
    /** Descripción del producto, solo para mostrar. */
    private String descripProducto;

    public DetOrdenDeProduccionSemanal() {
    }

    public DetOrdenDeProduccionSemanal(int productoID, int cantidad) {
        this.productoID = productoID;
        this.cantidad = cantidad;
    }

    public int getOrdenProduccionID() {
        return ordenProduccionID;
    }

    public void setOrdenProduccionID(int ordenProduccionID) {
        this.ordenProduccionID = ordenProduccionID;
    }

    public int getProductoID() {
        return productoID;
    }

    public void setProductoID(int productoID) {
        this.productoID = productoID;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDescripProducto() {
        return descripProducto;
    }

    public void setDescripProducto(String descripProducto) {
        this.descripProducto = descripProducto;
    }
}
