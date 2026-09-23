package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para verificar la implementación del patrón de diseño
 * Facade: {@link FabricaDrones}, que concentra en una sola llamada la
 * elección de la factoría concreta ({@link AgriculturaFactory} o
 * {@link VigilanciaFactory}), su configuración con el dato propio de la
 * especialización y la creación del dron a través de la plantilla
 * {@link DroneFactory} (Factory Method), sin que el cliente conozca ninguna
 * de esas tres clases.
 */
class FabricaDronesTest {

    private final FabricaDrones fabrica = new FabricaDrones();

    // ------------------------------------------------------------------
    // Constantes de tipo: la fachada expone las mismas que el subsistema
    // ------------------------------------------------------------------

    @Test
    void lasConstantesDeTipoSonLasMismasQueLasDeDroneFactory() {
        assertSame(DroneFactory.TIPO_AGRICULTURA, FabricaDrones.TIPO_AGRICULTURA);
        assertSame(DroneFactory.TIPO_VIGILANCIA, FabricaDrones.TIPO_VIGILANCIA);
    }

    @Test
    void tiposDisponiblesDelegaEnDroneFactory() {
        assertEquals(DroneFactory.tiposDisponibles(), fabrica.tiposDisponibles());
    }

    @Test
    void tiposDisponiblesIncluyeAgriculturaYVigilanciaEnEseOrden() {
        assertEquals(List.of(FabricaDrones.TIPO_AGRICULTURA, FabricaDrones.TIPO_VIGILANCIA),
                fabrica.tiposDisponibles());
    }

    // ------------------------------------------------------------------
    // crear(...) con tipo Agricultura
    // ------------------------------------------------------------------

    @Test
    void crearConTipoAgriculturaDevuelveUnaAgriculturaConSusDatosBasicos() {
        Drone drone = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D001", "SN-AGR-1145", "AgroWing X2", "DJI", 12.4, 18.0, false);

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals("D001", agricultura.getId());
        assertEquals("SN-AGR-1145", agricultura.getSerial());
        assertEquals("AgroWing X2", agricultura.getModelo());
        assertEquals("DJI", agricultura.getFabricante());
        assertEquals(12.4, agricultura.getPeso());
    }

    @Test
    void crearConTipoAgriculturaUsaLaCapacidadDeTanqueRecibida() {
        Drone drone = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D001", "SN-001", "ModeloX", "FabricanteX", 2.5, 18.0, false);

        Agricultura agricultura = assertInstanceOf(Agricultura.class, drone);
        assertEquals(18.0, agricultura.getCapacidadTanque());
    }

    @Test
    void crearConTipoAgriculturaIgnoraElParametroDeDeteccionTermica() {
        Drone conTrue = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D001", "SN-001", "ModeloX", "FabricanteX", 2.5, 18.0, true);
        Drone conFalse = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D002", "SN-002", "ModeloY", "FabricanteY", 3.0, 18.0, false);

        assertInstanceOf(Agricultura.class, conTrue);
        assertInstanceOf(Agricultura.class, conFalse);
        assertEquals(18.0, ((Agricultura) conTrue).getCapacidadTanque());
        assertEquals(18.0, ((Agricultura) conFalse).getCapacidadTanque());
    }

    // ------------------------------------------------------------------
    // crear(...) con tipo Vigilancia
    // ------------------------------------------------------------------

    @Test
    void crearConTipoVigilanciaDevuelveUnaVigilanciaConSusDatosBasicos() {
        Drone drone = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "D002", "SN-VIG-0932", "SkyGuard 500", "Parrot", 8.1, 0.0, true);

        Vigilancia vigilancia = assertInstanceOf(Vigilancia.class, drone);
        assertEquals("D002", vigilancia.getId());
        assertEquals("SN-VIG-0932", vigilancia.getSerial());
        assertEquals("SkyGuard 500", vigilancia.getModelo());
        assertEquals("Parrot", vigilancia.getFabricante());
        assertEquals(8.1, vigilancia.getPeso());
    }

    @Test
    void crearConTipoVigilanciaUsaLaDeteccionTermicaRecibida() {
        Drone conDeteccion = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "D002", "SN-002", "ModeloY", "FabricanteY", 3.1, 0.0, true);
        Drone sinDeteccion = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "D003", "SN-003", "ModeloZ", "FabricanteZ", 4.0, 0.0, false);

        assertTrue(((Vigilancia) conDeteccion).isDeteccionTermica());
        assertTrue(!((Vigilancia) sinDeteccion).isDeteccionTermica());
    }

    @Test
    void crearConTipoVigilanciaIgnoraElParametroDeCapacidadDeTanque() {
        Drone drone = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "D002", "SN-002", "ModeloY", "FabricanteY", 3.1, 999.0, true);

        // Vigilancia no tiene capacidad de tanque: el valor recibido no tiene
        // donde guardarse. Basta con comprobar que igual se crea la Vigilancia
        // esperada, sin verse afectada por ese parametro.
        assertInstanceOf(Vigilancia.class, drone);
        assertTrue(((Vigilancia) drone).isDeteccionTermica());
    }

    // ------------------------------------------------------------------
    // Un tipo distinto a Agricultura se trata como Vigilancia
    // ------------------------------------------------------------------

    @Test
    void unTipoQueNoEsAgriculturaSeConstruyeComoVigilancia() {
        // FabricaDrones.crear (igual que el DroneFactory al que reemplaza en
        // el controlador) solo distingue el caso Agricultura por nombre; todo
        // lo demas cae en Vigilancia. Se documenta ese comportamiento aqui.
        Drone drone = fabrica.crear("Otro tipo cualquiera",
                "D099", "SN-099", "ModeloRaro", "FabricanteRaro", 1.0, 0.0, true);

        assertInstanceOf(Vigilancia.class, drone);
    }

    @Test
    void unTipoNuloSeConstruyeComoVigilancia() {
        Drone drone = fabrica.crear(null,
                "D099", "SN-099", "ModeloRaro", "FabricanteRaro", 1.0, 0.0, false);

        assertInstanceOf(Vigilancia.class, drone);
    }

    // ------------------------------------------------------------------
    // Cada llamada crea una instancia independiente
    // ------------------------------------------------------------------

    @Test
    void cadaLlamadaACrearDevuelveUnaInstanciaDistinta() {
        Drone primero = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D001", "SN-001", "ModeloX", "FabricanteX", 2.5, 18.0, false);
        Drone segundo = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "D001", "SN-001", "ModeloX", "FabricanteX", 2.5, 18.0, false);

        assertNotSame(primero, segundo);
    }

    @Test
    void crearUnDroneDeCadaTipoEnLaMismaFachadaNoLosMezcla() {
        // El mismo escenario que arma onFacade en el controlador: un dron de
        // cada tipo con la misma instancia de la fachada.
        Drone agricultura = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "A-DEMO", "SN-AGR-DEMO", "AgroWing X2", "DJI", 12.4, 18.0, false);
        Drone vigilancia = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "V-DEMO", "SN-VIG-DEMO", "SkyGuard 500", "Parrot", 8.1, 0.0, true);

        assertInstanceOf(Agricultura.class, agricultura);
        assertInstanceOf(Vigilancia.class, vigilancia);
        assertEquals(18.0, ((Agricultura) agricultura).getCapacidadTanque());
        assertTrue(((Vigilancia) vigilancia).isDeteccionTermica());
    }

    // ------------------------------------------------------------------
    // El cliente solo depende de la fachada, no de las factorias concretas
    // ------------------------------------------------------------------

    @Test
    void fabricaDronesNoExponeNingunaFactoriaConcreta() {
        // La fachada no declara ningun metodo que reciba o devuelva
        // DroneFactory, AgriculturaFactory ni VigilanciaFactory: el unico
        // punto de entrada del cliente es crear(...) con datos primitivos.
        for (var metodo : FabricaDrones.class.getDeclaredMethods()) {
            if (!java.lang.reflect.Modifier.isPublic(metodo.getModifiers())) {
                continue;
            }
            assertTrue(!DroneFactory.class.isAssignableFrom(metodo.getReturnType()));
            for (Class<?> parametro : metodo.getParameterTypes()) {
                assertTrue(!DroneFactory.class.isAssignableFrom(parametro));
            }
        }
    }
}
