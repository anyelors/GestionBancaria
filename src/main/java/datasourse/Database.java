package datasourse;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private static String url;
    private static String user;
    private static String password;

    private static boolean configLoaded;

    static {
        Properties propiedades = new Properties();
        try ( InputStream is = Database.class.getResourceAsStream("/config.properties") ) {
            if (is == null) {
                throw new IOException("Fichero de recursos no encontrado");
            }
            // Cargar propiedades
            propiedades.load ( is );
            // Obtener valores de las propiedades
            url = propiedades.getProperty("url");
            user = propiedades.getProperty("user");
            password = propiedades.getProperty("password");
            configLoaded = true;
        } catch (IOException e) {
            configLoaded = false;
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection() throws SQLException {
        if ( !configLoaded ) throw new SQLException("Config not loaded");
        return DriverManager.getConnection(url, user, password);
    }
}
