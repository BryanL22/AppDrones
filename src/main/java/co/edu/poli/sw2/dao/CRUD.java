package co.edu.poli.sw2.dao;

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
     */
    boolean crear(T obj);

    /**
     * Recupera todas las entidades almacenadas.
     *
     * @return lista con todas las entidades encontradas; vacia si no hay registros.
     */
    List<T> obtenerTodos();

    /**
     * Busca una entidad por su identificador.
     *
     * @param id identificador de la entidad.
     * @return la entidad encontrada, o {@code null} si no existe.
     */
    T obtenerPorId(int id);

    /**
     * Actualiza los datos de una entidad existente.
     *
     * @param obj entidad con los datos actualizados.
     * @return {@code true} si la actualizacion se realizo correctamente.
     */
    boolean actualizar(T obj);

    /**
     * Elimina una entidad a partir de su identificador.
     *
     * @param id identificador de la entidad a eliminar.
     * @return {@code true} si la eliminacion se realizo correctamente.
     */
    boolean eliminar(int id);

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
