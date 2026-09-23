package co.edu.poli.sw2.services;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Proxy del patron Proxy: intermediario entre el controlador y
 * {@link DronService}.
 *
 * <p>Implementa la misma interfaz que el servicio real, asi que el
 * controlador lo usa igual. Compara la contrasena recibida con
 * {@link #PASSWORD} y solo si coincide delega la eliminacion; si no,
 * devuelve {@code false} y la solicitud nunca llega al servicio real, al DAO
 * ni a la base de datos.</p>
 *
 * <p>La contrasena llega como parametro: quien la pide al usuario es la
 * vista, para que esta capa no dependa de JavaFX.</p>
 */
public class DronServiceProxy implements ServiceInterface {

    /**
     * Contrasena unica que autoriza el borrado de cualquier dron. Es una
     * constante de demostracion del patron, no un mecanismo de seguridad real.
     */
    static final String PASSWORD = "1234";

    ServiceInterface dronService;

    /**
     * Crea el proxy sobre el servicio real que debe proteger.
     *
     * @param dronService servicio al que se delega la eliminacion autorizada.
     */
    public DronServiceProxy(ServiceInterface dronService) {
        this.dronService = dronService;
    }

    /**
     * Comprueba si la contrasena recibida autoriza el borrado.
     *
     * @param password contrasena escrita por el usuario.
     * @return {@code true} si coincide con {@link #PASSWORD}.
     */
    public boolean verificarPassword(String password) {
        return PASSWORD.equals(password);
    }

    /**
     * Solo pasa la eliminacion al servicio real si la contrasena es correcta.
     */
    @Override
    public boolean eliminarDron(String id, String password) throws SQLException, IOException {
        if (verificarPassword(password)) {
            return dronService.eliminarDron(id, password);
        }

        return false;
    }
}
