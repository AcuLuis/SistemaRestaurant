
package BEAN;

public class Producto {
   private int productoID;
   private String descripProducto;
   private int cantidad;
   private double precioUnit;
   private int plato;
   private int tipo;

    public Producto() {
    }

    public Producto(int productoID, String descripProducto, int cantidad, double precioUnit, int plato, int tipo) {
        this.productoID = productoID;
        this.descripProducto = descripProducto;
        this.cantidad = cantidad;
        this.precioUnit = precioUnit;
        this.plato = plato;
        this.tipo = tipo;
    }

    public int getProductoID() {
        return productoID;
    }

    public void setProductoID(int productoID) {
        this.productoID = productoID;
    }

    public String getDescripProducto() {
        return descripProducto;
    }

    public void setDescripProducto(String descripProducto) {
        this.descripProducto = descripProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(double precioUnit) {
        this.precioUnit = precioUnit;
    }

    public int getPlato() {
        return plato;
    }

    public void setPlato(int plato) {
        this.plato = plato;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
