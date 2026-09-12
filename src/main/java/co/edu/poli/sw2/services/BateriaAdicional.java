package co.edu.poli.sw2.services;

/**
 * Decorador concreto (patron Decorator) que agrega una bateria adicional
 * a un {@link DronComponent}, sumando autonomia extra a la descripcion
 * sin modificar el drone original.
 */
public class BateriaAdicional extends DronWrapper {

    double autonomiaAdicionalMinutos;

    /**
     * Decora el componente con la autonomia extra indicada.
     *
     * @param componente componente a decorar.
     * @param autonomiaAdicionalMinutos autonomia extra en minutos.
     */
    public BateriaAdicional(DronComponent componente, double autonomiaAdicionalMinutos) {
        super(componente);
        this.autonomiaAdicionalMinutos = autonomiaAdicionalMinutos;
    }

    @Override
    public String describir() {
        return super.describir() + " + Bateria adicional (+" + autonomiaAdicionalMinutos + " min de autonomia)";
    }

    /**
     * @return autonomia adicional en minutos.
     */
    public double getAutonomiaAdicionalMinutos() {
        return autonomiaAdicionalMinutos;
    }
}
