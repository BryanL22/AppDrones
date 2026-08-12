package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.database.Conexion;
import co.edu.poli.sw2.model.Drone;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del contrato {@link CRUD} para la entidad {@link Drone}.
 */
public class DroneDAO implements CRUD<Drone> {

    private static final String SQL_CREAR_TABLA = "CREATE TABLE IF NOT EXISTS drone (" +
            "id_drone INT AUTO_INCREMENT PRIMARY KEY, " +
            "`serial` VARCHAR(100) NOT NULL, " +
            "modelo VARCHAR(100) NOT NULL, " +
            "fabricante VARCHAR(100) NOT NULL, " +
            "peso DOUBLE NOT NULL)";

    /**
     * Obtiene una conexion activa y garantiza que la tabla {@code drone} exista.
     *
     * @return conexion JDBC lista para usar.
     * @throws SQLException si falla la conexion o la creacion de la tabla.
     */
    private Connection obtenerConexion() throws SQLException {
        Connection connection = new Conexion().getConnection();
        crearTabla(connection, SQL_CREAR_TABLA);
        return connection;
    }

    @Override
    public boolean crear(Drone obj) {
        String sql = "INSERT INTO drone (`serial`, modelo, fabricante, peso) VALUES (?, ?, ?, ?)";

        try (Connection connection = obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, obj.getSerial());
            statement.setString(2, obj.getModelo());
            statement.setString(3, obj.getFabricante());
            statement.setDouble(4, obj.getPeso());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Drone> obtenerTodos() {
        List<Drone> drones = new ArrayList<>();
        String sql = "SELECT id_drone, `serial`, modelo, fabricante, peso FROM drone";

        try (Connection connection = obtenerConexion();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                drones.add(new Drone(
                        resultSet.getInt("id_drone"),
                        resultSet.getString("serial"),
                        resultSet.getString("modelo"),
                        resultSet.getString("fabricante"),
                        resultSet.getDouble("peso")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return drones;
    }

    @Override
    public Drone obtenerPorId(int id) {
        String sql = "SELECT id_drone, `serial`, modelo, fabricante, peso FROM drone WHERE id_drone = ?";

        try (Connection connection = obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Drone(
                            resultSet.getInt("id_drone"),
                            resultSet.getString("serial"),
                            resultSet.getString("modelo"),
                            resultSet.getString("fabricante"),
                            resultSet.getDouble("peso")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean actualizar(Drone obj) {
        String sql = "UPDATE drone SET `serial` = ?, modelo = ?, fabricante = ?, peso = ? WHERE id_drone = ?";

        try (Connection connection = obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, obj.getSerial());
            statement.setString(2, obj.getModelo());
            statement.setString(3, obj.getFabricante());
            statement.setDouble(4, obj.getPeso());
            statement.setInt(5, obj.getIdDrone());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM drone WHERE id_drone = ?";

        try (Connection connection = obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
