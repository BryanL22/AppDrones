package co.edu.poli.sw2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SensorTest {

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Sensor sensor = new Sensor(1, "Camara", "FabricanteX");

        assertEquals(1, sensor.getIdSensor());
        assertEquals("Camara", sensor.getTipo());
        assertEquals("FabricanteX", sensor.getFabricante());
    }

    @Test
    void lasPropiedadesSonModificablesMedianteSetters() {
        Sensor sensor = new Sensor();

        sensor.setIdSensor(2);
        sensor.setTipo("GPS");
        sensor.setFabricante("FabricanteY");

        assertEquals(2, sensor.getIdSensor());
        assertEquals("GPS", sensor.getTipo());
        assertEquals("FabricanteY", sensor.getFabricante());
    }

    @Test
    void toStringIncluyeElTipo() {
        Sensor sensor = new Sensor(1, "Camara", "FabricanteX");

        assertTrue(sensor.toString().contains("Camara"));
    }
}
