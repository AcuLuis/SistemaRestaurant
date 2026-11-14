
package restaurante.dao;

import restaurante.util.DbBean;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {

    public LoginDao() {
    }
    
    public static boolean consultaLogin(String u, String p){
        DbBean con = new DbBean();
        boolean bool = false;
        int nivel = 0;
        try{
            String sql = "select * from usuario where usuario = '"+ u +"' and pw = '"+ p +"'";
            ResultSet resultado = con.resultadoSQL(sql);
            if(resultado.next()){
               bool = true;
            }
        }catch(Exception e){
            e.printStackTrace();
            bool = false;
        }
        try{
            con.desconecta();
        }catch(SQLException e){
        }
        return bool;
    }
}
