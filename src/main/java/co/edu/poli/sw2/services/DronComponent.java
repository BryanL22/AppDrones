package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Componente base del patron Decorator: envuelve un {@link Drone} ya
 * existente (del paquete {@code model}, sin modificarlo) para que
 * {@link DronWrapper} y los decoradores concretos como
 * {@link BateriaAdicional} puedan agregarle caracteristicas por composicion,
 * encadenando envoltorios via constructor:
 * {@code Drone -> DronComponent -> DronWrapper -> BateriaAdicional}.
 */
public class DronComponent {

    private final Drone drone;

    /**
     * Envuelve el drone recibido.
     *
     * @param drone drone existente a decorar.
     */
    public DronComponent(Drone drone) {
        this.drone = drone;
    }

    /**
     * Devuelve el drone envuelto por este componente.
     *
     * @return el drone original, sin decorar.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Describe el drone envuelto con sus datos basicos. Los decoradores le
     * agregan texto a esta descripcion en vez de reemplazarla.
     *
     * @return una descripcion legible del drone.
     */
    public String describir() {
        return drone.getModelo() + " (" + drone.getFabricante() + "), " + drone.getPeso() + " kg";
    }

    /**
     * Devuelve el peso del drone envuelto. Los decoradores que agregan
     * accesorios con peso propio (por ejemplo, {@link BateriaAdicional}) lo
     * suman a este valor.
     *
     * @return el peso del drone, en kilogramos.
     */
    public double getPesoTotal() {
        return drone.getPeso();
    }
}
