package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para verificar la implementación del patrón de diseño
 * Bridge simplificado:
 * <ul>
 *   <li>{@link ControlDrone} define el contrato para los controles de vuelo.</li>
 *   <li>{@link ControlBasico} y {@link ControlAutonomo} son las implementaciones concretas.</li>
 *   <li>{@link Drone} no tiene ningún campo ni referencia a {@link ControlDrone}.</li>
 *   <li>{@link ControlDrone} se relaciona con {@link Drone} únicamente como parámetro
 *       en {@link ControlDrone#ejecutarAccion(Drone)} (dependencia "usa" en UML).</li>
 * </ul>
 */
class ControlBridgeTest {

    @Test
    void controlBasicoConDronAgricultura() {
        Agricultura agri = new Agricultura("AG-1", "SN-AG1", "AgriMax", "DJI", 15.0, 20.0);
        ControlDrone control = new ControlBasico();

        assertEquals("Control básico", control.getTipoControl());
        assertEquals("Dron con control básico", control.descripcionBreve());

        String accion = control.ejecutarAccion(agri);
        assertNotNull(accion);
        assertTrue(accion.contains("Control básico"));
        assertTrue(accion.contains("AgriMax"));
        assertTrue(accion.contains("AG-1"));
    }

    @Test
    void controlAutonomoConDronVigilancia() {
        Vigilancia vig = new Vigilancia("VG-1", "SN-VG1", "SkyGuard", "DJI", 8.0, true);
        ControlDrone control = new ControlAutonomo();

        assertEquals("Control autónomo", control.getTipoControl());
        assertEquals("Dron con control autónomo", control.descripcionBreve());

        String accion = control.ejecutarAccion(vig);
        assertNotNull(accion);
        assertTrue(accion.contains("Control autónomo"));
        assertTrue(accion.contains("SkyGuard"));
        assertTrue(accion.contains("VG-1"));
    }

    @Test
    void ejecucionDinamicaConDiferentesDronesSinAsociacionPersistente() {
        Agricultura agri = new Agricultura("AG-10", "SN-10", "AgriPro", "DJI", 12.0, 16.0);
        Vigilancia vig = new Vigilancia("VG-20", "SN-20", "SecureFly", "Parrot", 7.0, true);

        ControlDrone control = new ControlBasico();

        // ControlDrone recibe Drone como parámetro puntual (dependencia "usa" en UML):
        // no retiene ni almacena internamente la referencia al dron.
        String accionAgri = control.ejecutarAccion(agri);
        assertTrue(accionAgri.contains("AgriPro"));

        String accionVig = control.ejecutarAccion(vig);
        assertTrue(accionVig.contains("SecureFly"));
    }

    @Test
    void factoriaControlDroneCrearInstanciaCorrectamente() {
        ControlDrone c1 = ControlDrone.crear("Control básico");
        assertInstanceOf(ControlBasico.class, c1);
        assertEquals("Control básico", c1.getTipoControl());

        ControlDrone c2 = ControlDrone.crear("Control autónomo");
        assertInstanceOf(ControlAutonomo.class, c2);
        assertEquals("Control autónomo", c2.getTipoControl());
    }

    @Test
    void descripcionBreveEsExactamenteLaSolicitadaPorNegocio() {
        ControlDrone basico = ControlDrone.crear("Control básico");
        assertEquals("Dron con control básico", basico.descripcionBreve());

        ControlDrone autonomo = ControlDrone.crear("Control autónomo");
        assertEquals("Dron con control autónomo", autonomo.descripcionBreve());
    }

    @Test
    void droneNoTieneNingunaResponsabilidadDelBridge() {
        Drone drone = new Drone("D-2", "SN-2", "ModelY", "Maker", 6.0);
        Drone clon = drone.clone();

        // Drone no expone ningún atributo ni método relacionado con el tipo de control.
        assertNotNull(clon);
        assertEquals(drone.getId(), clon.getId());
    }

    @Test
    void droneBuilderNoConoceElTipoDeControl() {
        Drone drone = new DroneBuilder()
                .id("D-BUILD")
                .serial("SN-B")
                .construir();

        assertEquals("D-BUILD", drone.getId());
    }
}
