
package restaurante.modelo;

public class Usuario {
    
    int usuarioID;
    int empleadoID;
    String usuario;
    String contrasena;
    int tipoUsuario;
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

    public Usuario(int usuarioID, int empleadoID, String usuario, String contrasena, int tipoUsuario, int idLocalR) {
        this.usuarioID = usuarioID;
        this.empleadoID = empleadoID;
        this.usuario = usuario;
        this.contrasena = contrasena;
        this.tipoUsuario = tipoUsuario;
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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public int getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(int tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    
}
