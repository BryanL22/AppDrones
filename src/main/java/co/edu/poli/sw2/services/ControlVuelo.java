package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Abstracción del patrón Bridge: representa el control de vuelo que se
 * ejercerá sobre un dron, sin acoplarse a una modalidad de control
 * concreta. Delega la ejecución en un {@link ControlDrone} (el Implementor
 * del puente), que puede ser {@link ControlBasico} o {@link ControlAutonomo}
 * indistintamente, y puede intercambiarse en cualquier momento con
 * {@link #setControl(ControlDrone)}.
 *
 * <p>Es esta clase —y no {@link Drone}— la que conoce y asocia un dron con
 * su modalidad de control. La asociación vive únicamente mientras se usa
 * esta instancia (por ejemplo, para probar el control desde la vista): no
 * se persiste en base de datos ni se guarda dentro de {@link Drone}, que
 * permanece completamente ajeno al patrón Bridge.</p>
 */
public class ControlVuelo {

    private Drone drone;
    private ControlDrone control;

    /**
     * Crea un control de vuelo asociando un dron con una implementación
     * concreta de control.
     *
     * @param drone dron sobre el cual se ejecuta el control (puede ser null si se asigna después).
     * @param control implementación concreta del control ({@link ControlBasico} o {@link ControlAutonomo}).
     */
    public ControlVuelo(Drone drone, ControlDrone control) {
        this.drone = drone;
        this.control = control != null ? control : new ControlBasico();
    }

    /**
     * Devuelve el dron asociado a este control.
     *
     * @return el dron asociado, o {@code null} si no se ha asignado ninguno.
     */
    public Drone getDrone() {
        return drone;
    }

    /**
     * Asigna o cambia el dron sobre el que opera este control.
     *
     * @param drone el nuevo dron a asociar.
     */
    public void setDrone(Drone drone) {
        this.drone = drone;
    }

    /**
     * Devuelve la implementación de control (Implementor) usada actualmente.
     *
     * @return la implementación concreta del control.
     */
    public ControlDrone getControl() {
        return control;
    }

    /**
     * Cambia, en tiempo de ejecución, la implementación de control usada
     * (por ejemplo, de {@link ControlBasico} a {@link ControlAutonomo}) sin
     * afectar al dron asociado. Este intercambio dinámico es la esencia del
     * patrón Bridge.
     *
     * @param control la nueva implementación de control a usar.
     */
    public void setControl(ControlDrone control) {
        this.control = control != null ? control : new ControlBasico();
    }

    /**
     * Devuelve el nombre del tipo de control actualmente asignado
     * ("Control básico" o "Control autónomo").
     *
     * @return el nombre del tipo de control.
     */
    public String getTipoControl() {
        return control.getTipoControl();
    }

    /**
     * Ejecuta la acción de control delegando en el Implementor y agregando
     * el contexto del dron asociado.
     *
     * @return mensaje detallado con la acción ejecutada y el dron involucrado.
     */
    public String ejecutarAccion() {
        String infoDrone = drone != null
                ? drone.getModelo() + " (ID: " + drone.getId() + ")"
                : "Dron no asignado";
        return "[" + control.getTipoControl() + "] " + control.ejecutarAccion() + " Dron: " + infoDrone + ".";
    }

    /**
     * Devuelve el mensaje breve que debe mostrarse al usuario: únicamente
     * "Dron con control autónomo" o "Dron con control básico".
     *
     * @return el mensaje breve correspondiente al tipo de control asignado.
     */
    public String descripcionBreve() {
        return "Dron con " + control.getTipoControl().toLowerCase();
    }

    /**
     * Factoría utilitaria que construye la implementación de control
     * (Implementor) adecuada según el tipo indicado.
     *
     * @param tipoControl "Control básico" o "Control autónomo".
     * @return instancia concreta de {@link ControlDrone}.
     */
    public static ControlDrone crearControl(String tipoControl) {
        if (ControlAutonomo.NOMBRE.equalsIgnoreCase(tipoControl) || "Control autonomo".equalsIgnoreCase(tipoControl)) {
            return new ControlAutonomo();
        }
        return new ControlBasico();
    }

    /**
     * Crea un {@link ControlVuelo} asociando el dron indicado con la
     * implementación de control correspondiente al tipo indicado.
     *
     * @param tipoControl "Control básico" o "Control autónomo".
     * @param drone dron a asociar al control.
     * @return una nueva instancia de {@link ControlVuelo}.
     */
    public static ControlVuelo crear(String tipoControl, Drone drone) {
        return new ControlVuelo(drone, crearControl(tipoControl));
    }
}
