package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Agricultura;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del contrato {@link CRUD} para la entidad {@link Agricultura}.
 *
 * <p>Sigue el patron de herencia por tabla: los campos comunes de dron viven
 * en la tabla {@code drone} (a traves de {@link DroneDAO}) y los campos
 * propios de esta especializacion en la tabla {@code agricultura}, ligada a
 * la primera mediante {@code id_drone}. El identificador lo asigna el
 * usuario manualmente (no se genera de forma automatica).</p>
 */
public class AgriculturaDAO implements CRUD<Agricultura> {

    private static final String SQL_CREAR_TABLA = "CREATE TABLE IF NOT EXISTS agricultura (" +
            "id_drone VARCHAR(100) PRIMARY KEY, " +
            "capacidad_tanque DOUBLE NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private final DroneDAO droneDAO = new DroneDAO();

    private Connection obtenerConexion() throws SQLException {
        Connection connection = droneDAO.obtenerConexion();
        crearTabla(connection, SQL_CREAR_TABLA);
        return connection;
    }

    @Override
    public boolean crear(Agricultura obj) {
        String sqlDrone = "INSERT INTO drone (id, `serial`, modelo, fabricante, peso) VALUES (?, ?, ?, ?, ?)";
        String sqlAgricultura = "INSERT INTO agricultura (id_drone, capacidad_tanque) VALUES (?, ?)";

        try (Connection connection = obtenerConexion()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementDrone = connection.prepareStatement(sqlDrone);
                 PreparedStatement statementAgricultura = connection.prepareStatement(sqlAgricultura)) {

                statementDrone.setString(1, obj.getId());
                statementDrone.setString(2, obj.getSerial());
                statementDrone.setString(3, obj.getModelo());
                statementDrone.setString(4, obj.getFabricante());
                statementDrone.setDouble(5, obj.getPeso());
                statementDrone.executeUpdate();

                statementAgricultura.setString(1, obj.getId());
                statementAgricultura.setDouble(2, obj.getCapacidadTanque());
                statementAgricultura.executeUpdate();

                connection.commit();
                return true;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Agricultura> obtenerTodos() {
        List<Agricultura> resultado = new ArrayList<>();
        String sql = "SELECT d.id, d.`serial`, d.modelo, d.fabricante, d.peso, a.capacidad_tanque " +
                "FROM drone d INNER JOIN agricultura a ON d.id = a.id_drone";

        try (Connection connection = obtenerConexion();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                resultado.add(mapear(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultado;
    }

    @Override
    public Agricultura obtenerPorId(String id) {
        String sql = "SELECT d.id, d.`serial`, d.modelo, d.fabricante, d.peso, a.capacidad_tanque " +
                "FROM drone d INNER JOIN agricultura a ON d.id = a.id_drone WHERE d.id = ?";

        try (Connection connection = obtenerConexion();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapear(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean actualizar(Agricultura obj) {
        String sqlDrone = "UPDATE drone SET `serial` = ?, modelo = ?, fabricante = ?, peso = ? WHERE id = ?";
        String sqlAgricultura = "UPDATE agricultura SET capacidad_tanque = ? WHERE id_drone = ?";

        try (Connection connection = obtenerConexion()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statementDrone = connection.prepareStatement(sqlDrone);
                 PreparedStatement statementAgricultura = connection.prepareStatement(sqlAgricultura)) {

                statementDrone.setString(1, obj.getSerial());
                statementDrone.setString(2, obj.getModelo());
                statementDrone.setString(3, obj.getFabricante());
                statementDrone.setDouble(4, obj.getPeso());
                statementDrone.setString(5, obj.getId());
                int filasDrone = statementDrone.executeUpdate();

                statementAgricultura.setDouble(1, obj.getCapacidadTanque());
                statementAgricultura.setString(2, obj.getId());
                int filasAgricultura = statementAgricultura.executeUpdate();

                connection.commit();
                return filasDrone > 0 && filasAgricultura > 0;
            } catch (SQLException e) {
                connection.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(String id) {
        // La fila de "agricultura" se elimina en cascada (ON DELETE CASCADE) al borrar el dron base.
        return droneDAO.eliminar(id);
    }

    private Agricultura mapear(ResultSet resultSet) throws SQLException {
        return new Agricultura(
                resultSet.getString("id"),
                resultSet.getString("serial"),
                resultSet.getString("modelo"),
                resultSet.getString("fabricante"),
                resultSet.getDouble("peso"),
                resultSet.getDouble("capacidad_tanque")
        );
    }
}
