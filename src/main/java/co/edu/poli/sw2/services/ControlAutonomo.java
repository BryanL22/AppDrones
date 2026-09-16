package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Implementación concreta del patrón Bridge que representa el "Control autónomo".
 *
 * <p>Modela el esquema de control automatizado y programado para los drones
 * del sistema. Incorpora un atributo propio de instancia {@link #algoritmoNavegacion}
 * que define el tipo de algoritmo de vuelo autónomo ("Patrullaje" o "Riego").</p>
 */
public class ControlAutonomo implements ControlDrone {

    /**
     * Nombre formal del tipo de control.
     */
    public static final String NOMBRE = "Control autónomo";

    /**
     * Constante para el algoritmo de navegación de Patrullaje.
     */
    public static final String ALGORITMO_PATRULLAJE = "Patrullaje";

    /**
     * Constante para el algoritmo de navegación de Riego.
     */
    public static final String ALGORITMO_RIEGO = "Riego";

    /**
     * Algoritmo de navegación utilizado por el control autónomo ("Patrullaje" o "Riego").
     */
    private String algoritmoNavegacion;

    /**
     * Constructor por defecto: inicializa con el algoritmo "Patrullaje".
     */
    public ControlAutonomo() {
        this(ALGORITMO_PATRULLAJE);
    }

    /**
     * Constructor con algoritmo de navegación específico.
     *
     * @param algoritmoNavegacion nombre del algoritmo ("Patrullaje" o "Riego").
     */
    public ControlAutonomo(String algoritmoNavegacion) {
        setAlgoritmoNavegacion(algoritmoNavegacion);
    }

    public String getAlgoritmoNavegacion() {
        return algoritmoNavegacion;
    }

    /**
     * Asigna el algoritmo de navegación ("Patrullaje" o "Riego").
     *
     * @param algoritmoNavegacion "Patrullaje" o "Riego".
     */
    public void setAlgoritmoNavegacion(String algoritmoNavegacion) {
        if (ALGORITMO_RIEGO.equalsIgnoreCase(algoritmoNavegacion)) {
            this.algoritmoNavegacion = ALGORITMO_RIEGO;
        } else {
            this.algoritmoNavegacion = ALGORITMO_PATRULLAJE;
        }
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
        return "[" + NOMBRE + "] Modo autopiloto activo (algoritmo: " + algoritmoNavegacion
                + "): el sistema ejecuta la misión de forma autónoma para " + infoDrone + ".";
    }
}
