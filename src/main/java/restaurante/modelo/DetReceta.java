package restaurante.modelo;

import java.math.BigDecimal;

/** Insumo y cantidad necesarios para preparar una unidad del producto de la receta. */
public class DetReceta {

    private int recetaID;
    private int insumoID;
    private BigDecimal cantidad;
    /** Datos del insumo, solo para mostrar. */
    private String descripInsumo;
    private String unidadMedida;

    public DetReceta() {
    }

    public DetReceta(int insumoID, BigDecimal cantidad) {
        this.insumoID = insumoID;
        this.cantidad = cantidad;
    }

    public int getRecetaID() {
        return recetaID;
    }

    public void setRecetaID(int recetaID) {
        this.recetaID = recetaID;
    }

    public int getInsumoID() {
        return insumoID;
    }

    public void setInsumoID(int insumoID) {
        this.insumoID = insumoID;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripInsumo() {
        return descripInsumo;
    }

    public void setDescripInsumo(String descripInsumo) {
        this.descripInsumo = descripInsumo;
    }

    public String getUnidadMedida() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida = unidadMedida;
    }
}
