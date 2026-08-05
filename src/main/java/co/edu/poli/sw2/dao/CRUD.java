package co.edu.poli.sw2.dao;

import java.util.List;

/**
 * Contrato generico de persistencia para las operaciones basicas
 * de creacion, lectura, actualizacion y eliminacion (CRUD).
 *
 * @param <T> tipo de entidad sobre la cual se aplican las operaciones.
 */
public interface CRUD<T> {

    boolean crear(T obj);

    List<T> obtenerTodos();

    T obtenerPorId(int id);

    boolean actualizar(T obj);

    boolean eliminar(int id);
}
