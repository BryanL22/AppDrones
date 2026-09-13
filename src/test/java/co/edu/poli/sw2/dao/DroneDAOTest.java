package co.edu.poli.sw2.dao;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integracion para {@link DroneDAO}. Se ejecutan contra la base
 * de datos configurada en {@code .env}; cada prueba crea sus propios
 * registros y los elimina al finalizar para no dejar datos residuales.
 *
 * <p>Cubre tanto los drones sin especializacion como los creados como
 * {@link Agricultura} o {@link Vigilancia}, ya que un unico {@link DroneDAO}
 * maneja los tres casos.</p>
 */
class DroneDAOTest {

    private final DroneDAO droneDAO = new DroneDAO();
    private String idCreado;

    @AfterEach
    void eliminarDroneDePrueba() throws Exception {
        if (idCreado != null) {
            droneDAO.eliminar(idCreado);
            idCreado = null;
        }
    }

    private Drone crearDronePrueba() throws Exception {
        String idUnico = "TEST-" + UUID.randomUUID();
        String serialUnico = "TEST-" + UUID.randomUUID();
        Drone drone = new Drone(idUnico, serialUnico, "ModeloTest", "FabricanteTest", 4.2);

        assertTrue(droneDAO.crear(drone));

        idCreado = idUnico;
        return droneDAO.obtenerPorId(idCreado);
    }

    @Test
    void crearUsaElIdAsignadoManualmenteYQuedaRecuperablePorObtenerTodos() throws Exception {
        Drone creado = crearDronePrueba();

        List<Drone> todos = droneDAO.obtenerTodos();
        Optional<Drone> encontrado = todos.stream()
                .filter(d -> d.getId().equals(creado.getId()))
                .findFirst();

        assertTrue(encontrado.isPresent());
        assertEquals(creado.getSerial(), encontrado.get().getSerial());
    }

    @Test
    void crearFallaSiElIdYaExiste() throws Exception {
        Drone creado = crearDronePrueba();

        Drone duplicado = new Drone(creado.getId(), "OTRO-SERIAL", "OtroModelo", "OtroFabricante", 1.0);

        assertFalse(droneDAO.crear(duplicado));
    }

    @Test
    void obtenerPorIdDevuelveElDroneCorrespondiente() throws Exception {
        Drone creado = crearDronePrueba();

        Drone encontrado = droneDAO.obtenerPorId(creado.getId());

        assertNotNull(encontrado);
        assertEquals(creado.getSerial(), encontrado.getSerial());
        assertEquals("ModeloTest", encontrado.getModelo());
        assertEquals("FabricanteTest", encontrado.getFabricante());
        assertEquals(4.2, encontrado.getPeso());
    }

    @Test
    void obtenerPorIdDevuelveNuloSiElDroneNoExiste() throws Exception {
        assertNull(droneDAO.obtenerPorId("id-inexistente"));
    }

    @Test
    void actualizarModificaLosDatosDelDroneExistente() throws Exception {
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
    void actualizarDevuelveFalsoSiElDroneNoExiste() throws Exception {
        Drone inexistente = new Drone("id-inexistente", "NO-EXISTE", "X", "Y", 1.0);

        assertFalse(droneDAO.actualizar(inexistente));
    }

    @Test
    void eliminarQuitaElDroneDeLaBaseDeDatos() throws Exception {
        Drone creado = crearDronePrueba();

        assertTrue(droneDAO.eliminar(creado.getId()));
        assertNull(droneDAO.obtenerPorId(creado.getId()));

        idCreado = null;
    }

    @Test
    void eliminarDevuelveFalsoSiElDroneNoExiste() throws Exception {
        assertFalse(droneDAO.eliminar("id-inexistente"));
    }

    @Test
    void crearAgriculturaInsertaEnDroneYEnAgricultura() throws Exception {
        String idUnico = "TEST-AGRO-" + UUID.randomUUID();
        Agricultura agricultura = new Agricultura(idUnico, "SER-AGRO", "ModeloAgro", "FabricanteAgro", 6.0, 20.0);

        assertTrue(droneDAO.crear(agricultura));
        idCreado = idUnico;

        Drone encontrado = droneDAO.obtenerPorId(idUnico);
        Agricultura encontradaAgricultura = assertInstanceOf(Agricultura.class, encontrado);
        assertEquals("SER-AGRO", encontradaAgricultura.getSerial());
        assertEquals(20.0, encontradaAgricultura.getCapacidadTanque());
    }

    @Test
    void crearVigilanciaInsertaEnDroneYEnVigilancia() throws Exception {
        String idUnico = "TEST-VIG-" + UUID.randomUUID();
        Vigilancia vigilancia = new Vigilancia(idUnico, "SER-VIG", "ModeloVig", "FabricanteVig", 3.0, true);

        assertTrue(droneDAO.crear(vigilancia));
        idCreado = idUnico;

        Drone encontrado = droneDAO.obtenerPorId(idUnico);
        Vigilancia encontradaVigilancia = assertInstanceOf(Vigilancia.class, encontrado);
        assertEquals("SER-VIG", encontradaVigilancia.getSerial());
        assertTrue(encontradaVigilancia.isDeteccionTermica());
    }

    @Test
    void actualizarAgriculturaModificaCamposBaseYPropios() throws Exception {
        String idUnico = "TEST-AGRO-" + UUID.randomUUID();
        droneDAO.crear(new Agricultura(idUnico, "SER-AGRO", "ModeloAgro", "FabricanteAgro", 6.0, 20.0));
        idCreado = idUnico;

        Agricultura actualizacion = new Agricultura(idUnico, "SER-AGRO", "ModeloAgroNuevo", "FabricanteAgro", 6.0, 35.5);
        assertTrue(droneDAO.actualizar(actualizacion));

        Agricultura actualizada = assertInstanceOf(Agricultura.class, droneDAO.obtenerPorId(idUnico));
        assertEquals("ModeloAgroNuevo", actualizada.getModelo());
        assertEquals(35.5, actualizada.getCapacidadTanque());
    }

    @Test
    void eliminarUnaAgriculturaBorraLaFilaDeAgriculturaEnCascada() throws Exception {
        String idUnico = "TEST-AGRO-" + UUID.randomUUID();
        droneDAO.crear(new Agricultura(idUnico, "SER-AGRO", "ModeloAgro", "FabricanteAgro", 6.0, 20.0));

        assertTrue(droneDAO.eliminar(idUnico));

        assertNull(droneDAO.obtenerPorId(idUnico));
    }
}
