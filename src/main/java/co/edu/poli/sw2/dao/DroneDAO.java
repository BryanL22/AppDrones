package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import co.edu.poli.sw2.services.Conexion;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementacion del contrato {@link CRUD} para la entidad {@link Drone} y
 * sus especializaciones {@link Agricultura} y {@link Vigilancia}.
 *
 * <p>Sigue el patron de herencia por tabla: los campos comunes viven en
 * {@code drone} y los propios de cada especializacion en {@code agricultura}
 * / {@code vigilancia}, relacionadas mediante {@code id_drone}
 * (con {@code ON DELETE CASCADE}). En lugar de tener un DAO por cada
 * subclase, esta unica clase decide con {@code instanceof} que tabla
 * adicional leer, escribir o actualizar segun el tipo real del objeto.</p>
 *
 * <p>El identificador de cada dron lo asigna el usuario manualmente (no se
 * genera de forma automatica) y es la llave primaria de {@code drone}.</p>
 *
 * <p>La conexion JDBC se obtiene del servicio Singleton {@link Conexion} y
 * se comparte entre todas las operaciones (no se cierra al terminar cada
 * una): cada metodo solo cierra sus propios {@link Statement}/
 * {@link ResultSet}.</p>
 */
public class DroneDAO implements CRUD<Drone> {

    private static final String SQL_CREAR_TABLA_DRONE = "CREATE TABLE IF NOT EXISTS drone (" +
            "id VARCHAR(100) PRIMARY KEY, " +
            "`serial` VARCHAR(100) NOT NULL, " +
            "modelo VARCHAR(100) NOT NULL, " +
            "fabricante VARCHAR(100) NOT NULL, " +
            "peso DOUBLE NOT NULL)";

    private static final String SQL_CREAR_TABLA_AGRICULTURA = "CREATE TABLE IF NOT EXISTS agricultura (" +
            "id_drone VARCHAR(100) PRIMARY KEY, " +
            "capacidad_tanque DOUBLE NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_CREAR_TABLA_VIGILANCIA = "CREATE TABLE IF NOT EXISTS vigilancia (" +
            "id_drone VARCHAR(100) PRIMARY KEY, " +
            "deteccion_termica BOOLEAN NOT NULL, " +
            "FOREIGN KEY (id_drone) REFERENCES drone(id) ON DELETE CASCADE)";

    private static final String SQL_SELECT_BASE =
            "SELECT d.id, d.`serial`, d.modelo, d.fabricante, d.peso, " +
                    "a.capacidad_tanque, v.deteccion_termica " +
                    "FROM drone d " +
                    "LEFT JOIN agricultura a ON d.id = a.id_drone " +
                    "LEFT JOIN vigilancia v ON d.id = v.id_drone";

    /**
     * Crea el DAO. No recibe dependencias: la conexion se obtiene del
     * servicio Singleton {@link Conexion} en cada operacion.
     */
    public DroneDAO() {
    }

    /**
     * Obtiene la conexion compartida (Singleton) y garantiza que las tres
     * tablas existan. La conexion no se cierra aqui: la administra
     * {@link Conexion} durante toda la vida de la aplicacion.
     *
     * @return conexion JDBC lista para usar.
     * @throws SQLException si falla la conexion o la creacion de alguna tabla.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    private Connection obtenerConexion() throws SQLException, IOException {
        Connection connection = Conexion.obtenerInstancia().getConnection();
        crearTabla(connection, SQL_CREAR_TABLA_DRONE);
        crearTabla(connection, SQL_CREAR_TABLA_AGRICULTURA);
        crearTabla(connection, SQL_CREAR_TABLA_VIGILANCIA);
        return connection;
    }

    @Override
    public boolean crear(Drone obj) throws SQLException, IOException {
        String sql = "INSERT INTO drone (id, `serial`, modelo, fabricante, peso) VALUES (?, ?, ?, ?, ?)";

        Connection connection = obtenerConexion();
        connection.setAutoCommit(false);

        boolean exito = false;
        try {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, obj.getId());
                statement.setString(2, obj.getSerial());
                statement.setString(3, obj.getModelo());
                statement.setString(4, obj.getFabricante());
                statement.setDouble(5, obj.getPeso());
                statement.executeUpdate();

                insertarEspecializacion(connection, obj);
            }

            connection.commit();
            exito = true;
            return true;
        } finally {
            if (!exito) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    private void insertarEspecializacion(Connection connection, Drone obj) throws SQLException {
        if (obj instanceof Agricultura agricultura) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO agricultura (id_drone, capacidad_tanque) VALUES (?, ?)")) {
                statement.setString(1, obj.getId());
                statement.setDouble(2, agricultura.getCapacidadTanque());
                statement.executeUpdate();
            }
        } else if (obj instanceof Vigilancia vigilancia) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO vigilancia (id_drone, deteccion_termica) VALUES (?, ?)")) {
                statement.setString(1, obj.getId());
                statement.setBoolean(2, vigilancia.isDeteccionTermica());
                statement.executeUpdate();
            }
        }
    }

    @Override
    public List<Drone> obtenerTodos() throws SQLException, IOException {
        List<Drone> drones = new ArrayList<>();

        Connection connection = obtenerConexion();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_BASE)) {

            while (resultSet.next()) {
                drones.add(mapearDrone(resultSet));
            }
        }

        return drones;
    }

    @Override
    public Drone obtenerPorId(String id) throws SQLException, IOException {
        String sql = SQL_SELECT_BASE + " WHERE d.id = ?";

        Connection connection = obtenerConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapearDrone(resultSet);
                }
            }
        }

        return null;
    }

    @Override
    public boolean actualizar(Drone obj) throws SQLException, IOException {
        String sql = "UPDATE drone SET `serial` = ?, modelo = ?, fabricante = ?, peso = ? WHERE id = ?";

        Connection connection = obtenerConexion();
        connection.setAutoCommit(false);

        boolean exito = false;
        try {
            int filasDrone;
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, obj.getSerial());
                statement.setString(2, obj.getModelo());
                statement.setString(3, obj.getFabricante());
                statement.setDouble(4, obj.getPeso());
                statement.setString(5, obj.getId());
                filasDrone = statement.executeUpdate();
            }

            int filasEspecializacion = actualizarEspecializacion(connection, obj);

            connection.commit();
            exito = true;
            return filasDrone > 0 && filasEspecializacion > 0;
        } finally {
            if (!exito) {
                connection.rollback();
            }
            connection.setAutoCommit(true);
        }
    }

    private int actualizarEspecializacion(Connection connection, Drone obj) throws SQLException {
        if (obj instanceof Agricultura agricultura) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE agricultura SET capacidad_tanque = ? WHERE id_drone = ?")) {
                statement.setDouble(1, agricultura.getCapacidadTanque());
                statement.setString(2, obj.getId());
                return statement.executeUpdate();
            }
        }
        if (obj instanceof Vigilancia vigilancia) {
            try (PreparedStatement statement = connection.prepareStatement(
                    "UPDATE vigilancia SET deteccion_termica = ? WHERE id_drone = ?")) {
                statement.setBoolean(1, vigilancia.isDeteccionTermica());
                statement.setString(2, obj.getId());
                return statement.executeUpdate();
            }
        }
        // El dron no tiene especializacion: no hay una segunda tabla que actualizar.
        return 1;
    }

    @Override
    public boolean eliminar(String id) throws SQLException, IOException {
        // Las filas de "agricultura"/"vigilancia" se eliminan en cascada (ON DELETE CASCADE).
        String sql = "DELETE FROM drone WHERE id = ?";

        Connection connection = obtenerConexion();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, id);
            return statement.executeUpdate() > 0;
        }
    }

    private Drone mapearDrone(ResultSet resultSet) throws SQLException {
        String id = resultSet.getString("id");
        String serial = resultSet.getString("serial");
        String modelo = resultSet.getString("modelo");
        String fabricante = resultSet.getString("fabricante");
        double peso = resultSet.getDouble("peso");

        double capacidadTanque = resultSet.getDouble("capacidad_tanque");
        if (!resultSet.wasNull()) {
            return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
        }

        boolean deteccionTermica = resultSet.getBoolean("deteccion_termica");
        if (!resultSet.wasNull()) {
            return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
        }

        return new Drone(id, serial, modelo, fabricante, peso);
    }
}
