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

    @Override
    public boolean crear(Drone obj) {
        String sql = "INSERT INTO drone (`serial`, modelo, fabricante, peso) VALUES (?, ?, ?, ?)";

        try (Connection connection = new Conexion().getConnection();
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

        try (Connection connection = new Conexion().getConnection();
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
        // TODO: implementar consulta de un Drone por su identificador
        return null;
    }

    @Override
    public boolean actualizar(Drone obj) {
        // TODO: implementar actualizacion de un Drone existente
        return false;
    }

    @Override
    public boolean eliminar(int id) {
        // TODO: implementar eliminacion de un Drone por su identificador
        return false;
    }
}
