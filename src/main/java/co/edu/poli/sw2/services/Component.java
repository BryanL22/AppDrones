package co.edu.poli.sw2.services;

/**
 * Interfaz comun (Component) del patron Composite.
 *
 * <p>Declara la unica operacion que comparten un sensor individual
 * ({@link WrapperSensor}) y un grupo de sensores ({@link Composite}), de modo
 * que el controlador pueda tratarlos igual sin saber con cual esta hablando.</p>
 */
public interface Component {

    /**
     * Devuelve la lectura del elemento: la del propio sensor si es individual,
     * o la de todos sus hijos si es un grupo.
     *
     * @return texto con la lectura.
     */
    String leer();
}
