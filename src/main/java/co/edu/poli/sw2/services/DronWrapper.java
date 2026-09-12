package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Decorador base (patron Decorator) que envuelve un {@link DronComponent}
 * y delega en el su descripcion. Sirve de base para decoradores concretos
 * como {@link BateriaAdicional}.
 */
public class DronWrapper implements DronComponent {

    DronComponent componente;

    /**
     * Adapta el drone recibido a {@code DronComponent} y lo envuelve.
     *
     * @param drone drone a decorar.
     */
    public DronWrapper(Drone drone) {
        this(DronComponent.of(drone));
    }

    /**
     * Envuelve el componente recibido.
     *
     * @param componente componente a decorar.
     */
    public DronWrapper(DronComponent componente) {
        this.componente = componente;
    }

    /**
     * Describe el drone delegando en el componente envuelto.
     *
     * @return descripcion del componente envuelto.
     */
    @Override
    public String describir() {
        return componente.describir();
    }
}
