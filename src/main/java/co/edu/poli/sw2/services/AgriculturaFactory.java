package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;

/**
 * Fabrica concreta (patron Factory Method) de drones {@link Agricultura}.
 *
 * <p>Recibe en su constructor el dato propio de esta especializacion (la
 * capacidad del tanque) y lo usa al construir cada dron que se le pida.</p>
 */
public class AgriculturaFactory extends DroneFactory {

    private final double capacidadTanque;

    /**
     * Crea la fabrica para drones de agricultura con la capacidad de tanque
     * indicada.
     *
     * @param capacidadTanque capacidad del tanque de insumos en litros.
     */
    public AgriculturaFactory(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
    }

    @Override
    protected Drone crearEspecializado(String id, String serial, String modelo, String fabricante, double peso) {
        return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
    }
}
