package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Abstracción refinada en el patrón Bridge que representa el "Control autónomo".
 *
 * <p>Modela el esquema de control automatizado y programado para los drones en el sistema.</p>
 */
public class ControlAutonomo extends ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control autónomo";

    /**
     * Crea una instancia de Control Autónomo asociada a un dron.
     *
     * @param drone el dron sobre el cual opera el control.
     */
    public ControlAutonomo(Drone drone) {
        super(drone);
    }

    @Override
    public String getTipoControl() {
        return NOMBRE;
    }

    @Override
    public String ejecutarAccion() {
        String infoDrone = drone != null ? drone.getModelo() + " (ID: " + drone.getId() + ")" : "Dron no asignado";
        return "[Control Autónomo] Modo autopiloto activo: el sistema ejecuta la misión de forma autónoma para " + infoDrone + ".";
    }
}
