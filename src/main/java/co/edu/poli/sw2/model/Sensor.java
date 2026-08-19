package co.edu.poli.sw2.model;

/**
 * Representa un sensor instalado en un dron (por ejemplo, camara, GPS o LIDAR).
 */
public class Sensor {

    private String id;
    private String tipo;
    private String fabricante;

    public Sensor() {
    }

    public Sensor(String id, String tipo, String fabricante) {
        this.id = id;
        this.tipo = tipo;
        this.fabricante = fabricante;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
                "id='" + id + '\'' +
                ", tipo='" + tipo + '\'' +
                ", fabricante='" + fabricante + '\'' +
                '}';
    }
}
