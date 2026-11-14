package restaurante.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import restaurante.datos.ConexionBD;

public class CreaBD {

    private static final Logger LOG = Logger.getLogger(CreaBD.class.getName());

    public void accesoBDCrea(String sql) {
        try (Connection connection = ConexionBD.abrirServidor();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            LOG.log(Level.SEVERE, "Ocurrió un error al crear la base de datos", e);
        }
    }

}
