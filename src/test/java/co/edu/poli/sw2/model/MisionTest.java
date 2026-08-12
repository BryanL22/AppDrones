package co.edu.poli.sw2.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MisionTest {

    private static final LocalDate FECHA = LocalDate.of(2026, 8, 12);

    @Test
    void constructorSinPilotoLoDejaEnNulo() {
        Mision mision = new Mision(1, "Inspeccion", "Bogota", FECHA);

        assertEquals(1, mision.getIdMision());
        assertEquals("Inspeccion", mision.getNombre());
        assertEquals("Bogota", mision.getUbicacion());
        assertEquals(FECHA, mision.getFecha());
        assertNull(mision.getPiloto());
    }

    @Test
    void constructorConPilotoLoAsigna() {
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");

        Mision mision = new Mision(1, "Inspeccion", "Bogota", FECHA, piloto);

        assertEquals(piloto, mision.getPiloto());
    }

    @Test
    void elPilotoSePuedeAsignarConSetter() {
        Mision mision = new Mision(1, "Inspeccion", "Bogota", FECHA);
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");

        mision.setPiloto(piloto);

        assertEquals(piloto, mision.getPiloto());
    }

    @Test
    void toStringIncluyeElPiloto() {
        Piloto piloto = new Piloto(1, "Juan Perez", 5, "3000000000");
        Mision mision = new Mision(1, "Inspeccion", "Bogota", FECHA, piloto);

        assertTrue(mision.toString().contains("Juan Perez"));
    }
}
