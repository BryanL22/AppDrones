package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class VigilanciaFactoryTest {

    @Test
    void crearDevuelveUnaVigilanciaConSusDatos() {
        Drone drone = new VigilanciaFactory(true).crear("D2", "SER-002", "ModeloY", "FabricanteY", 3.1);

        Vigilancia vigilancia = assertInstanceOf(Vigilancia.class, drone);
        assertEquals("D2", vigilancia.getId());
        assertEquals("SER-002", vigilancia.getSerial());
        assertEquals("ModeloY", vigilancia.getModelo());
        assertEquals("FabricanteY", vigilancia.getFabricante());
        assertEquals(3.1, vigilancia.getPeso());
        assertTrue(vigilancia.isDeteccionTermica());
    }
}
