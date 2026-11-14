
package restaurante.modelo;

public class Plato {
    
   private int idPlato;
   private String descPlato;

    public Plato() {
    }

    public Plato(int idPlato, String descPlato) {
        this.idPlato = idPlato;
        this.descPlato = descPlato;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public void setIdPlato(int idPlato) {
        this.idPlato = idPlato;
    }

    public String getDescPlato() {
        return descPlato;
    }

    public void setDescPlato(String descPlato) {
        this.descPlato = descPlato;
    }
}
