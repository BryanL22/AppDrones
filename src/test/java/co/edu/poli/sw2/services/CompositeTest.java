package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Sensor;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para verificar la implementación del patrón de diseño
 * Composite: {@link Component} (interfaz común), {@link WrapperSensor}
 * (Leaf, que envuelve un {@link Sensor} del modelo) y {@link Composite}
 * (grupo que delega en sus hijos y puede anidarse a cualquier profundidad).
 */
class CompositeTest {

    private WrapperSensor hoja(String id, String tipo, String fabricante) {
        return new WrapperSensor(new Sensor(id, tipo, fabricante));
    }

    private Composite grupo(Component... hijos) {
        Composite grupo = new Composite();
        for (Component hijo : hijos) {
            grupo.add(hijo);
        }
        return grupo;
    }

    // ------------------------------------------------------------------
    // Leaf: WrapperSensor
    // ------------------------------------------------------------------

    @Test
    void wrapperSensorLeeIdTipoYFabricanteDelSensor() {
        WrapperSensor infrarrojo = hoja("T-01", "Sensor Infrarrojo", "Melexis");

        assertEquals("T-01 - Sensor Infrarrojo (Melexis)", infrarrojo.leer());
    }

    @Test
    void wrapperSensorReflejaLosCambiosPosterioresDelSensorEnvuelto() {
        Sensor sensor = new Sensor("C-01", "Sensor CMOS", "Sony");
        WrapperSensor hoja = new WrapperSensor(sensor);

        sensor.setTipo("Sensor CCD");
        sensor.setFabricante("Teledyne");

        assertEquals("C-01 - Sensor CCD (Teledyne)", hoja.leer());
    }

    @Test
    void wrapperSensorNoModificaElSensorQueEnvuelve() {
        Sensor sensor = new Sensor("S-01", "Sensor Analogico", "Bosch");
        WrapperSensor hoja = new WrapperSensor(sensor);

        hoja.leer();

        assertEquals("S-01", sensor.getId());
        assertEquals("Sensor Analogico", sensor.getTipo());
        assertEquals("Bosch", sensor.getFabricante());
    }

    @Test
    void wrapperSensorConSensorSinDatosNoLanzaExcepcion() {
        WrapperSensor vacio = new WrapperSensor(new Sensor());

        assertEquals("null - null (null)", vacio.leer());
    }

    // ------------------------------------------------------------------
    // Composite: grupo de sensores
    // ------------------------------------------------------------------

    @Test
    void compositeSinHijosDevuelveUnaLecturaVacia() {
        assertEquals("", new Composite().leer());
    }

    @Test
    void compositeConUnSoloHijoDevuelveLaLecturaDeEseHijo() {
        WrapperSensor rtd = hoja("T-02", "RTD", "Honeywell");

        Composite temperatura = grupo(rtd);

        assertEquals(rtd.leer(), temperatura.leer());
    }

    @Test
    void compositeUneLasLecturasDeSusHijosUnaPorLineaYEnOrden() {
        Composite temperatura = grupo(
                hoja("T-01", "Sensor Infrarrojo", "Melexis"),
                hoja("T-02", "RTD", "Honeywell"));

        assertEquals("T-01 - Sensor Infrarrojo (Melexis)\nT-02 - RTD (Honeywell)",
                temperatura.leer());
    }

    @Test
    void compositeNoTerminaConSaltoDeLinea() {
        Composite camara = grupo(
                hoja("C-01", "Sensor CMOS", "Sony"),
                hoja("C-02", "Sensor CCD", "Teledyne"));

        assertFalse(camara.leer().endsWith("\n"));
    }

    @Test
    void leerEsRepetibleYNoAlteraElGrupo() {
        Composite camara = grupo(
                hoja("C-01", "Sensor CMOS", "Sony"),
                hoja("C-02", "Sensor CCD", "Teledyne"));

        String primera = camara.leer();
        String segunda = camara.leer();

        assertEquals(primera, segunda);
        assertEquals(2, segunda.split("\n").length);
    }

    @Test
    void addAgregaUnHijoAlFinalDelGrupo() {
        Composite grupo = grupo(hoja("D-01", "SPI", "Texas Instruments"));

        grupo.add(hoja("D-02", "UART", "FTDI"));

        assertEquals("D-01 - SPI (Texas Instruments)\nD-02 - UART (FTDI)", grupo.leer());
    }

    @Test
    void removeQuitaUnHijoDelGrupo() {
        WrapperSensor spi = hoja("D-01", "SPI", "Texas Instruments");
        WrapperSensor uart = hoja("D-02", "UART", "FTDI");
        Composite digital = grupo(spi, uart);

        digital.remove(spi);

        assertEquals(uart.leer(), digital.leer());
    }

    @Test
    void removeDeUnElementoQueNoEstaEnElGrupoNoLoAltera() {
        WrapperSensor spi = hoja("D-01", "SPI", "Texas Instruments");
        Composite digital = grupo(spi);

        digital.remove(hoja("X-99", "Otro", "Otro"));

        assertEquals(spi.leer(), digital.leer());
    }

    @Test
    void removerTodosLosHijosDejaElGrupoVacio() {
        WrapperSensor spi = hoja("D-01", "SPI", "Texas Instruments");
        Composite digital = grupo(spi);

        digital.remove(spi);

        assertEquals("", digital.leer());
    }

    @Test
    void elMismoComponenteAgregadoDosVecesSeLeeDosVeces() {
        WrapperSensor cmos = hoja("C-01", "Sensor CMOS", "Sony");

        Composite camara = grupo(cmos, cmos);

        assertEquals(cmos.leer() + "\n" + cmos.leer(), camara.leer());
    }

    // ------------------------------------------------------------------
    // Composición: el árbol se puede anidar
    // ------------------------------------------------------------------

    @Test
    void compositeAnidadoRecorreElArbolCompletoEnProfundidad() {
        WrapperSensor analogico = hoja("S-01", "Sensor Analogico", "Bosch");
        WrapperSensor spi = hoja("D-01", "SPI", "Texas Instruments");
        WrapperSensor uart = hoja("D-02", "UART", "FTDI");

        Composite digital = grupo(spi, uart);
        Composite sonido = grupo(analogico, digital);

        assertEquals(String.join("\n",
                "S-01 - Sensor Analogico (Bosch)",
                "D-01 - SPI (Texas Instruments)",
                "D-02 - UART (FTDI)"), sonido.leer());
    }

    @Test
    void arbolDeSensoresDelDroneDevuelveLosOchoSensoresEnOrden() {
        WrapperSensor infrarrojo = hoja("T-01", "Sensor Infrarrojo", "Melexis");
        WrapperSensor rtd = hoja("T-02", "RTD", "Honeywell");
        WrapperSensor cmos = hoja("C-01", "Sensor CMOS", "Sony");
        WrapperSensor ccd = hoja("C-02", "Sensor CCD", "Teledyne");
        WrapperSensor analogico = hoja("S-01", "Sensor Analogico", "Bosch");
        WrapperSensor spi = hoja("D-01", "SPI", "Texas Instruments");
        WrapperSensor uart = hoja("D-02", "UART", "FTDI");
        WrapperSensor inteligente = hoja("I-01", "Sensor Inteligente", "Nvidia");

        Composite temperatura = grupo(infrarrojo, rtd);
        Composite camara = grupo(cmos, ccd);
        Composite digital = grupo(spi, uart);
        Composite sonido = grupo(analogico, digital);
        Composite general = grupo(temperatura, camara, sonido, inteligente);

        String[] lecturas = general.leer().split("\n");

        assertEquals(8, lecturas.length);
        assertEquals(infrarrojo.leer(), lecturas[0]);
        assertEquals(rtd.leer(), lecturas[1]);
        assertEquals(cmos.leer(), lecturas[2]);
        assertEquals(ccd.leer(), lecturas[3]);
        assertEquals(analogico.leer(), lecturas[4]);
        assertEquals(spi.leer(), lecturas[5]);
        assertEquals(uart.leer(), lecturas[6]);
        assertEquals(inteligente.leer(), lecturas[7]);
    }

    @Test
    void laLecturaDeLaRaizEsLaUnionDeLasLecturasDeSusSubgrupos() {
        Composite temperatura = grupo(
                hoja("T-01", "Sensor Infrarrojo", "Melexis"),
                hoja("T-02", "RTD", "Honeywell"));
        Composite camara = grupo(
                hoja("C-01", "Sensor CMOS", "Sony"),
                hoja("C-02", "Sensor CCD", "Teledyne"));

        Composite general = grupo(temperatura, camara);

        assertEquals(temperatura.leer() + "\n" + camara.leer(), general.leer());
    }

    @Test
    void elArbolReflejaLosCambiosHechosEnUnSubgrupoDespuesDeArmarlo() {
        Composite camara = grupo(hoja("C-01", "Sensor CMOS", "Sony"));
        Composite general = grupo(camara);

        camara.add(hoja("C-02", "Sensor CCD", "Teledyne"));

        assertEquals(2, general.leer().split("\n").length);
        assertTrue(general.leer().contains("C-02 - Sensor CCD (Teledyne)"));
    }

    @Test
    void removerUnSubgrupoQuitaTodosSusSensoresDeLaRaiz() {
        WrapperSensor inteligente = hoja("I-01", "Sensor Inteligente", "Nvidia");
        Composite digital = grupo(
                hoja("D-01", "SPI", "Texas Instruments"),
                hoja("D-02", "UART", "FTDI"));
        Composite general = grupo(digital, inteligente);

        general.remove(digital);

        assertEquals(inteligente.leer(), general.leer());
    }

    @Test
    void unSubgrupoVacioAnidadoAportaUnaLineaEnBlanco() {
        // Comportamiento actual: un grupo sin hijos devuelve "" y Composite
        // lo une igual que a cualquier otra lectura, dejando una linea vacia.
        WrapperSensor primero = hoja("T-01", "Sensor Infrarrojo", "Melexis");
        WrapperSensor ultimo = hoja("I-01", "Sensor Inteligente", "Nvidia");

        Composite general = grupo(primero, new Composite(), ultimo);

        assertEquals(primero.leer() + "\n\n" + ultimo.leer(), general.leer());
    }

    // ------------------------------------------------------------------
    // Interfaz común: el cliente trata igual a hojas y grupos
    // ------------------------------------------------------------------

    @Test
    void hojasYGruposSeUsanIgualATravesDeComponent() {
        List<Component> elementos = List.of(
                hoja("T-01", "Sensor Infrarrojo", "Melexis"),
                grupo(hoja("C-01", "Sensor CMOS", "Sony"), hoja("C-02", "Sensor CCD", "Teledyne")),
                new Composite());

        for (Component elemento : elementos) {
            assertNotNull(elemento.leer());
        }
    }

    @Test
    void compositeNoDependeDeLasClasesConcretasDeSusHijos() {
        // Component tiene un unico metodo: cualquier implementacion sirve como hijo.
        Component fijo = () -> "lectura fija";

        Composite general = grupo(fijo, hoja("I-01", "Sensor Inteligente", "Nvidia"));

        assertEquals("lectura fija\nI-01 - Sensor Inteligente (Nvidia)", general.leer());
    }

    @Test
    void wrapperSensorYCompositeImplementanComponent() {
        assertTrue(Component.class.isAssignableFrom(WrapperSensor.class));
        assertTrue(Component.class.isAssignableFrom(Composite.class));
    }
}
