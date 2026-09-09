package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Piloto;
import co.edu.poli.sw2.model.Sensor;
import co.edu.poli.sw2.model.Vigilancia;

import java.util.List;

/**
 * Builder (patron Builder) para construir un {@link Drone} paso a paso.
 *
 * <p>Un unico builder cubre toda la jerarquia: se van encadenando los
 * atributos comunes de {@link Drone} y, opcionalmente, los propios de una
 * unica especializacion ({@link Agricultura} o {@link Vigilancia}). El unico
 * atributo obligatorio es {@code id}; el resto es opcional y puede omitirse
 * (por ejemplo, un dron solo con {@code id} y {@code peso}, o solo con
 * {@code id} y el atributo de una especializacion). {@link #construir()}
 * decide, segun que atributos especificos se hayan establecido, que clase
 * concreta instanciar:</p>
 * <ul>
 *     <li>Ninguno especifico establecido: construye un {@link Drone}.</li>
 *     <li>Solo los de Agricultura ({@code capacidadTanque}): construye una
 *     {@link Agricultura}.</li>
 *     <li>Solo los de Vigilancia ({@code deteccionTermica}): construye una
 *     {@link Vigilancia}.</li>
 *     <li>Los de ambas especializaciones a la vez: lanza
 *     {@link IllegalStateException}, ya que un mismo dron no puede ser las
 *     dos cosas.</li>
 * </ul>
 *
 * <p>Solo usa los constructores (sin argumentos) y setters publicos que ya
 * existen en {@code model}; no depende de ningun cambio en ese paquete.</p>
 */
public class DroneBuilder {

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private Double peso;
    private Piloto piloto;
    private List<Sensor> sensores;

    private Double capacidadTanque;
    private Boolean deteccionTermica;

    /**
     * Establece el identificador del dron.
     *
     * @param id identificador a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Establece el numero de serie del dron.
     *
     * @param serial serial a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder serial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Establece el modelo del dron.
     *
     * @param modelo modelo a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    /**
     * Establece el fabricante del dron.
     *
     * @param fabricante fabricante a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder fabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    /**
     * Establece el peso del dron en kilogramos.
     *
     * @param peso peso a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder peso(double peso) {
        this.peso = peso;
        return this;
    }

    /**
     * Establece el piloto asignado al dron.
     *
     * @param piloto piloto a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder piloto(Piloto piloto) {
        this.piloto = piloto;
        return this;
    }

    /**
     * Establece los sensores instalados en el dron.
     *
     * @param sensores lista de sensores a asignar.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder sensores(List<Sensor> sensores) {
        this.sensores = sensores;
        return this;
    }

    /**
     * Establece la capacidad del tanque (atributo especifico de
     * {@link Agricultura}).
     *
     * @param capacidadTanque capacidad del tanque en litros.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder capacidadTanque(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
        return this;
    }

    /**
     * Establece si el dron tiene deteccion termica (atributo especifico de
     * {@link Vigilancia}).
     *
     * @param deteccionTermica si el dron tiene deteccion termica.
     * @return este builder, para encadenar mas llamadas.
     */
    public DroneBuilder deteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
        return this;
    }

    /**
     * Construye el dron con los atributos establecidos hasta el momento.
     * Instancia con el constructor sin argumentos (de {@link Drone},
     * {@link Agricultura} o {@link Vigilancia}, segun corresponda) y aplica
     * un setter por cada atributo que se haya establecido; los que no se
     * establecieron simplemente quedan con su valor por defecto.
     *
     * @return un {@link Drone}, {@link Agricultura} o {@link Vigilancia},
     *         segun los atributos especificos que se hayan establecido.
     * @throws IllegalStateException si falta el atributo obligatorio
     *         ({@code id}), o si se establecieron atributos especificos de
     *         mas de una especializacion a la vez.
     */
    public final Drone construir() {
        if (esVacio(id)) {
            throw new IllegalStateException("Falta el atributo obligatorio del drone: id.");
        }

        boolean tieneAgricultura = capacidadTanque != null;
        boolean tieneVigilancia = deteccionTermica != null;

        if (tieneAgricultura && tieneVigilancia) {
            throw new IllegalStateException("Conflicto al construir el drone: se establecieron atributos de "
                    + "Agricultura (capacidadTanque) y de Vigilancia (deteccionTermica) al mismo tiempo; "
                    + "un drone solo puede tener una especializacion.");
        }

        Drone drone;
        if (tieneAgricultura) {
            Agricultura agricultura = new Agricultura();
            agricultura.setCapacidadTanque(capacidadTanque);
            drone = agricultura;
        } else if (tieneVigilancia) {
            Vigilancia vigilancia = new Vigilancia();
            vigilancia.setDeteccionTermica(deteccionTermica);
            drone = vigilancia;
        } else {
            drone = new Drone();
        }

        drone.setId(id);
        if (serial != null) {
            drone.setSerial(serial);
        }
        if (modelo != null) {
            drone.setModelo(modelo);
        }
        if (fabricante != null) {
            drone.setFabricante(fabricante);
        }
        if (peso != null) {
            drone.setPeso(peso);
        }
        if (piloto != null) {
            drone.setPiloto(piloto);
        }
        if (sensores != null) {
            drone.setSensores(sensores);
        }

        return drone;
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }
}
