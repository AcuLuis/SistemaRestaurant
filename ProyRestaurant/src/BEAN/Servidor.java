
package BEAN;

public class Servidor {
    
    int servidorID;
    String nomServidor;

    public Servidor() {
    }

    public Servidor(int servidorID) {
        this.servidorID = servidorID;
    }

    public Servidor(int servidorID, String nomServidor) {
        this.servidorID = servidorID;
        this.nomServidor = nomServidor;
    }

    public int getServidorID() {
        return servidorID;
    }

    public void setServidorID(int servidorID) {
        this.servidorID = servidorID;
    }

    public String getNomServidor() {
        return nomServidor;
    }

    public void setNomServidor(String nomServidor) {
        this.nomServidor = nomServidor;
    }
}
