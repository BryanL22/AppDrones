package co.edu.poli.sw2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Encargada de administrar la conexion JDBC hacia la base de datos MySQL.
 */
public class Conexion {

    // TODO: externalizar estos valores a un archivo de configuracion antes de pasar a produccion
    private static final String URL = "jdbc:mysql://localhost:3306/appdrones?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String PASSWORD = "1234";

    private Connection connection;

    public Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        asegurarEsquema(connection);
        return connection;
    }

    private void asegurarEsquema(Connection conn) throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS drone (" +
                "id_drone INT AUTO_INCREMENT PRIMARY KEY, " +
                "`serial` VARCHAR(100) NOT NULL, " +
                "modelo VARCHAR(100) NOT NULL, " +
                "fabricante VARCHAR(100) NOT NULL, " +
                "peso DOUBLE NOT NULL)";
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
