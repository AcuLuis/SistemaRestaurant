
package restaurante.modelo;

public class Usuario {
    
    int usuarioID;
    int empleadoID;
    String usuario;
    String contraseña;
    int tipUsuaruario;
    int idLocalR;

    public Usuario() {
    }

    public Usuario(int usuarioID) {
        this.usuarioID = usuarioID;
    }
    

    public Usuario(int usuarioID, int empleadoID) {
        this.usuarioID = usuarioID;
        this.empleadoID = empleadoID;
    }

    public Usuario(int usuarioID, int empleadoID, String usuario, String contraseña, int tipUsuaruario, int idLocalR) {
        this.usuarioID = usuarioID;
        this.empleadoID = empleadoID;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.tipUsuaruario = tipUsuaruario;
        this.idLocalR = idLocalR;
    }

    public int getIdLocalR() {
        return idLocalR;
    }

    public void setIdLocalR(int idLocalR) {
        this.idLocalR = idLocalR;
    }

    public int getUsuarioID() {
        return usuarioID;
    }

    public void setUsuarioID(int usuarioID) {
        this.usuarioID = usuarioID;
    }

    public int getEmpleadoID() {
        return empleadoID;
    }

    public void setEmpleadoID(int empleadoID) {
        this.empleadoID = empleadoID;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public int getTipUsuaruario() {
        return tipUsuaruario;
    }

    public void setTipUsuaruario(int tipUsuaruario) {
        this.tipUsuaruario = tipUsuaruario;
    }

    
}
