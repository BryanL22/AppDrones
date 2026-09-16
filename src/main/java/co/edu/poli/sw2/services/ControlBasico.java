package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Implementación concreta del patrón Bridge que representa el "Control básico".
 *
 * <p>Modela el esquema de control manual y directo para los drones del
 * sistema. Incorpora un atributo propio de instancia {@link #nivelSensibilidad}
 * para calibrar la respuesta de los mandos manuales.</p>
 */
public class ControlBasico implements ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control básico";

    /**
     * Valor por defecto de la sensibilidad del control básico.
     */
    public static final int SENSIBILIDAD_POR_DEFECTO = 5;

    /**
     * Nivel de sensibilidad de los mandos manuales (escala numérica, ej. 1 a 10).
     */
    private int nivelSensibilidad;

    /**
     * Constructor por defecto con nivel de sensibilidad estándar.
     */
    public ControlBasico() {
        this(SENSIBILIDAD_POR_DEFECTO);
    }

    /**
     * Constructor con nivel de sensibilidad personalizado.
     *
     * @param nivelSensibilidad nivel de respuesta de los mandos.
     */
    public ControlBasico(int nivelSensibilidad) {
        this.nivelSensibilidad = nivelSensibilidad;
    }

    public int getNivelSensibilidad() {
        return nivelSensibilidad;
    }

    public void setNivelSensibilidad(int nivelSensibilidad) {
        this.nivelSensibilidad = nivelSensibilidad;
    }

    @Override
    public String getTipoControl() {
        return NOMBRE;
    }

    @Override
    public String ejecutarAccion(Drone drone) {
        String infoDrone = drone != null
                ? drone.getModelo() + " (ID: " + drone.getId() + ")"
                : "Dron no asignado";
        return "[" + NOMBRE + "] Modo manual activo (sensibilidad: " + nivelSensibilidad
                + "): el operador gestiona directamente el dron " + infoDrone + ".";
    }
}
