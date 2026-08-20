package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;

import java.util.List;

/**
 * Fabrica (patron Factory) encargada de crear la instancia concreta de
 * {@link Drone} que corresponde a un tipo de especializacion.
 *
 * <p>Centraliza en un unico lugar la decision de que subclase instanciar
 * ({@link Agricultura} o {@link Vigilancia}) a partir de un texto de tipo,
 * para que el resto de la aplicacion (por ejemplo, el controlador de la
 * vista) no tenga que conocer ni repetir esa logica de seleccion ni llamar
 * directamente a los constructores de cada subclase.</p>
 */
public final class DroneFactory {

    /** Texto que identifica el tipo de especializacion Agricultura. */
    public static final String TIPO_AGRICULTURA = "Agricultura";

    /** Texto que identifica el tipo de especializacion Vigilancia. */
    public static final String TIPO_VIGILANCIA = "Vigilancia";

    private DroneFactory() {
    }

    /**
     * Devuelve los tipos de especializacion que esta fabrica sabe construir,
     * en el orden en que deben ofrecerse en la interfaz.
     *
     * @return lista con {@link #TIPO_AGRICULTURA} y {@link #TIPO_VIGILANCIA}.
     */
    public static List<String> tiposDisponibles() {
        return List.of(TIPO_AGRICULTURA, TIPO_VIGILANCIA);
    }

    /**
     * Crea la instancia de {@link Drone} que corresponde al tipo indicado.
     *
     * @param tipo {@link #TIPO_AGRICULTURA} o {@link #TIPO_VIGILANCIA}.
     * @param id identificador asignado manualmente por el usuario.
     * @param serial serial del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kg.
     * @param capacidadTanque capacidad del tanque en litros; solo se usa si {@code tipo} es {@link #TIPO_AGRICULTURA}.
     * @param deteccionTermica si tiene deteccion termica; solo se usa si {@code tipo} es {@link #TIPO_VIGILANCIA}.
     * @return la instancia de {@link Agricultura} o {@link Vigilancia} construida.
     * @throws IllegalArgumentException si {@code tipo} no es ninguno de los tipos soportados.
     */
    public static Drone crear(String tipo, String id, String serial, String modelo, String fabricante, double peso,
                               double capacidadTanque, boolean deteccionTermica) {
        if (TIPO_AGRICULTURA.equals(tipo)) {
            return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
        }
        if (TIPO_VIGILANCIA.equals(tipo)) {
            return new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
        }
        throw new IllegalArgumentException("Tipo de drone desconocido: " + tipo);
    }
}
