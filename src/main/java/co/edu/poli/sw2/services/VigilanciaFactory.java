package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;

/**
 * Fabrica concreta (patron Factory Method) de drones {@link Vigilancia}.
 *
 * <p>Recibe en su constructor el dato propio de esta especializacion (si
 * tiene deteccion termica) y lo usa al construir cada dron que se le pida.</p>
 */
public class VigilanciaFactory extends DroneFactory {

    private final boolean deteccionTermica;

    /**
     * Crea la fabrica para drones de vigilancia con la deteccion termica
     * indicada.
     *
     * @param deteccionTermica si el dron debe contar con deteccion termica.
     */
    public VigilanciaFactory(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
    }

    @Override
    protected Drone crearEspecializado(String id, String serial, String modelo, String fabricante, double peso) {
        return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
    }
}
