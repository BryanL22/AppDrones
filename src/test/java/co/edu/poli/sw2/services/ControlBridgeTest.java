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
 * Bridge: {@link ControlDrone} (Implementor) con sus implementaciones
 * concretas {@link ControlBasico} y {@link ControlAutonomo}, y
 * {@link ControlVuelo} (Abstracción) que las asocia con un {@link Drone}
 * sin que este ultimo conozca nada sobre el control.
 */
class ControlBridgeTest {

    @Test
    void controlBasicoConDronAgricultura() {
        Agricultura agri = new Agricultura("AG-1", "SN-AG1", "AgriMax", "DJI", 15.0, 20.0);
        ControlVuelo controlVuelo = new ControlVuelo(agri, new ControlBasico());

        assertEquals("Control básico", controlVuelo.getTipoControl());
        assertEquals(agri, controlVuelo.getDrone());

        String accion = controlVuelo.ejecutarAccion();
        assertNotNull(accion);
        assertTrue(accion.contains("Control básico"));
        assertTrue(accion.contains("AgriMax"));
        assertTrue(accion.contains("AG-1"));
    }

    @Test
    void controlAutonomoConDronVigilancia() {
        Vigilancia vig = new Vigilancia("VG-1", "SN-VG1", "SkyGuard", "DJI", 8.0, true);
        ControlVuelo controlVuelo = new ControlVuelo(vig, new ControlAutonomo());

        assertEquals("Control autónomo", controlVuelo.getTipoControl());
        assertEquals(vig, controlVuelo.getDrone());

        String accion = controlVuelo.ejecutarAccion();
        assertNotNull(accion);
        assertTrue(accion.contains("Control autónomo"));
        assertTrue(accion.contains("SkyGuard"));
        assertTrue(accion.contains("VG-1"));
    }

    @Test
    void intercambioDinamicoDeDronEnControl() {
        Agricultura agri = new Agricultura("AG-10", "SN-10", "AgriPro", "DJI", 12.0, 16.0);
        Vigilancia vig = new Vigilancia("VG-20", "SN-20", "SecureFly", "Parrot", 7.0, true);

        ControlVuelo controlVuelo = new ControlVuelo(agri, new ControlBasico());
        assertTrue(controlVuelo.ejecutarAccion().contains("AgriPro"));

        // Cambiamos dinámicamente el dron asociado al control
        controlVuelo.setDrone(vig);
        assertEquals(vig, controlVuelo.getDrone());
        assertTrue(controlVuelo.ejecutarAccion().contains("SecureFly"));
    }

    @Test
    void intercambioDinamicoDeImplementacionDeControl() {
        Agricultura agri = new Agricultura("AG-30", "SN-30", "AgriZeta", "DJI", 10.0, 12.0);

        ControlVuelo controlVuelo = new ControlVuelo(agri, new ControlBasico());
        assertEquals("Control básico", controlVuelo.getTipoControl());

        // Cambiamos dinámicamente la implementación de control (Implementor) sin tocar el dron
        controlVuelo.setControl(new ControlAutonomo());
        assertEquals("Control autónomo", controlVuelo.getTipoControl());
        assertEquals(agri, controlVuelo.getDrone());
    }

    @Test
    void factoriaControlVueloCrearInstanciaCorrectamente() {
        Agricultura agri = new Agricultura("AG-3", "SN-3", "T40", "DJI", 38.0, 40.0);
        Vigilancia vig = new Vigilancia("VG-3", "SN-3", "M300", "DJI", 9.0, false);

        ControlVuelo c1 = ControlVuelo.crear("Control básico", agri);
        assertInstanceOf(ControlBasico.class, c1.getControl());
        assertEquals(agri, c1.getDrone());

        ControlVuelo c2 = ControlVuelo.crear("Control autónomo", vig);
        assertInstanceOf(ControlAutonomo.class, c2.getControl());
        assertEquals(vig, c2.getDrone());
    }

    @Test
    void descripcionBreveEsExactamenteLaSolicitadaPorNegocio() {
        Drone drone = new Drone("D-1", "SN-1", "ModelX", "Maker", 5.0);

        ControlVuelo basico = ControlVuelo.crear("Control básico", drone);
        assertEquals("Dron con control básico", basico.descripcionBreve());

        ControlVuelo autonomo = ControlVuelo.crear("Control autónomo", drone);
        assertEquals("Dron con control autónomo", autonomo.descripcionBreve());
    }

    @Test
    void droneNoTieneNingunaResponsabilidadDelBridge() {
        Drone drone = new Drone("D-2", "SN-2", "ModelY", "Maker", 6.0);
        Drone clon = drone.clone();

        // Drone no expone ningun atributo ni metodo relacionado con el tipo de control:
        // la unica forma de asociar un control es a traves de ControlVuelo (la Abstraccion del Bridge).
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
        // DroneBuilder ya no expone un metodo tipoControl(...): esa responsabilidad
        // no pertenece a Drone ni a su construccion.
    }
}
