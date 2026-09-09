package co.edu.poli.sw2.services;

/**
 * Decorador concreto (patron Decorator) que agrega la caracteristica de una
 * bateria adicional a un {@link DronWrapper}: sobre la descripcion y el peso
 * que ya trae el envoltorio, suma el peso propio de la bateria y la
 * autonomia extra que aporta, sin modificar el {@code Drone} original.
 */
public class BateriaAdicional {

    private static final double PESO_POR_DEFECTO_KG = 0.5;
    private static final double AUTONOMIA_POR_DEFECTO_MINUTOS = 20.0;

    private final DronWrapper dronWrapper;
    private final double pesoBateriaKg;
    private final double autonomiaAdicionalMinutos;

    /**
     * Envuelve el wrapper recibido agregandole una bateria adicional con el
     * peso y la autonomia extra por defecto.
     *
     * @param dronWrapper envoltorio a decorar.
     */
    public BateriaAdicional(DronWrapper dronWrapper) {
        this(dronWrapper, PESO_POR_DEFECTO_KG, AUTONOMIA_POR_DEFECTO_MINUTOS);
    }

    /**
     * Envuelve el wrapper recibido agregandole una bateria adicional con el
     * peso y la autonomia extra indicados.
     *
     * @param dronWrapper envoltorio a decorar.
     * @param pesoBateriaKg peso propio de la bateria adicional, en kilogramos.
     * @param autonomiaAdicionalMinutos autonomia extra que aporta la bateria, en minutos.
     */
    public BateriaAdicional(DronWrapper dronWrapper, double pesoBateriaKg, double autonomiaAdicionalMinutos) {
        this.dronWrapper = dronWrapper;
        this.pesoBateriaKg = pesoBateriaKg;
        this.autonomiaAdicionalMinutos = autonomiaAdicionalMinutos;
    }

    /**
     * Describe el drone agregando la bateria adicional a la descripcion del
     * envoltorio decorado.
     *
     * @return la descripcion del envoltorio decorado, mas la bateria adicional.
     */
    public String describir() {
        return dronWrapper.describir() + " + Bateria adicional (+" + autonomiaAdicionalMinutos + " min de autonomia)";
    }

    /**
     * Devuelve el peso total, sumando el peso propio de la bateria adicional
     * al peso del envoltorio decorado.
     *
     * @return el peso total, en kilogramos, incluyendo la bateria adicional.
     */
    public double getPesoTotal() {
        return dronWrapper.getPesoTotal() + pesoBateriaKg;
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
