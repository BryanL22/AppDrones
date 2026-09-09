package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

/**
 * Servicio (patron Prototype) encargado de obtener una copia de un
 * {@link Drone} existente.
 *
 * <p>Delega el clonado en el propio objeto (via {@link Drone#clone()}, que
 * cada subclase sobreescribe para copiarse a si misma), en vez de inspeccionar
 * su tipo concreto y volver a construirlo con {@code new}. Asi, el resto de
 * la aplicacion (por ejemplo, el controlador de la vista) no necesita conocer
 * si el dron es {@link co.edu.poli.sw2.model.Agricultura},
 * {@link co.edu.poli.sw2.model.Vigilancia} o un {@link Drone} sin
 * especializacion: siempre obtiene una copia del mismo tipo concreto que el
 * original, con sus mismos datos pero con una identidad (referencia de
 * memoria) distinta.</p>
 */
public final class DronePrototype {

    private DronePrototype() {
    }

    /**
     * Obtiene una copia independiente del dron recibido.
     *
     * @param original dron a clonar.
     * @return una copia de {@code original}, del mismo tipo concreto, cuya
     *         referencia de memoria es distinta a la del original.
     */
    public static Drone clonar(Drone original) {
        return original.clone();
    }
}
