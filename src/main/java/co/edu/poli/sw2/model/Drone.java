package co.edu.poli.sw2.model;

import java.util.ArrayList;
import java.util.List;

public class Drone {

    private int idDrone;
    private String serial;
    private String modelo;
    private String fabricante;
    private double peso;
    private List<Piloto> pilotos;
    private List<Sensor> sensores;
    private List<Mision> misiones;

    public Drone() {
        this.pilotos = new ArrayList<>();
        this.sensores = new ArrayList<>();
        this.misiones = new ArrayList<>();
    }

    public Drone(int idDrone, String serial, String modelo, String fabricante, double peso) {
        this();
        this.idDrone = idDrone;
        this.serial = serial;
        this.modelo = modelo;
        this.fabricante = fabricante;
        this.peso = peso;
    }

    public int getIdDrone() {
        return idDrone;
    }

    public void setIdDrone(int idDrone) {
        this.idDrone = idDrone;
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

    public List<Piloto> getPilotos() {
        return pilotos;
    }

    public void setPilotos(List<Piloto> pilotos) {
        this.pilotos = pilotos;
    }

    public List<Sensor> getSensores() {
        return sensores;
    }

    public void setSensores(List<Sensor> sensores) {
        this.sensores = sensores;
    }

    public List<Mision> getMisiones() {
        return misiones;
    }

    public void setMisiones(List<Mision> misiones) {
        this.misiones = misiones;
    }

    @Override
    public String toString() {
        return "Drone{" +
                "idDrone=" + idDrone +
                ", serial='" + serial + '\'' +
                ", modelo='" + modelo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                ", peso=" + peso +
                ", pilotos=" + pilotos +
                ", sensores=" + sensores +
                ", misiones=" + misiones +
                '}';
    }
}
