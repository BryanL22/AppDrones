package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Piloto;
import co.edu.poli.sw2.model.Sensor;
import co.edu.poli.sw2.model.Vigilancia;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder unificado para la jerarquia {@link Drone} y sus subtipos
 * ({@link Agricultura} y {@link Vigilancia}).
 *
 * <h3>Problema que resuelve</h3>
 * <p>La clase base {@link Drone} y sus especializaciones comparten un
 * conjunto amplio de atributos comunes (id, serial, modelo, fabricante,
 * peso, piloto, sensores) y difieren unicamente en uno o dos campos
 * propios de cada subtipo. Construir estos objetos directamente con
 * {@code new} obliga al cliente a recordar el orden exacto de los
 * parametros, a pasar valores que no aplican al subtipo deseado y a
 * repetir la logica de seleccion de la subclase correcta. El patron
 * Builder encapsula esa complejidad: el cliente configura paso a paso
 * solo los atributos que necesita y delega en {@link #construir()} la
 * decision de que subclase instanciar.</p>
 *
 * <h3>Por que un unico Builder para todos los subtipos</h3>
 * <p>Dado que {@link Agricultura} y {@link Vigilancia} comparten la
 * gran mayoria de sus atributos con {@link Drone}, tener un Builder
 * por subtipo duplicaria practicamente todo el codigo. Un unico Builder
 * con metodos fluidos para los campos comunes y para cada campo
 * especifico resulta mas simple, coherente y facil de mantener. El
 * metodo {@link #construir()} determina automaticamente el subtipo a
 * partir de los campos especificos que el cliente haya configurado.</p>
 *
 * <h3>Por que esta en el paquete {@code services} y no en {@code model}</h3>
 * <p>El paquete {@code model} contiene exclusivamente las entidades de
 * dominio (POJOs puros). El Builder, en cambio, es logica de
 * construccion y seleccion de subtipo: una responsabilidad de servicio,
 * no de dominio. Ubicarlo en {@code services} respeta la separacion de
 * responsabilidades del proyecto: {@code model} define <em>que</em> es
 * un dron; {@code services} define <em>como</em> se construye.</p>
 *
 * <p>Adicionalmente, mantener el Builder fuera de {@code model} permite
 * cumplir la restriccion de no modificar ninguna clase del paquete
 * {@code model}.</p>
 */
public class DroneBuilder {

    // ── Atributos comunes de Drone ──────────────────────────────────

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private Double peso;
    private Piloto piloto;
    private List<Sensor> sensores;

    // ── Atributo especifico de Agricultura ──────────────────────────

    private Double capacidadTanque;
    private boolean capacidadTanqueConfigurada;

    // ── Atributo especifico de Vigilancia ───────────────────────────

    private Boolean deteccionTermica;
    private boolean deteccionTermicaConfigurada;

    // ── Metodos fluidos: atributos comunes ──────────────────────────

    /**
     * Asigna el identificador del dron.
     *
     * @param id identificador unico del dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder id(String id) {
        this.id = id;
        return this;
    }

    /**
     * Asigna el numero de serie del dron.
     *
     * @param serial numero de serie del dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder serial(String serial) {
        this.serial = serial;
        return this;
    }

    /**
     * Asigna el modelo del dron.
     *
     * @param modelo modelo del dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    /**
     * Asigna el fabricante del dron.
     *
     * @param fabricante fabricante del dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder fabricante(String fabricante) {
        this.fabricante = fabricante;
        return this;
    }

    /**
     * Asigna el peso del dron en kilogramos.
     *
     * @param peso peso del dron en kilogramos.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder peso(double peso) {
        this.peso = peso;
        return this;
    }

    /**
     * Asigna el piloto responsable de operar el dron.
     *
     * @param piloto piloto a asignar al dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder piloto(Piloto piloto) {
        this.piloto = piloto;
        return this;
    }

    /**
     * Asigna la lista de sensores instalados en el dron.
     *
     * @param sensores lista de sensores a asignar al dron.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder sensores(List<Sensor> sensores) {
        this.sensores = sensores;
        return this;
    }

    // ── Metodos fluidos: atributos especificos ──────────────────────

    /**
     * Asigna la capacidad del tanque de insumos.
     * <p>Al invocar este metodo, el builder interpreta que se desea
     * construir un {@link Agricultura}.</p>
     *
     * @param capacidadTanque capacidad del tanque en litros.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder capacidadTanque(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
        this.capacidadTanqueConfigurada = true;
        return this;
    }

    /**
     * Asigna si el dron cuenta con deteccion termica.
     * <p>Al invocar este metodo, el builder interpreta que se desea
     * construir un {@link Vigilancia}.</p>
     *
     * @param deteccionTermica {@code true} si el dron tiene deteccion
     *                         termica, {@code false} en caso contrario.
     * @return esta misma instancia del builder para encadenamiento.
     */
    public DroneBuilder deteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
        this.deteccionTermicaConfigurada = true;
        return this;
    }

    // ── Metodo de construccion ──────────────────────────────────────

    /**
     * Construye la instancia de {@link Drone} (o de su subtipo
     * correspondiente) a partir de los atributos configurados.
     *
     * <p>Logica de determinacion del subtipo:</p>
     * <ol>
     *     <li>Si no se configuro ningun atributo especifico de subtipo,
     *         se construye un {@link Drone} base.</li>
     *     <li>Si se configuro unicamente {@link #capacidadTanque(double)},
     *         se construye un {@link Agricultura}.</li>
     *     <li>Si se configuro unicamente {@link #deteccionTermica(boolean)},
     *         se construye un {@link Vigilancia}.</li>
     *     <li>Si se configuraron atributos de dos o mas subtipos a la
     *         vez, se lanza {@link IllegalStateException} indicando el
     *         conflicto.</li>
     * </ol>
     *
     * <p>Antes de construir, valida que los cinco atributos comunes
     * obligatorios ({@code id}, {@code serial}, {@code modelo},
     * {@code fabricante} y {@code peso}) esten presentes. Si falta
     * alguno, lanza {@link IllegalStateException}.</p>
     *
     * @return la instancia construida de {@link Drone}, {@link Agricultura}
     *         o {@link Vigilancia}.
     * @throws IllegalStateException si falta un atributo obligatorio o si
     *         se configuraron atributos de mas de un subtipo.
     */
    public Drone construir() {
        validarObligatorios();
        validarConflictoDeSubtipos();

        Drone drone;

        if (capacidadTanqueConfigurada) {
            drone = new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
        } else if (deteccionTermicaConfigurada) {
            drone = new Vigilancia(id, serial, modelo, fabricante, peso, deteccionTermica);
        } else {
            drone = new Drone(id, serial, modelo, fabricante, peso);
        }

        // Asignar atributos opcionales mediante los setters existentes.
        if (piloto != null) {
            drone.setPiloto(piloto);
        }
        if (sensores != null) {
            drone.setSensores(sensores);
        }

        return drone;
    }

    // ── Validaciones internas ───────────────────────────────────────

    private void validarObligatorios() {
        List<String> faltantes = new ArrayList<>();

        if (id == null || id.isBlank()) {
            faltantes.add("id");
        }
        if (serial == null || serial.isBlank()) {
            faltantes.add("serial");
        }
        if (modelo == null || modelo.isBlank()) {
            faltantes.add("modelo");
        }
        if (fabricante == null || fabricante.isBlank()) {
            faltantes.add("fabricante");
        }
        if (peso == null) {
            faltantes.add("peso");
        }

        if (!faltantes.isEmpty()) {
            throw new IllegalStateException(
                    "No se puede construir el Drone: faltan los siguientes atributos obligatorios: "
                            + String.join(", ", faltantes) + ".");
        }
    }

    private void validarConflictoDeSubtipos() {
        if (capacidadTanqueConfigurada && deteccionTermicaConfigurada) {
            throw new IllegalStateException(
                    "Conflicto de subtipos: se configuraron atributos de Agricultura "
                            + "(capacidadTanque) y de Vigilancia (deteccionTermica) al mismo tiempo. "
                            + "Un dron solo puede pertenecer a un subtipo.");
        }
    }
}
