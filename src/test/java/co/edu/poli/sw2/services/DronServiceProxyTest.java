package co.edu.poli.sw2.services;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas del patron Proxy: verifican que {@link DronServiceProxy} solo deja
 * pasar la eliminacion cuando la contrasena es correcta.
 *
 * <p>El servicio real se sustituye por un espia que anota las solicitudes que
 * recibe, de modo que se puede comprobar lo importante del patron: que una
 * contrasena incorrecta no alcanza al servicio real.</p>
 */
class DronServiceProxyTest {

    /**
     * Servicio real de mentira: registra las eliminaciones que recibe en
     * lugar de tocar la base de datos.
     */
    private static class ServicioEspia implements ServiceInterface {
        private final List<String> eliminaciones = new ArrayList<>();

        @Override
        public boolean eliminarDron(String id, String password) {
            eliminaciones.add(id);
            return true;
        }
    }

    @Test
    void conPasswordCorrectaLaEliminacionLlegaAlServicioReal() throws Exception {
        ServicioEspia espia = new ServicioEspia();
        ServiceInterface proxy = new DronServiceProxy(espia);

        assertTrue(proxy.eliminarDron("D-1", "1234"));
        assertEquals(List.of("D-1"), espia.eliminaciones);
    }

    @Test
    void conPasswordIncorrectaLaEliminacionNoLlegaAlServicioReal() throws Exception {
        ServicioEspia espia = new ServicioEspia();
        ServiceInterface proxy = new DronServiceProxy(espia);

        assertFalse(proxy.eliminarDron("D-1", "otra"));
        assertTrue(espia.eliminaciones.isEmpty());
    }

    @Test
    void sinPasswordTampocoSeElimina() throws Exception {
        ServicioEspia espia = new ServicioEspia();
        ServiceInterface proxy = new DronServiceProxy(espia);

        assertFalse(proxy.eliminarDron("D-1", null));
        assertFalse(proxy.eliminarDron("D-1", ""));
        assertTrue(espia.eliminaciones.isEmpty());
    }

    @Test
    void verificarPasswordSoloAceptaLaClaveConfigurada() {
        DronServiceProxy proxy = new DronServiceProxy(new DronService());

        assertTrue(proxy.verificarPassword("1234"));
        assertFalse(proxy.verificarPassword("12345"));
    }

    @Test
    void elProxySePuedeUsarDondeSeEsperaElServicio() {
        ServiceInterface proxy = new DronServiceProxy(new DronService());

        assertTrue(proxy instanceof ServiceInterface);
    }
}
