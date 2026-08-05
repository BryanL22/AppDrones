package co.edu.poli.sw2.model;

public class Sensor {

    private int idSensor;
    private String tipo;
    private String fabricante;

    public Sensor() {
    }

    public Sensor(int idSensor, String tipo, String fabricante) {
        this.idSensor = idSensor;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    public int getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(int idSensor) {
        this.idSensor = idSensor;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "idSensor=" + idSensor +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }
}
