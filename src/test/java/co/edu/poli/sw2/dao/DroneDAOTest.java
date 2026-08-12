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
 * registros identificados con un serial unico y los elimina al finalizar
 * para no dejar datos residuales.
 */
class DroneDAOTest {

    private final DroneDAO droneDAO = new DroneDAO();
    private Integer idCreado;

    @AfterEach
    void eliminarDroneDePrueba() {
        if (idCreado != null) {
            droneDAO.eliminar(idCreado);
            idCreado = null;
        }
    }

    private Drone crearDronePrueba() {
        String serialUnico = "TEST-" + UUID.randomUUID();
        Drone drone = new Drone(0, serialUnico, "ModeloTest", "FabricanteTest", 4.2);

        assertTrue(droneDAO.crear(drone));

        idCreado = buscarIdPorSerial(serialUnico);
        assertNotNull(idCreado, "El drone de prueba deberia quedar visible en obtenerTodos()");

        return droneDAO.obtenerPorId(idCreado);
    }

    private Integer buscarIdPorSerial(String serial) {
        return droneDAO.obtenerTodos().stream()
                .filter(d -> serial.equals(d.getSerial()))
                .map(Drone::getIdDrone)
                .findFirst()
                .orElse(null);
    }

    @Test
    void crearInsertaUnDroneRecuperablePorObtenerTodos() {
        Drone creado = crearDronePrueba();

        List<Drone> todos = droneDAO.obtenerTodos();
        Optional<Drone> encontrado = todos.stream()
                .filter(d -> d.getIdDrone() == creado.getIdDrone())
                .findFirst();

        assertTrue(encontrado.isPresent());
        assertEquals(creado.getSerial(), encontrado.get().getSerial());
    }

    @Test
    void obtenerPorIdDevuelveElDroneCorrespondiente() {
        Drone creado = crearDronePrueba();

        Drone encontrado = droneDAO.obtenerPorId(creado.getIdDrone());

        assertNotNull(encontrado);
        assertEquals(creado.getSerial(), encontrado.getSerial());
        assertEquals("ModeloTest", encontrado.getModelo());
        assertEquals("FabricanteTest", encontrado.getFabricante());
        assertEquals(4.2, encontrado.getPeso());
    }

    @Test
    void obtenerPorIdDevuelveNuloSiElDroneNoExiste() {
        assertNull(droneDAO.obtenerPorId(-1));
    }

    @Test
    void actualizarModificaLosDatosDelDroneExistente() {
        Drone creado = crearDronePrueba();

        creado.setModelo("ModeloActualizado");
        creado.setFabricante("FabricanteActualizado");
        creado.setPeso(9.9);

        assertTrue(droneDAO.actualizar(creado));

        Drone actualizado = droneDAO.obtenerPorId(creado.getIdDrone());
        assertEquals("ModeloActualizado", actualizado.getModelo());
        assertEquals("FabricanteActualizado", actualizado.getFabricante());
        assertEquals(9.9, actualizado.getPeso());
    }

    @Test
    void actualizarDevuelveFalsoSiElDroneNoExiste() {
        Drone inexistente = new Drone(-1, "NO-EXISTE", "X", "Y", 1.0);

        assertFalse(droneDAO.actualizar(inexistente));
    }

    @Test
    void eliminarQuitaElDroneDeLaBaseDeDatos() {
        Drone creado = crearDronePrueba();

        assertTrue(droneDAO.eliminar(creado.getIdDrone()));
        assertNull(droneDAO.obtenerPorId(creado.getIdDrone()));

        idCreado = null;
    }

    @Test
    void eliminarDevuelveFalsoSiElDroneNoExiste() {
        assertFalse(droneDAO.eliminar(-1));
    }
}
