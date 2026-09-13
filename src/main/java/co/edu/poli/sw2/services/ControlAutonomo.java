package co.edu.poli.sw2.services;

/**
 * Implementor concreto (ConcreteImplementor) del patrón Bridge que representa
 * el "Control autónomo".
 *
 * <p>Modela el esquema de control automatizado y programado para los drones
 * del sistema. No conoce ni referencia ningún {@link co.edu.poli.sw2.model.Drone}
 * en particular: esa asociación la hace {@link ControlVuelo} (la
 * Abstracción del puente) en tiempo de ejecución.</p>
 */
public class ControlAutonomo implements ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control autónomo";

    @Override
    public String getTipoControl() {
        return NOMBRE;
    }

    @Override
    public String ejecutarAccion() {
        return "Modo autopiloto activo: el sistema ejecuta la misión de forma autónoma.";
    }
}
