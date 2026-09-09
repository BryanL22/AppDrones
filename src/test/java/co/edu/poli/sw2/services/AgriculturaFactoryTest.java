package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class AgriculturaFactoryTest {

    @Test
    void crearDevuelveUnaAgriculturaConSusDatos() {
        Drone drone = new AgriculturaFactory(30.0).crear("D1", "SER-001", "ModeloX", "FabricanteX", 2.5);

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals("D1", agricultura.getId());
        assertEquals("SER-001", agricultura.getSerial());
        assertEquals("ModeloX", agricultura.getModelo());
        assertEquals("FabricanteX", agricultura.getFabricante());
        assertEquals(2.5, agricultura.getPeso());
        assertEquals(30.0, agricultura.getCapacidadTanque());
    }
}
