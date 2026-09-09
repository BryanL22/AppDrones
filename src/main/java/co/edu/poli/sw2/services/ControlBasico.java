package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Abstracción refinada en el patrón Bridge que representa el "Control básico".
 *
 * <p>Modela el esquema de control manual y directo para los drones en el sistema.</p>
 */
public class ControlBasico extends ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control básico";

    /**
     * Crea una instancia de Control Básico asociada a un dron.
     *
     * @param drone el dron sobre el cual opera el control.
     */
    public ControlBasico(Drone drone) {
        super(drone);
    }

    @Override
    public String getTipoControl() {
        return NOMBRE;
    }

    @Override
    public String ejecutarAccion() {
        String infoDrone = drone != null ? drone.getModelo() + " (ID: " + drone.getId() + ")" : "Dron no asignado";
        return "[Control Básico] Modo manual activo: el operador gestiona directamente el dron " + infoDrone + ".";
    }
}
