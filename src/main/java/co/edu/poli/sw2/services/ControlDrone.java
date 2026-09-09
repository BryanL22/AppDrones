package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Abstracción base en el patrón Bridge para la gestión de controles de vuelo.
 *
 * <p>Mantiene una referencia a la entidad {@link Drone} sobre la que opera,
 * desacoplando la lógica y modalidad del control de vuelo ("Control básico"
 * y "Control autónomo") de las características propias de los drones
 * registrados en la aplicación.</p>
 */
public abstract class ControlDrone {

    /**
     * Referencia al dron administrado (puente con la entidad del modelo).
     */
    protected Drone drone;

    /**
     * Crea un control de vuelo asociado a un dron.
     *
     * @param drone el dron a controlar (puede ser null si se asigna después).
     */
    public ControlDrone(Drone drone) {
        this.drone = drone;
    }

    /**
     * Devuelve el dron asociado al control.
     *
     * @return la instancia de {@link Drone} asociada.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Asigna o cambia el dron asociado a este control.
     *
     * @param drone el nuevo dron a asociar.
     */
    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    /**
     * Devuelve el nombre del tipo de control ("Control básico" o "Control autónomo").
     *
     * @return texto con el nombre del tipo de control.
     */
    public abstract String getTipoControl();

    /**
     * Ejecuta el protocolo o acción correspondiente a la modalidad de control.
     *
     * @return mensaje con la descripción de la acción ejecutada.
     */
    public abstract String ejecutarAccion();

    /**
     * Factoría utilitaria que construye el control adecuado según el tipo indicado.
     *
     * @param tipoControl "Control básico" o "Control autónomo".
     * @param drone dron a asociar al control.
     * @return instancia concreta de {@link ControlDrone}.
     */
    public static ControlDrone crear(String tipoControl, Drone drone) {
        if ("Control autónomo".equalsIgnoreCase(tipoControl) || "Control autonomo".equalsIgnoreCase(tipoControl)) {
            return new ControlAutonomo(drone);
        }
        return new ControlBasico(drone);
    }
}
