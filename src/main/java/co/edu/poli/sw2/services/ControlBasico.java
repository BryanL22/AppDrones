package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Implementación concreta del patrón Bridge que representa el "Control básico".
 *
 * <p>Modela el esquema de control manual y directo para los drones del
 * sistema. Recibe el {@link Drone} como parámetro durante la llamada a
 * {@link #ejecutarAccion(Drone)} (relación de dependencia en UML), sin
 * almacenar ninguna referencia en sus atributos.</p>
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
    public String ejecutarAccion(Drone drone) {
        String infoDrone = drone != null
                ? drone.getModelo() + " (ID: " + drone.getId() + ")"
                : "Dron no asignado";
        return "[" + NOMBRE + "] Modo manual activo: el operador gestiona directamente el dron " + infoDrone + ".";
    }
}
