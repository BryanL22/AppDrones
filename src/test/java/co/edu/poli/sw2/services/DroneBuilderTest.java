package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Piloto;
import co.edu.poli.sw2.model.Sensor;
import co.edu.poli.sw2.model.Vigilancia;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para {@link DroneBuilder}.
 */
class DroneBuilderTest {

    // ── Construccion exitosa ────────────────────────────────────────

    @Nested
    @DisplayName("Construccion exitosa")
    class ConstruccionExitosa {

        @Test
        @DisplayName("Sin atributos de subtipo → crea Drone base")
        void construirDroneBase() {
            Drone drone = new DroneBuilder()
                    .id("D001")
                    .serial("SN-100")
                    .modelo("Phantom")
                    .fabricante("DJI")
                    .peso(1.5)
                    .construir();

            assertNotNull(drone);
            assertInstanceOf(Drone.class, drone);
            assertFalse(drone instanceof Agricultura);
            assertFalse(drone instanceof Vigilancia);

            assertEquals("D001", drone.getId());
            assertEquals("SN-100", drone.getSerial());
            assertEquals("Phantom", drone.getModelo());
            assertEquals("DJI", drone.getFabricante());
            assertEquals(1.5, drone.getPeso());
        }

        @Test
        @DisplayName("Con capacidadTanque → crea Agricultura")
        void construirAgricultura() {
            Drone drone = new DroneBuilder()
                    .id("A001")
                    .serial("SN-200")
                    .modelo("Agras T30")
                    .fabricante("DJI")
                    .peso(25.0)
                    .capacidadTanque(30.0)
                    .construir();

            assertNotNull(drone);
            assertInstanceOf(Agricultura.class, drone);

            Agricultura agricultura = (Agricultura) drone;
            assertEquals("A001", agricultura.getId());
            assertEquals("SN-200", agricultura.getSerial());
            assertEquals("Agras T30", agricultura.getModelo());
            assertEquals("DJI", agricultura.getFabricante());
            assertEquals(25.0, agricultura.getPeso());
            assertEquals(30.0, agricultura.getCapacidadTanque());
        }

        @Test
        @DisplayName("Con deteccionTermica true → crea Vigilancia")
        void construirVigilanciaConTermica() {
            Drone drone = new DroneBuilder()
                    .id("V001")
                    .serial("SN-300")
                    .modelo("Matrice 300")
                    .fabricante("DJI")
                    .peso(6.3)
                    .deteccionTermica(true)
                    .construir();

            assertNotNull(drone);
            assertInstanceOf(Vigilancia.class, drone);

            Vigilancia vigilancia = (Vigilancia) drone;
            assertEquals("V001", vigilancia.getId());
            assertTrue(vigilancia.isDeteccionTermica());
        }

        @Test
        @DisplayName("Con deteccionTermica false → aun asi crea Vigilancia (el atributo fue configurado)")
        void construirVigilanciaSinTermica() {
            Drone drone = new DroneBuilder()
                    .id("V002")
                    .serial("SN-400")
                    .modelo("Mavic 2 Enterprise")
                    .fabricante("DJI")
                    .peso(1.1)
                    .deteccionTermica(false)
                    .construir();

            assertInstanceOf(Vigilancia.class, drone);
            assertFalse(((Vigilancia) drone).isDeteccionTermica());
        }

        @Test
        @DisplayName("Con piloto asignado → el setter se invoca correctamente")
        void construirConPiloto() {
            Piloto piloto = new Piloto("P01", "Carlos", "LIC-001", "3001234567");

            Drone drone = new DroneBuilder()
                    .id("D002")
                    .serial("SN-500")
                    .modelo("Phantom 4")
                    .fabricante("DJI")
                    .peso(1.4)
                    .piloto(piloto)
                    .construir();

            assertNotNull(drone.getPiloto());
            assertEquals("Carlos", drone.getPiloto().getNombre());
        }

        @Test
        @DisplayName("Con sensores asignados → el setter se invoca correctamente")
        void construirConSensores() {
            List<Sensor> sensores = List.of(
                    new Sensor("S01", "Camara", "Sony"),
                    new Sensor("S02", "GPS", "Garmin")
            );

            Drone drone = new DroneBuilder()
                    .id("D003")
                    .serial("SN-600")
                    .modelo("Inspire 2")
                    .fabricante("DJI")
                    .peso(3.4)
                    .sensores(sensores)
                    .construir();

            assertEquals(2, drone.getSensores().size());
        }
    }

    // ── Validacion de atributos obligatorios ────────────────────────

    @Nested
    @DisplayName("Validacion de atributos obligatorios")
    class ValidacionObligatorios {

        @Test
        @DisplayName("Sin id → lanza excepcion indicando que falta 'id'")
        void faltaId() {
            DroneBuilder builder = new DroneBuilder()
                    .serial("SN-100")
                    .modelo("Phantom")
                    .fabricante("DJI")
                    .peso(1.5);

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("id"));
        }

        @Test
        @DisplayName("Sin serial → lanza excepcion indicando que falta 'serial'")
        void faltaSerial() {
            DroneBuilder builder = new DroneBuilder()
                    .id("D001")
                    .modelo("Phantom")
                    .fabricante("DJI")
                    .peso(1.5);

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("serial"));
        }

        @Test
        @DisplayName("Sin modelo → lanza excepcion indicando que falta 'modelo'")
        void faltaModelo() {
            DroneBuilder builder = new DroneBuilder()
                    .id("D001")
                    .serial("SN-100")
                    .fabricante("DJI")
                    .peso(1.5);

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("modelo"));
        }

        @Test
        @DisplayName("Sin fabricante → lanza excepcion indicando que falta 'fabricante'")
        void faltaFabricante() {
            DroneBuilder builder = new DroneBuilder()
                    .id("D001")
                    .serial("SN-100")
                    .modelo("Phantom")
                    .peso(1.5);

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("fabricante"));
        }

        @Test
        @DisplayName("Sin peso → lanza excepcion indicando que falta 'peso'")
        void faltaPeso() {
            DroneBuilder builder = new DroneBuilder()
                    .id("D001")
                    .serial("SN-100")
                    .modelo("Phantom")
                    .fabricante("DJI");

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("peso"));
        }

        @Test
        @DisplayName("Sin ningun atributo → lista todos los faltantes en el mensaje")
        void faltanTodos() {
            DroneBuilder builder = new DroneBuilder();

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            String mensaje = ex.getMessage();
            assertTrue(mensaje.contains("id"));
            assertTrue(mensaje.contains("serial"));
            assertTrue(mensaje.contains("modelo"));
            assertTrue(mensaje.contains("fabricante"));
            assertTrue(mensaje.contains("peso"));
        }
    }

    // ── Conflicto de subtipos ──────────────────────────────────────

    @Nested
    @DisplayName("Conflicto de subtipos")
    class ConflictoSubtipos {

        @Test
        @DisplayName("capacidadTanque + deteccionTermica → lanza excepcion de conflicto")
        void conflictoAgriculturaYVigilancia() {
            DroneBuilder builder = new DroneBuilder()
                    .id("X001")
                    .serial("SN-999")
                    .modelo("Hibrido")
                    .fabricante("Test")
                    .peso(5.0)
                    .capacidadTanque(20.0)
                    .deteccionTermica(true);

            IllegalStateException ex = assertThrows(IllegalStateException.class, builder::construir);
            assertTrue(ex.getMessage().contains("Conflicto"));
        }
    }

    // ── Encadenamiento fluido ──────────────────────────────────────

    @Nested
    @DisplayName("Encadenamiento fluido")
    class EncadenamientoFluido {

        @Test
        @DisplayName("Todos los metodos devuelven la misma instancia del builder")
        void encadenamientoDevuelveMismaInstancia() {
            DroneBuilder builder = new DroneBuilder();

            assertSame(builder, builder.id("D001"));
            assertSame(builder, builder.serial("SN-100"));
            assertSame(builder, builder.modelo("Phantom"));
            assertSame(builder, builder.fabricante("DJI"));
            assertSame(builder, builder.peso(1.5));
            assertSame(builder, builder.piloto(null));
            assertSame(builder, builder.sensores(null));
            assertSame(builder, builder.capacidadTanque(10.0));
        }
    }
}
