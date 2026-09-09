package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Decorador base (patron Decorator) que envuelve un {@link DronComponent}
 * por composicion y, por defecto, delega en el toda su descripcion.
 *
 * <p>Guarda una referencia a {@code DronComponent} -no a un {@link Drone}
 * directamente- para poder decorar tanto un drone recien adaptado
 * ({@link DronComponent#of(Drone)}) como otro decorador ya aplicado. En vez
 * de una clase abstracta {@code DronDecorator}, esta clase concreta cumple
 * ese rol: sirve de base para agregar caracteristicas adicionales
 * encadenando envoltorios via constructor, como hace {@link BateriaAdicional}
 * al extenderla.</p>
 */
public class DronWrapper implements DronComponent {

    private final DronComponent componente;

    /**
     * Adapta el drone recibido a un {@code DronComponent} y lo envuelve.
     *
     * @param drone drone existente a decorar.
     */
    public DronWrapper(Drone drone) {
        this(DronComponent.of(drone));
    }

    /**
     * Envuelve el componente recibido.
     *
     * @param componente componente (drone adaptado u otro decorador) a decorar.
     */
    public DronWrapper(DronComponent componente) {
        this.componente = componente;
    }

    /**
     * Devuelve el componente envuelto por este wrapper.
     *
     * @return el componente decorado.
     */
    protected DronComponent getComponente() {
        return componente;
    }

    /**
     * Describe el drone delegando en el componente envuelto.
     *
     * @return la descripcion del componente envuelto.
     */
    @Override
    public String describir() {
        return componente.describir();
    }
}
