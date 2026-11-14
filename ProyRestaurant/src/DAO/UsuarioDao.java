
package DAO;

import BEAN.*;
import UTIL.DbBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class UsuarioDao {

    Usuario user = new Usuario();
    LocalR local = new LocalR();
    boolean val = false;
    
    public UsuarioDao() {
    }
    
    public int consulta(String usuario, String contraseña){
        
        DbBean con = new DbBean();
        int id=0;
        String sql = "";
        
        try{
            
            sql = "select empleadoID  \n";
            sql = sql + " from usuario \n";
            sql = sql +" where usuario = '"+usuario+"' and contraseña = '"+contraseña+"' ";
            
            
            ResultSet resultado = con.resultadoSQL(sql);
            while(resultado.next()){
                user.setEmpleadoID(resultado.getInt(1));
            }
            id = user.getEmpleadoID();
            
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(SQLException e){
        }
        
        return id;
    }
    
    
    // consulta si los datos existen en la BD
    
    public boolean consultaLogin(String usuario, String contraseña, String localID){
        
        DbBean con = new DbBean();
        
        String sql = "";
        
        try{
            
            sql = "select usuario, contraseña, idLocalR \n";
            sql = sql + " from usuario \n";
            sql = sql +" where usuario = '"+usuario+"' and contraseña = '"+contraseña+"' and idLocalR = '"+localID+"' ";
            
            
            ResultSet resultado = con.resultadoSQL(sql);
            while(resultado.next()){
                user.setUsuario(resultado.getString(1));
                user.setContraseña(resultado.getString(2));
                local.setIdLocalR(resultado.getInt(3));
                
                String idLocal = Integer.toString(local.getIdLocalR());
                
                if(user.getUsuario().isEmpty() && user.getContraseña().isEmpty() && idLocal.isEmpty()){
                    return false;
                }else{
                    return true;
                } 
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(SQLException e){
        }
        
        return false;
    }
    
    //consulta si es un tipo de usuario 1 --> que puede ver todo
    // sino no podra ver algunos menus.
    
    public int confirmaUsuario (String usuario, String contraseña,String localID){
        
        DbBean con = new DbBean();
        int resp=0;
        String sql = "";
        String tipUsuario;
        
        try{
            
            sql = "select tipoUsuario\n";
            sql = sql + " from usuario ";
            sql = sql +" where usuario = '"+usuario+"' and contraseña = '"+contraseña+"' and idLocalR = '"+localID+"' ";
            
            
            ResultSet resultado = con.resultadoSQL(sql);
            while(resultado.next()){
                user.setTipUsuaruario(resultado.getInt(1));
                
                tipUsuario = Integer.toString(user.getTipUsuaruario());
                
                if(tipUsuario.equals("1")){
                    resp = 1;
                    return resp;
                }else if(tipUsuario.equals("2")) {
                    resp = 2;
                    return resp;
                } else if(tipUsuario.equals("3")){
                    resp = 3;
                    return resp;
                }else{
                    resp = 4;
                    return resp;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(SQLException e){
        }
        return resp;    
    }
    
    
    public Vector<Usuario> listaUsuario(boolean valida, String cadena){
    
        String sql;
        Vector<Usuario> listaUsuario = new Vector<Usuario>();
        DbBean con = new DbBean();
    
        sql = "select * from Usuario ";
   
        if (valida == true){
           sql = sql + " where usuarioID like '"+ cadena +"%'"+ " or "+ " usuario like '"+cadena+"%' ";
        }
            
        try {
                ResultSet resul = con.resultadoSQL(sql);
            
            while(resul.next()){
                Usuario usu = new Usuario();
                usu.setUsuarioID(resul.getInt(1));
                usu.setIdLocalR(resul.getInt(2));
                usu.setUsuario(resul.getString(3));
                usu.setContraseña(resul.getString(4));
                usu.setTipUsuaruario(resul.getInt(5));
                usu.setEmpleadoID(resul.getInt(6));

                listaUsuario.addElement(usu);
            }
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        return listaUsuario;
    }
    public void eliminarUsuario(Usuario user){
    
        String sql;
        DbBean con = new DbBean();
        
        sql = "delete from Usuario";
        sql = sql+ " where usuarioID = "+user.getUsuarioID()+" ";
        
        try{
            con.ejecutaSQL(sql);
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }
    public void insertaUsuario(Usuario user){
        
        String sql;
        DbBean con = new DbBean();
        
        sql = "insert into Usuario values ( ";
        sql = sql +user.getUsuarioID()+", "+user.getIdLocalR()+", '"+user.getUsuario()+"', '";
        sql = sql +user.getContraseña()+"', "+user.getTipUsuaruario()+", "+user.getEmpleadoID()+") ";

        try{
            System.out.println(sql);
            con.ejecutaSQL(sql);
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }
    
    public void actualizarUsuario(Usuario user){

        String sql;
        DbBean con = new DbBean();
        
        sql = "update Usuario set ";
        sql = sql+ " idLocalR = "+user.getIdLocalR()+", ";
        sql = sql+ " usuario = '"+user.getUsuario()+"', ";
        sql = sql+ " tipoUsuario = "+user.getTipUsuaruario()+", ";
        sql = sql+ " empleadoID = "+user.getEmpleadoID()+" ";
        
        sql = sql+ " where usuarioID = "+user.getUsuarioID()+" ";
        
        try{
            con.ejecutaSQL(sql);
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
    }
    
}
