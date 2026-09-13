package co.edu.poli.sw2.services;

/**
 * Implementor concreto (ConcreteImplementor) del patrón Bridge que representa
 * el "Control básico".
 *
 * <p>Modela el esquema de control manual y directo para los drones del
 * sistema. No conoce ni referencia ningún {@link co.edu.poli.sw2.model.Drone}
 * en particular: esa asociación la hace {@link ControlVuelo} (la
 * Abstracción del puente) en tiempo de ejecución.</p>
 */
public class ControlBasico implements ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control básico";

    @Override
    public String getTipoControl() {
        return NOMBRE;
    }

    @Override
    public String ejecutarAccion() {
        return "Modo manual activo: el operador gestiona directamente el dron.";
    }
}
