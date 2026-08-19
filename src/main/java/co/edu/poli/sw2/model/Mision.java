package co.edu.poli.sw2.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa una mision de vuelo en la que participan uno o mas drones.
 */
public class Mision {

    private String id;
    private String nombre;
    private String ubicacion;
    private String fecha;
    private List<Drone> drones;

    public Mision() {
        this.drones = new ArrayList<>();
    }

    public Mision(String id, String nombre, String ubicacion, String fecha) {
        this();
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public List<Drone> getDrones() {
        return drones;
    }

    public void setDrones(List<Drone> drones) {
        this.drones = drones;
    }

    @Override
    public String toString() {
        return "Mision{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha='" + fecha + '\'' +
                ", drones=" + drones +
                '}';
    }
}
