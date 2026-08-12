package co.edu.poli.sw2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DroneTest {

    @Test
    void constructorVacioInicializaListasYPilotoEnNulo() {
        Drone drone = new Drone();

        assertNull(drone.getPiloto());
        assertNotNull(drone.getSensores());
        assertTrue(drone.getSensores().isEmpty());
        assertNotNull(drone.getMisiones());
        assertTrue(drone.getMisiones().isEmpty());
    }

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Drone drone = new Drone(1, "SER-001", "ModeloX", "FabricanteX", 2.5);

        assertEquals(1, drone.getIdDrone());
        assertEquals("SER-001", drone.getSerial());
        assertEquals("ModeloX", drone.getModelo());
        assertEquals("FabricanteX", drone.getFabricante());
        assertEquals(2.5, drone.getPeso());
    }

    @Test
    void unDroneTieneUnSoloPilotoAsociado() {
        Drone drone = new Drone();
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");

        drone.setPiloto(piloto);

        assertEquals(piloto, drone.getPiloto());
    }

    @Test
    void reemplazarElPilotoDescartaElAnterior() {
        Drone drone = new Drone();
        Piloto primero = new Piloto(1, "Juan Perez", 5, "3000000000");
        Piloto segundo = new Piloto(2, "Maria Lopez", 3, "3000000001");

        drone.setPiloto(primero);
        drone.setPiloto(segundo);

        assertEquals(segundo, drone.getPiloto());
    }

    @Test
    void toStringIncluyeElPiloto() {
        Drone drone = new Drone(1, "SER-001", "ModeloX", "FabricanteX", 2.5);
        drone.setPiloto(new Piloto(1, "Juan Perez", 5, "3000000000"));

        assertTrue(drone.toString().contains("Juan Perez"));
    }
}
