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

    public Drone() {
        this.sensores = new ArrayList<>();
    }

    public Drone(String id, String serial, String modelo, String fabricante, double peso) {
        this();
        this.id = id;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial = serial;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public Piloto getPiloto() {
        return piloto;
    }

    public void setPiloto(Piloto piloto) {
        this.piloto = piloto;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

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
