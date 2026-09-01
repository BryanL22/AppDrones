package co.edu.poli.sw2.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DroneFactoryTest {

    @Test
    void tiposDisponiblesIncluyeAgriculturaYVigilancia() {
        assertEquals(
                java.util.List.of(DroneFactory.TIPO_AGRICULTURA, DroneFactory.TIPO_VIGILANCIA),
                DroneFactory.tiposDisponibles());
    }
}
