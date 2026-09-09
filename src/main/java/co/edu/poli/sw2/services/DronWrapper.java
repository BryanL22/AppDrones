package co.edu.poli.sw2.services;

/**
 * Envoltorio (patron Decorator) que envuelve un {@link DronComponent} por
 * composicion y, por defecto, delega en el toda su descripcion y peso.
 *
 * <p>En vez de una clase abstracta {@code DronDecorator}, esta clase
 * concreta cumple ese rol: sirve de base para agregar caracteristicas
 * adicionales encadenando envoltorios via constructor, como hace
 * {@link BateriaAdicional} al envolver una instancia de {@code DronWrapper}.</p>
 */
public class DronWrapper {

    private final DronComponent componente;

    /**
     * Envuelve el componente recibido.
     *
     * @param componente componente (drone ya envuelto) a decorar.
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
    public String describir() {
        return componente.describir();
    }

    /**
     * Devuelve el peso total delegando en el componente envuelto.
     *
     * @return el peso del componente envuelto, en kilogramos.
     */
    public double getPesoTotal() {
        return componente.getPesoTotal();
    }
}
