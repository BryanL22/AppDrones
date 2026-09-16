package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Implementación concreta del patrón Bridge que representa el "Control autónomo".
 *
 * <p>Modela el esquema de control automatizado y programado para los drones
 * del sistema. Recibe el {@link Drone} como parámetro durante la llamada a
 * {@link #ejecutarAccion(Drone)} (relación de dependencia en UML), sin
 * almacenar ninguna referencia en sus atributos.</p>
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
    public String ejecutarAccion(Drone drone) {
        String infoDrone = drone != null
                ? drone.getModelo() + " (ID: " + drone.getId() + ")"
                : "Dron no asignado";
        return "[" + NOMBRE + "] Modo autopiloto activo: el sistema ejecuta la misión de forma autónoma para " + infoDrone + ".";
    }
}
