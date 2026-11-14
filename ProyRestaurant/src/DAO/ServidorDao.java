
package DAO;

import BEAN.Servidor;
import UTIL.DbBean;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ServidorDao {
        
      public boolean servidor(String servidor){
            
            DbBean con = new DbBean();
            
        String sql = "Select nomServidor from servidor ";
               sql =  sql + " where nomServidor = '"+servidor+"'";
        try{
            ResultSet resultado = con.resultadoSQL(sql);
            Servidor servi = new Servidor();
            while(resultado.next()){

                servi.setNomServidor(resultado.getString(1));
                if (servi.getNomServidor().isEmpty() || servi.getNomServidor().equals("")){
                    
                    con.conecta();
                    return false;
                }else{
                    
                    con.conectaConBd(servi.getNomServidor());
                    return true;
                }

            }
            
        }catch(java.sql.SQLException e){
            e.printStackTrace();
        }
        try{
            con.desconecta();
        }catch(SQLException e){}
        return false;
    }
}
