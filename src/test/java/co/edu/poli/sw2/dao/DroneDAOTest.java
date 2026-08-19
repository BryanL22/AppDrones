package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Drone;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integracion para {@link DroneDAO}. Se ejecutan contra la base
 * de datos configurada en {@code .env}; cada prueba crea sus propios
 * registros y los elimina al finalizar para no dejar datos residuales.
 */
class DroneDAOTest {

    private final DroneDAO droneDAO = new DroneDAO();
    private String idCreado;

    @AfterEach
    void eliminarDroneDePrueba() {
        if (idCreado != null) {
            droneDAO.eliminar(idCreado);
            idCreado = null;
        }
    }

    private Drone crearDronePrueba() {
        String idUnico = "TEST-" + UUID.randomUUID();
        String serialUnico = "TEST-" + UUID.randomUUID();
        Drone drone = new Drone(idUnico, serialUnico, "ModeloTest", "FabricanteTest", 4.2);

        assertTrue(droneDAO.crear(drone));

        idCreado = idUnico;
        return droneDAO.obtenerPorId(idCreado);
    }

    @Test
    void crearUsaElIdAsignadoManualmenteYQuedaRecuperablePorObtenerTodos() {
        Drone creado = crearDronePrueba();

        List<Drone> todos = droneDAO.obtenerTodos();
        Optional<Drone> encontrado = todos.stream()
                .filter(d -> d.getId().equals(creado.getId()))
                .findFirst();

        assertTrue(encontrado.isPresent());
        assertEquals(creado.getSerial(), encontrado.get().getSerial());
    }

    @Test
    void crearFallaSiElIdYaExiste() {
        Drone creado = crearDronePrueba();

        Drone duplicado = new Drone(creado.getId(), "OTRO-SERIAL", "OtroModelo", "OtroFabricante", 1.0);

        assertFalse(droneDAO.crear(duplicado));
    }

    @Test
    void obtenerPorIdDevuelveElDroneCorrespondiente() {
        Drone creado = crearDronePrueba();

        Drone encontrado = droneDAO.obtenerPorId(creado.getId());

        assertNotNull(encontrado);
        assertEquals(creado.getSerial(), encontrado.getSerial());
        assertEquals("ModeloTest", encontrado.getModelo());
        assertEquals("FabricanteTest", encontrado.getFabricante());
        assertEquals(4.2, encontrado.getPeso());
    }

    @Test
    void obtenerPorIdDevuelveNuloSiElDroneNoExiste() {
        assertNull(droneDAO.obtenerPorId("id-inexistente"));
    }

    @Test
    void actualizarModificaLosDatosDelDroneExistente() {
        Drone creado = crearDronePrueba();

        creado.setModelo("ModeloActualizado");
        creado.setFabricante("FabricanteActualizado");
        creado.setPeso(9.9);

        assertTrue(droneDAO.actualizar(creado));

        Drone actualizado = droneDAO.obtenerPorId(creado.getId());
        assertEquals("ModeloActualizado", actualizado.getModelo());
        assertEquals("FabricanteActualizado", actualizado.getFabricante());
        assertEquals(9.9, actualizado.getPeso());
    }

    @Test
    void actualizarDevuelveFalsoSiElDroneNoExiste() {
        Drone inexistente = new Drone("id-inexistente", "NO-EXISTE", "X", "Y", 1.0);

        assertFalse(droneDAO.actualizar(inexistente));
    }

    @Test
    void eliminarQuitaElDroneDeLaBaseDeDatos() {
        Drone creado = crearDronePrueba();

        assertTrue(droneDAO.eliminar(creado.getId()));
        assertNull(droneDAO.obtenerPorId(creado.getId()));

        idCreado = null;
    }

    @Test
    void eliminarDevuelveFalsoSiElDroneNoExiste() {
        assertFalse(droneDAO.eliminar("id-inexistente"));
    }
}
