
package restaurante.modelo;

public class TipInsumo {
    
    int idTipInsumo;
    String descripcion;

    public TipInsumo() {
    }

    public TipInsumo(int idTipInsumo) {
        this.idTipInsumo = idTipInsumo;
    }

    public TipInsumo(int idTipInsumo, String descripcion) {
        this.idTipInsumo = idTipInsumo;
        this.descripcion = descripcion;
    }

    public int getIdTipInsumo() {
        return idTipInsumo;
    }

    public void setIdTipInsumo(int idTipInsumo) {
        this.idTipInsumo = idTipInsumo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
