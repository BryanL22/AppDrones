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
 * Pruebas unitarias para verificar la implementación del patrón de diseño Bridge
 * enfocado en los tipos de controles del CRUD ("Control básico" y "Control autónomo").
 */
class ControlBridgeTest {

    @Test
    void controlBasicoConDronAgricultura() {
        Agricultura agri = new Agricultura("AG-1", "SN-AG1", "AgriMax", "DJI", 15.0, 20.0);
        ControlBasico control = new ControlBasico(agri);

        assertEquals("Control básico", control.getTipoControl());
        assertEquals(agri, control.getDrone());

        String accion = control.ejecutarAccion();
        assertNotNull(accion);
        assertTrue(accion.contains("Control Básico"));
        assertTrue(accion.contains("AgriMax"));
        assertTrue(accion.contains("AG-1"));
    }

    @Test
    void controlAutonomoConDronVigilancia() {
        Vigilancia vig = new Vigilancia("VG-1", "SN-VG1", "SkyGuard", "DJI", 8.0, true);
        ControlAutonomo control = new ControlAutonomo(vig);

        assertEquals("Control autónomo", control.getTipoControl());
        assertEquals(vig, control.getDrone());

        String accion = control.ejecutarAccion();
        assertNotNull(accion);
        assertTrue(accion.contains("Control Autónomo"));
        assertTrue(accion.contains("SkyGuard"));
        assertTrue(accion.contains("VG-1"));
    }

    @Test
    void intercambioDinamicoDeDronEnControl() {
        Agricultura agri = new Agricultura("AG-10", "SN-10", "AgriPro", "DJI", 12.0, 16.0);
        Vigilancia vig = new Vigilancia("VG-20", "SN-20", "SecureFly", "Parrot", 7.0, true);

        ControlDrone control = new ControlBasico(agri);
        assertTrue(control.ejecutarAccion().contains("AgriPro"));

        // Cambiamos dinámicamente el dron asociado al control
        control.setDrone(vig);
        assertEquals(vig, control.getDrone());
        assertTrue(control.ejecutarAccion().contains("SecureFly"));
    }

    @Test
    void factoriaControlDroneCrearInstanciaCorrectamente() {
        Agricultura agri = new Agricultura("AG-3", "SN-3", "T40", "DJI", 38.0, 40.0);
        Vigilancia vig = new Vigilancia("VG-3", "SN-3", "M300", "DJI", 9.0, false);

        ControlDrone c1 = ControlDrone.crear("Control básico", agri);
        assertInstanceOf(ControlBasico.class, c1);
        assertEquals(agri, c1.getDrone());

        ControlDrone c2 = ControlDrone.crear("Control autónomo", vig);
        assertInstanceOf(ControlAutonomo.class, c2);
        assertEquals(vig, c2.getDrone());
    }

    @Test
    void droneConservaTipoControlAlSerClonado() {
        Drone drone = new Drone("D-1", "SN-1", "ModelX", "Maker", 5.0);
        drone.setTipoControl("Control autónomo");

        Drone clon = drone.clone();
        assertEquals("Control autónomo", clon.getTipoControl());
    }

    @Test
    void droneBuilderEstableceTipoControlCorrectamente() {
        Drone drone = new DroneBuilder()
                .id("D-BUILD")
                .serial("SN-B")
                .tipoControl("Control autónomo")
                .construir();

        assertEquals("Control autónomo", drone.getTipoControl());
    }
}
