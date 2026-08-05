package co.edu.poli.sw2.model;

import java.time.LocalDate;

public class Mision {

    private int idMision;
    private String nombre;
    private String ubicacion;
    private LocalDate fecha;

    public Mision() {
    }

    public Mision(int idMision, String nombre, String ubicacion, LocalDate fecha) {
        this.idMision = idMision;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.fecha = fecha;
    }

    public int getIdMision() {
        return idMision;
    }

    public void setIdMision(int idMision) {
        this.idMision = idMision;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    @Override
    public String toString() {
        return "Mision{" +
                "idMision=" + idMision +
                ", nombre='" + nombre + '\'' +
                ", ubicacion='" + ubicacion + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
