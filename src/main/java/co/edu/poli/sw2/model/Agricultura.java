package co.edu.poli.sw2.model;

/**
 * Dron especializado en labores agricolas, con un tanque para insumos.
 */
public class Agricultura extends Drone {

    private double capacidadTanque;

    public Agricultura() {
        super();
    }

    public Agricultura(String id, String serial, String modelo, String fabricante, double peso,
                        double capacidadTanque) {
        super(id, serial, modelo, fabricante, peso);
        this.capacidadTanque = capacidadTanque;
    }

    public double getCapacidadTanque() {
        return capacidadTanque;
    }

    public void setCapacidadTanque(double capacidadTanque) {
        this.capacidadTanque = capacidadTanque;
    }

    @Override
    public String toString() {
        return "Agricultura{" +
                "capacidadTanque=" + capacidadTanque +
                "} " + super.toString();
    }
}
