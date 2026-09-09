package co.edu.poli.sw2.services;

/**
 * Decorador concreto (patron Decorator) que agrega la caracteristica de una
 * bateria adicional a un {@link DronComponent}: suma a la descripcion del
 * componente envuelto la autonomia extra que aporta, sin modificar el
 * {@code Drone} original.
 *
 * <p>Al extender {@link DronWrapper} y recibir un {@code DronComponent}
 * generico, puede envolver tanto un drone recien adaptado como otra
 * {@code BateriaAdicional} (u otro decorador futuro) ya aplicada,
 * permitiendo agregar mas de una caracteristica encadenando envoltorios.</p>
 */
public class BateriaAdicional extends DronWrapper {

    private static final double AUTONOMIA_POR_DEFECTO_MINUTOS = 20.0;

    private final double autonomiaAdicionalMinutos;

    /**
     * Envuelve el componente recibido agregandole una bateria adicional con
     * la autonomia extra por defecto.
     *
     * @param componente componente (drone adaptado u otro decorador) a decorar.
     */
    public BateriaAdicional(DronComponent componente) {
        this(componente, AUTONOMIA_POR_DEFECTO_MINUTOS);
    }

    /**
     * Envuelve el componente recibido agregandole una bateria adicional con
     * la autonomia extra indicada.
     *
     * @param componente componente (drone adaptado u otro decorador) a decorar.
     * @param autonomiaAdicionalMinutos autonomia extra que aporta la bateria, en minutos.
     */
    public BateriaAdicional(DronComponent componente, double autonomiaAdicionalMinutos) {
        super(componente);
        this.autonomiaAdicionalMinutos = autonomiaAdicionalMinutos;
    }

    /**
     * Describe el drone agregando la bateria adicional a la descripcion del
     * componente decorado.
     *
     * @return la descripcion del componente decorado, mas la bateria adicional.
     */
    @Override
    public String describir() {
        return super.describir() + " + Bateria adicional (+" + autonomiaAdicionalMinutos + " min de autonomia)";
    }

    /**
     * Devuelve la autonomia adicional que aporta esta bateria.
     *
     * @return la autonomia adicional, en minutos.
     */
    public double getAutonomiaAdicionalMinutos() {
        return autonomiaAdicionalMinutos;
    }
}
