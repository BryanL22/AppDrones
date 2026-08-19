package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integracion para {@link VigilanciaDAO}. Verifica que los
 * campos base se guarden en {@code drone} y los propios en {@code vigilancia},
 * relacionados por {@code id_drone}, y que borrar el dron elimine tambien la
 * fila de la subclase (ON DELETE CASCADE).
 */
class VigilanciaDAOTest {

    private final VigilanciaDAO vigilanciaDAO = new VigilanciaDAO();
    private final DroneDAO droneDAO = new DroneDAO();
    private String idCreado;

    @AfterEach
    void eliminarDronDePrueba() {
        if (idCreado != null) {
            droneDAO.eliminar(idCreado);
            idCreado = null;
        }
    }

    private Vigilancia crearVigilanciaPrueba() {
        String idUnico = "TEST-VIG-" + UUID.randomUUID();
        String serialUnico = "TEST-VIG-" + UUID.randomUUID();
        Vigilancia vigilancia = new Vigilancia(idUnico, serialUnico, "ModeloVig", "FabricanteVig", 3.0, true);

        assertTrue(vigilanciaDAO.crear(vigilancia));

        idCreado = idUnico;
        return vigilancia;
    }

    @Test
    void crearInsertaEnDroneYEnVigilancia() {
        Vigilancia creada = crearVigilanciaPrueba();

        Vigilancia encontrada = vigilanciaDAO.obtenerPorId(creada.getId());

        assertNotNull(encontrada);
        assertEquals(creada.getSerial(), encontrada.getSerial());
        assertTrue(encontrada.isDeteccionTermica());
    }

    @Test
    void obtenerTodosIncluyeElRegistroCreado() {
        Vigilancia creada = crearVigilanciaPrueba();

        boolean presente = vigilanciaDAO.obtenerTodos().stream()
                .anyMatch(v -> v.getId().equals(creada.getId()));

        assertTrue(presente);
    }

    @Test
    void actualizarModificaCamposBaseYPropios() {
        Vigilancia creada = crearVigilanciaPrueba();

        creada.setModelo("ModeloVigActualizado");
        creada.setDeteccionTermica(false);

        assertTrue(vigilanciaDAO.actualizar(creada));

        Vigilancia actualizada = vigilanciaDAO.obtenerPorId(creada.getId());
        assertEquals("ModeloVigActualizado", actualizada.getModelo());
        assertFalse(actualizada.isDeteccionTermica());
    }

    @Test
    void eliminarBorraElDronBaseYLaFilaDeVigilanciaEnCascada() {
        Vigilancia creada = crearVigilanciaPrueba();

        assertTrue(vigilanciaDAO.eliminar(creada.getId()));

        assertNull(vigilanciaDAO.obtenerPorId(creada.getId()));
        assertNull(droneDAO.obtenerPorId(creada.getId()));

        idCreado = null;
    }

    @Test
    void obtenerPorIdDevuelveNuloSiNoExiste() {
        assertNull(vigilanciaDAO.obtenerPorId("id-inexistente"));
    }

    @Test
    void actualizarDevuelveFalsoSiNoExiste() {
        Vigilancia inexistente = new Vigilancia("id-inexistente", "X", "Y", "Z", 1.0, true);

        assertFalse(vigilanciaDAO.actualizar(inexistente));
    }
}
