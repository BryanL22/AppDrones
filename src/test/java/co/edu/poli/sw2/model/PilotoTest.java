package co.edu.poli.sw2.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PilotoTest {

    @Test
    void constructorConDatosAsignaTodosLosCampos() {
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");

        assertEquals(1, piloto.getIdPiloto());
        assertEquals("Juan Perez", piloto.getNombre());
        assertEquals(5, piloto.getExperiencia());
        assertEquals("3000000000", piloto.getTelefono());
    }

    @Test
    void lasPropiedadesSonModificablesMedianteSetters() {
        Piloto piloto = new Piloto();

        piloto.setIdPiloto(2);
        piloto.setNombre("Maria Lopez");
        piloto.setExperiencia(3);
        piloto.setTelefono("3000000001");

        assertEquals(2, piloto.getIdPiloto());
        assertEquals("Maria Lopez", piloto.getNombre());
        assertEquals(3, piloto.getExperiencia());
        assertEquals("3000000001", piloto.getTelefono());
    }

    @Test
    void toStringIncluyeElNombre() {
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");

        assertTrue(piloto.toString().contains("Juan Perez"));
    }
}
