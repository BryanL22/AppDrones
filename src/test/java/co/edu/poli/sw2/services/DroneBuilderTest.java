package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Piloto;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DroneBuilderTest {

    @Test
    void construirSinAtributosEspecificosDevuelveUnDroneBase() {
        Drone drone = new DroneBuilder()
                .id("D1")
                .serial("SER-001")
                .modelo("ModeloX")
                .fabricante("FabricanteX")
                .peso(2.5)
                .construir();

        assertEquals(Drone.class, drone.getClass());
        assertEquals("D1", drone.getId());
        assertEquals("SER-001", drone.getSerial());
        assertEquals("ModeloX", drone.getModelo());
        assertEquals("FabricanteX", drone.getFabricante());
        assertEquals(2.5, drone.getPeso());
    }

    @Test
    void construirConCapacidadTanqueDevuelveUnaAgricultura() {
        Drone drone = new DroneBuilder()
                .id("D2")
                .serial("SER-002")
                .modelo("ModeloY")
                .fabricante("FabricanteY")
                .peso(6.0)
                .capacidadTanque(20.0)
                .construir();

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals(20.0, agricultura.getCapacidadTanque());
    }

    @Test
    void construirConDeteccionTermicaDevuelveUnaVigilancia() {
        Drone drone = new DroneBuilder()
                .id("D3")
                .serial("SER-003")
                .modelo("ModeloZ")
                .fabricante("FabricanteZ")
                .peso(3.0)
                .deteccionTermica(true)
                .construir();

        Vigilancia vigilancia = assertInstanceOf(Vigilancia.class, drone);
        assertTrue(vigilancia.isDeteccionTermica());
    }

    @Test
    void construirConAtributosDeAgriculturaYVigilanciaLanzaExcepcion() {
        DroneBuilder builder = new DroneBuilder()
                .id("D4")
                .serial("SER-004")
                .modelo("ModeloW")
                .fabricante("FabricanteW")
                .peso(4.0)
                .capacidadTanque(10.0)
                .deteccionTermica(false);

        assertThrows(IllegalStateException.class, builder::construir);
    }

    @Test
    void construirSinIdLanzaExcepcion() {
        DroneBuilder builder = new DroneBuilder()
                .serial("SER-005")
                .modelo("ModeloU")
                .fabricante("FabricanteU")
                .peso(5.0);

        assertThrows(IllegalStateException.class, builder::construir);
    }

    @Test
    void construirSoloConIdYPesoNoLanzaExcepcion() {
        Drone drone = new DroneBuilder()
                .id("D8")
                .peso(4.4)
                .construir();

        assertEquals(Drone.class, drone.getClass());
        assertEquals("D8", drone.getId());
        assertEquals(4.4, drone.getPeso());
        assertEquals(null, drone.getSerial());
    }

    @Test
    void construirSoloConIdYCapacidadTanqueDevuelveUnaAgriculturaSinLosDemasComunes() {
        Drone drone = new DroneBuilder()
                .id("D9")
                .capacidadTanque(15.0)
                .construir();

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals("D9", agricultura.getId());
        assertEquals(15.0, agricultura.getCapacidadTanque());
        assertEquals(null, agricultura.getSerial());
    }

    @Test
    void construirSoloConIdDevuelveUnDroneBase() {
        Drone drone = new DroneBuilder()
                .id("D10")
                .construir();

        assertEquals(Drone.class, drone.getClass());
        assertEquals("D10", drone.getId());
    }

    @Test
    void construirAplicaPilotoOpcionalSiSeEstablece() {
        Piloto piloto = new Piloto("P1", "Nombre", "Licencia", "123");

        Drone drone = new DroneBuilder()
                .id("D6")
                .serial("SER-006")
                .modelo("ModeloV")
                .fabricante("FabricanteV")
                .peso(1.0)
                .piloto(piloto)
                .construir();

        assertEquals(piloto, drone.getPiloto());
    }

    @Test
    void metodosFluidosDevuelvenLaMismaInstanciaDelBuilder() {
        DroneBuilder builder = new DroneBuilder();

        assertEquals(builder, builder.id("D7"));
        assertEquals(builder, builder.serial("SER-007"));
        assertEquals(builder, builder.peso(1.0));
    }
}
