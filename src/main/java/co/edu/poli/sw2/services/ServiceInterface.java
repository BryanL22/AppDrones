package co.edu.poli.sw2.services;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Interfaz comun (Service Interface) del patron Proxy.
 *
 * <p>Declara la unica operacion que el patron protege: eliminar un dron. La
 * implementan tanto el servicio real ({@link DronService}) como el proxy
 * ({@link DronServiceProxy}), y gracias a eso el controlador puede recibir
 * cualquiera de los dos sin notar la diferencia.</p>
 */
public interface ServiceInterface {

    /**
     * Elimina el dron indicado.
     *
     * @param id identificador del dron a eliminar.
     * @param password contrasena que el proxy valida antes de permitir el
     *                 borrado; el servicio real la ignora.
     * @return {@code true} si el dron se elimino.
     * @throws SQLException si falla la consulta a la base de datos.
     * @throws IOException si falla la lectura de la configuracion de conexion.
     */
    boolean eliminarDron(String id, String password) throws SQLException, IOException;
}
