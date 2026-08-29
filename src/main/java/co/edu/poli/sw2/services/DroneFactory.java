package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;

import java.util.List;

/**
 * Plantilla reutilizable (patron Factory Method) para crear un {@link Drone}.
 *
 * <p>Define el paso comun a toda creacion (recibir los datos base del dron)
 * y delega en cada subclase concreta ({@link AgriculturaFactory},
 * {@link VigilanciaFactory}) la decision de que especializacion instanciar y
 * con que datos propios. Asi, agregar un nuevo tipo de dron en el futuro solo
 * implica escribir una nueva subclase de {@link DroneFactory}, sin tocar esta
 * clase ni el resto de fabricas existentes.</p>
 */
public abstract class DroneFactory {

    /** Texto que identifica el tipo de especializacion Agricultura. */
    public static final String TIPO_AGRICULTURA = "Agricultura";

    /** Texto que identifica el tipo de especializacion Vigilancia. */
    public static final String TIPO_VIGILANCIA = "Vigilancia";

    /**
     * Devuelve los tipos de especializacion disponibles, en el orden en que
     * deben ofrecerse en la interfaz.
     *
     * @return lista con {@link #TIPO_AGRICULTURA} y {@link #TIPO_VIGILANCIA}.
     */
    public static List<String> tiposDisponibles() {
        return List.of(TIPO_AGRICULTURA, TIPO_VIGILANCIA);
    }

    /**
     * Crea el dron con los datos base recibidos, delegando en
     * {@link #crearEspecializado} la construccion de la especializacion
     * concreta que sabe fabricar esta instancia.
     *
     * @param id identificador asignado manualmente por el usuario.
     * @param serial serial del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kg.
     * @return la instancia de {@link Drone} construida.
     */
    public final Drone crear(String id, String serial, String modelo, String fabricante, double peso) {
        return crearEspecializado(id, serial, modelo, fabricante, peso);
    }

    /**
     * Construye la especializacion concreta que le corresponde a esta
     * fabrica, usando los datos propios que haya recibido en su constructor
     * (por ejemplo, la capacidad del tanque o si tiene deteccion termica).
     *
     * @param id identificador asignado manualmente por el usuario.
     * @param serial serial del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kg.
     * @return la instancia de {@link Agricultura}, {@link Vigilancia} u otra
     *         especializacion que corresponda.
     */
    protected abstract Drone crearEspecializado(String id, String serial, String modelo, String fabricante,
                                                 double peso);
}
