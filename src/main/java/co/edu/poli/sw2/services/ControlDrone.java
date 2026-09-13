package co.edu.poli.sw2.services;

/**
 * Implementor del patrón Bridge para la gestión de controles de vuelo.
 *
 * <p>Declara las operaciones que cualquier modalidad de control (básico,
 * autónomo, o cualquier otra que se agregue en el futuro) debe implementar.
 * Esta interfaz no conoce nada sobre los drones administrados en el CRUD
 * ({@link co.edu.poli.sw2.model.Drone}) ni sobre su persistencia: solo
 * describe cómo se ejecuta una modalidad de control. La Abstracción del
 * puente, {@link ControlVuelo}, es quien asocia una implementación concreta
 * de este contrato con un dron en particular, en tiempo de ejecución.</p>
 *
 * <p>Implementaciones concretas: {@link ControlBasico} y
 * {@link ControlAutonomo}.</p>
 */
public interface ControlDrone {

    /**
     * Devuelve el nombre del tipo de control ("Control básico" o "Control autónomo").
     *
     * @return texto con el nombre del tipo de control.
     */
    String getTipoControl();

    /**
     * Ejecuta el protocolo o acción correspondiente a la modalidad de control.
     *
     * @return mensaje con la descripción de la acción ejecutada.
     */
    String ejecutarAccion();
}
