package co.edu.poli.sw2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa un dron administrado por la aplicacion, junto con el piloto
 * que lo opera y los sensores instalados en el.
 *
 * <p>Es la superclase de los tipos especializados de dron
 * ({@link co.edu.poli.sw2.model.Agricultura} y {@link co.edu.poli.sw2.model.Vigilancia}).</p>
 */
public class Drone {

    private String id;
    private String serial;
    private String modelo;
    private String fabricante;
    private double peso;
    private Piloto piloto;
    private List<Sensor> sensores;

    /**
     * Crea un dron sin datos, con la lista de sensores vacia.
     */
    public Drone() {
        this.sensores = new ArrayList<>();
    }

    /**
     * Crea un dron con sus datos basicos.
     *
     * @param id identificador unico asignado manualmente (no se genera automaticamente).
     * @param serial numero de serie del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kilogramos.
     */
    public Drone(String id, String serial, String modelo, String fabricante, double peso) {
        this();
        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    /**
     * Devuelve el identificador del dron.
     *
     * @return el identificador del dron.
     */
    public String getId() {
        return id;
    }

    /**
     * Asigna el identificador del dron.
     *
     * @param id identificador a asignar al dron.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Devuelve el numero de serie del dron.
     *
     * @return el numero de serie del dron.
     */
    public String getSerial() {
        return serial;
    }

    /**
     * Asigna el numero de serie del dron.
     *
     * @param serial numero de serie a asignar al dron.
     */
    public void setSerial(String serial) {
        this.serial = serial;
    }

    /**
     * Devuelve el modelo del dron.
     *
     * @return el modelo del dron.
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * Asigna el modelo del dron.
     *
     * @param modelo modelo a asignar al dron.
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * Devuelve el fabricante del dron.
     *
     * @return el fabricante del dron.
     */
    public String getFabricante() {
        return fabricante;
    }

    /**
     * Asigna el fabricante del dron.
     *
     * @param fabricante fabricante a asignar al dron.
     */
    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    /**
     * Devuelve el peso del dron.
     *
     * @return el peso del dron en kilogramos.
     */
    public double getPeso() {
        return peso;
    }

    /**
     * Asigna el peso del dron.
     *
     * @param peso peso en kilogramos a asignar al dron.
     */
    public void setPeso(double peso) {
        this.peso = peso;
    }

    /**
     * Devuelve el piloto asignado al dron.
     *
     * @return el piloto asignado al dron, o {@code null} si no tiene.
     */
    public Piloto getPiloto() {
        return piloto;
    }

    /**
     * Asigna el piloto del dron.
     *
     * @param piloto piloto a asignar al dron.
     */
    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    /**
     * Devuelve los sensores instalados en el dron.
     *
     * @return la lista de sensores instalados en el dron.
     */
    public List<Sensor> getSensores() {
        return sensores;
    }

    /**
     * Asigna los sensores instalados en el dron.
     *
     * @param sensores lista de sensores a asignar al dron.
     */
    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "id='" + id + '\'' +
                ", serial='" + serial + '\'' +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", piloto=" + piloto +
                ", sensores=" + sensores +
                '}';
    }
}
