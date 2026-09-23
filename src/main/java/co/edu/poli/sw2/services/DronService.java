package co.edu.poli.sw2.services;

import co.edu.poli.sw2.dao.DroneDAO;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servicio real (Service) del patron Proxy: contiene la operacion de
 * eliminacion de verdad.
 *
 * <p>Reutiliza el {@link DroneDAO} que ya existe para borrar el dron de la
 * base de datos; no implementa persistencia propia. Tampoco valida la
 * contrasena: eso es tarea del {@link DronServiceProxy}. Si la solicitud le
 * llega, la ejecuta.</p>
 */
public class DronService implements ServiceInterface {

    DroneDAO droneDAO = new DroneDAO();

    /**
     * Elimina el dron delegando en el DAO existente. Ignora la contrasena
     * porque el control de acceso ya lo hizo el proxy.
     */
    @Override
    public boolean eliminarDron(String id, String password) throws SQLException, IOException {
        return droneDAO.eliminar(id);
    }
}
