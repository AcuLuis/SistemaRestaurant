
package restaurante.modelo;

import java.math.BigDecimal;

public class Insumo {
    
    int insumoID;
    String unidadMedida;
    String descripInsumo;
    BigDecimal precioUnit;
    BigDecimal cantidad;
    int idTipInsumo;

    public Insumo() {
    }

    public Insumo(int insumoID) {
        this.insumoID = insumoID;
    }

    public Insumo(int insumoID, int idTipInsumo) {
        this.insumoID = insumoID;
        this.idTipInsumo = idTipInsumo;
    }

    public Insumo(int insumoID, String unidadMedida, String descripInsumo, BigDecimal precioUnit, BigDecimal cantidad, int idTipInsumo) {
        this.insumoID = insumoID;
        this.unidadMedida = unidadMedida;
        this.descripInsumo = descripInsumo;
        this.precioUnit = precioUnit;
        this.cantidad = cantidad;
        this.idTipInsumo = idTipInsumo;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdTipInsumo() {
        return idTipInsumo;
    }

    public void setIdTipInsumo(int idTipInsumo) {
        this.idTipInsumo = idTipInsumo;
    }
    
    public int getInsumoID() {
        return insumoID;
    }

    public void setInsumoID(int insumoID) {
        this.insumoID = insumoID;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public String getDescripInsumo() {
        return descripInsumo;
    }

    public void setDescripInsumo(String descripInsumo) {
        this.descripInsumo = descripInsumo;
    }

    public BigDecimal getPrecioUnit() {
        return precioUnit;
    }

    public void setPrecioUnit(BigDecimal precioUnit) {
        this.precioUnit = precioUnit;
    }
}
