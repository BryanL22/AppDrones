package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Contrato del patrón Bridge para la gestión de controles de vuelo.
 *
 * <p>Declara las operaciones que cualquier modalidad de control (básico,
 * autónomo, o cualquier otra que se agregue en el futuro) debe implementar.
 * Se relaciona directamente con {@link Drone} mediante una relación de
 * dependencia (en UML: relación "usa"), recibiéndolo como parámetro en
 * {@link #ejecutarAccion(Drone)}, sin que ninguna de las dos clases guarde
 * referencias persistentes de la otra.</p>
 *
 * <p>Implementaciones concretas: {@link ControlBasico} y {@link ControlAutonomo}.</p>
 */
public interface ControlDrone {

    /**
     * Devuelve el nombre del tipo de control ("Control básico" o "Control autónomo").
     *
     * @return texto con el nombre del tipo de control.
     */
    String getTipoControl();

    /**
     * Ejecuta el protocolo o acción correspondiente a la modalidad de control
     * sobre el dron suministrado.
     *
     * @param drone el dron sobre el cual se ejecuta la acción (puede ser null).
     * @return mensaje con la descripción de la acción ejecutada.
     */
    String ejecutarAccion(Drone drone);

    /**
     * Devuelve el mensaje breve que debe mostrarse al usuario: únicamente
     * "Dron con control autónomo" o "Dron con control básico".
     *
     * @return el mensaje breve correspondiente al tipo de control.
     */
    default String descripcionBreve() {
        return "Dron con " + getTipoControl().toLowerCase();
    }

    /**
     * Factoría utilitaria que construye la implementación de control
     * adecuada según el tipo indicado.
     *
     * @param tipoControl "Control básico" o "Control autónomo".
     * @return instancia concreta de {@link ControlDrone}.
     */
    static ControlDrone crear(String tipoControl) {
        if (ControlAutonomo.NOMBRE.equalsIgnoreCase(tipoControl) || "Control autonomo".equalsIgnoreCase(tipoControl)) {
            return new ControlAutonomo();
        }
        return new ControlBasico();
    }
}
