package co.edu.poli.sw2.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Contrato generico de persistencia para las operaciones basicas
 * de creacion, lectura, actualizacion y eliminacion (CRUD).
 *
 * @param <T> tipo de entidad sobre la cual se aplican las operaciones.
 */
public interface CRUD<T> {

    /**
     * Inserta una nueva entidad en la base de datos.
     *
     * @param obj entidad a crear.
     * @return {@code true} si la insercion se realizo correctamente.
     * @throws SQLException si ocurre un error de acceso a datos.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    boolean crear(T obj) throws SQLException, IOException;

    /**
     * Recupera todas las entidades almacenadas.
     *
     * @return lista con todas las entidades encontradas; vacia si no hay registros.
     * @throws SQLException si ocurre un error de acceso a datos.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    List<T> obtenerTodos() throws SQLException, IOException;

    /**
     * Busca una entidad por su identificador.
     *
     * @param id identificador de la entidad.
     * @return la entidad encontrada, o {@code null} si no existe.
     * @throws SQLException si ocurre un error de acceso a datos.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    T obtenerPorId(String id) throws SQLException, IOException;

    /**
     * Actualiza los datos de una entidad existente.
     *
     * @param obj entidad con los datos actualizados.
     * @return {@code true} si la actualizacion se realizo correctamente.
     * @throws SQLException si ocurre un error de acceso a datos.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    boolean actualizar(T obj) throws SQLException, IOException;

    /**
     * Elimina una entidad a partir de su identificador.
     *
     * @param id identificador de la entidad a eliminar.
     * @return {@code true} si la eliminacion se realizo correctamente.
     * @throws SQLException si ocurre un error de acceso a datos.
     * @throws IOException si no se pudo leer la configuracion de conexion.
     */
    boolean eliminar(String id) throws SQLException, IOException;

    /**
     * Crea la tabla de la entidad en la base de datos si todavia no existe.
     *
     * <p>Cada implementacion de {@link CRUD} es responsable de invocar este
     * metodo con la sentencia {@code CREATE TABLE IF NOT EXISTS} propia de
     * su entidad antes de operar sobre la tabla.</p>
     *
     * @param connection conexion JDBC activa sobre la cual ejecutar la sentencia.
     * @param sentenciaCreacion sentencia SQL de creacion de la tabla.
     * @throws SQLException si ocurre un error al ejecutar la sentencia.
     */
    default void crearTabla(Connection connection, String sentenciaCreacion) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(sentenciaCreacion);
        }
    }
}
