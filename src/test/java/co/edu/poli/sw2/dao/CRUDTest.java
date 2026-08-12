package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.database.Conexion;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integracion para el metodo por defecto {@link CRUD#crearTabla}.
 * Se ejecutan contra la base de datos configurada en {@code .env} y usan
 * una tabla desechable que se elimina despues de cada prueba.
 */
class CRUDTest {

    private static final String TABLA_PRUEBA = "test_crud_tabla";
    private static final String SQL_CREAR = "CREATE TABLE IF NOT EXISTS " + TABLA_PRUEBA + " (id INT PRIMARY KEY)";

    // Cualquier implementacion de CRUD sirve para probar el metodo por
    // defecto; se usa DroneDAO por ser la unica disponible en el proyecto.
    private final DroneDAO crud = new DroneDAO();

    @AfterEach
    void limpiarTablaDePrueba() throws SQLException {
        try (Connection connection = new Conexion().getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DROP TABLE IF EXISTS " + TABLA_PRUEBA);
        }
    }

    @Test
    void crearTablaCreaLaTablaSiNoExiste() throws SQLException {
        try (Connection connection = new Conexion().getConnection()) {
            crud.crearTabla(connection, SQL_CREAR);

            assertTrue(existeTabla(connection));
        }
    }

    @Test
    void crearTablaEsIdempotente() throws SQLException {
        try (Connection connection = new Conexion().getConnection()) {
            crud.crearTabla(connection, SQL_CREAR);

            assertDoesNotThrow(() -> crud.crearTabla(connection, SQL_CREAR));
            assertTrue(existeTabla(connection));
        }
    }

    private boolean existeTabla(Connection connection) throws SQLException {
        try (var resultSet = connection.getMetaData().getTables(null, null, TABLA_PRUEBA, null)) {
            return resultSet.next();
        }
    }
}
