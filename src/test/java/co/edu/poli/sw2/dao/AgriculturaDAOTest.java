package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Agricultura;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integracion para {@link AgriculturaDAO}. Verifica que los
 * campos base se guarden en {@code drone} y los propios en {@code agricultura},
 * relacionados por {@code id_drone}, y que borrar el dron elimine tambien la
 * fila de la subclase (ON DELETE CASCADE).
 */
class AgriculturaDAOTest {

    private final AgriculturaDAO agriculturaDAO = new AgriculturaDAO();
    private final DroneDAO droneDAO = new DroneDAO();
    private String idCreado;

    @AfterEach
    void eliminarDronDePrueba() {
        if (idCreado != null) {
            droneDAO.eliminar(idCreado);
            idCreado = null;
        }
    }

    private Agricultura crearAgriculturaPrueba() {
        String idUnico = "TEST-AGRO-" + UUID.randomUUID();
        String serialUnico = "TEST-AGRO-" + UUID.randomUUID();
        Agricultura agricultura = new Agricultura(idUnico, serialUnico, "ModeloAgro", "FabricanteAgro", 6.0, 20.0);

        assertTrue(agriculturaDAO.crear(agricultura));

        idCreado = idUnico;
        return agricultura;
    }

    @Test
    void crearInsertaEnDroneYEnAgricultura() {
        Agricultura creada = crearAgriculturaPrueba();

        Agricultura encontrada = agriculturaDAO.obtenerPorId(creada.getId());

        assertNotNull(encontrada);
        assertEquals(creada.getSerial(), encontrada.getSerial());
        assertEquals(20.0, encontrada.getCapacidadTanque());
    }

    @Test
    void obtenerTodosIncluyeElRegistroCreado() {
        Agricultura creada = crearAgriculturaPrueba();

        boolean presente = agriculturaDAO.obtenerTodos().stream()
                .anyMatch(a -> a.getId().equals(creada.getId()));

        assertTrue(presente);
    }

    @Test
    void actualizarModificaCamposBaseYPropios() {
        Agricultura creada = crearAgriculturaPrueba();

        creada.setModelo("ModeloAgroActualizado");
        creada.setCapacidadTanque(35.5);

        assertTrue(agriculturaDAO.actualizar(creada));

        Agricultura actualizada = agriculturaDAO.obtenerPorId(creada.getId());
        assertEquals("ModeloAgroActualizado", actualizada.getModelo());
        assertEquals(35.5, actualizada.getCapacidadTanque());
    }

    @Test
    void eliminarBorraElDronBaseYLaFilaDeAgriculturaEnCascada() {
        Agricultura creada = crearAgriculturaPrueba();

        assertTrue(agriculturaDAO.eliminar(creada.getId()));

        assertNull(agriculturaDAO.obtenerPorId(creada.getId()));
        assertNull(droneDAO.obtenerPorId(creada.getId()));

        idCreado = null;
    }

    @Test
    void obtenerPorIdDevuelveNuloSiNoExiste() {
        assertNull(agriculturaDAO.obtenerPorId("id-inexistente"));
    }

    @Test
    void actualizarDevuelveFalsoSiNoExiste() {
        Agricultura inexistente = new Agricultura("id-inexistente", "X", "Y", "Z", 1.0, 1.0);

        assertFalse(agriculturaDAO.actualizar(inexistente));
    }
}
