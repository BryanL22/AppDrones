package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DroneFactoryTest {

    @Test
    void tiposDisponiblesIncluyeAgriculturaYVigilancia() {
        assertEquals(
                java.util.List.of(DroneFactory.TIPO_AGRICULTURA, DroneFactory.TIPO_VIGILANCIA),
                DroneFactory.tiposDisponibles()
        );
    }

    @Test
    void crearConTipoAgriculturaDevuelveUnaAgriculturaConSusDatos() {
        Drone drone = DroneFactory.crear(DroneFactory.TIPO_AGRICULTURA,
                "D1", "SER-001", "ModeloX", "FabricanteX", 2.5, 30.0, false);

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals("D1", agricultura.getId());
        assertEquals("SER-001", agricultura.getSerial());
        assertEquals(30.0, agricultura.getCapacidadTanque());
    }

    @Test
    void crearConTipoVigilanciaDevuelveUnaVigilanciaConSusDatos() {
        Drone drone = DroneFactory.crear(DroneFactory.TIPO_VIGILANCIA,
                "D2", "SER-002", "ModeloY", "FabricanteY", 3.1, 0, true);

        Vigilancia vigilancia = assertInstanceOf(Vigilancia.class, drone);
        assertEquals("D2", vigilancia.getId());
        assertEquals("SER-002", vigilancia.getSerial());
        assertTrue(vigilancia.isDeteccionTermica());
    }

    @Test
    void crearConTipoDesconocidoLanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                DroneFactory.crear("Reparto", "D3", "SER-003", "ModeloZ", "FabricanteZ", 1.0, 0, false));
    }
}
