package co.edu.poli.sw2.model;

/**
 * Dron especializado en labores de vigilancia, con deteccion termica opcional.
 */
public class Vigilancia extends Drone {

    private boolean deteccionTermica;

    public Vigilancia() {
        super();
    }

    public Vigilancia(String id, String serial, String modelo, String fabricante, double peso,
                       boolean deteccionTermica) {
        super(id, serial, modelo, fabricante, peso);
        this.deteccionTermica = deteccionTermica;
    }

    public boolean isDeteccionTermica() {
        return deteccionTermica;
    }

    public void setDeteccionTermica(boolean deteccionTermica) {
        this.deteccionTermica = deteccionTermica;
    }

    @Override
    public String toString() {
        return "Vigilancia{" +
                "deteccionTermica=" + deteccionTermica +
                "} " + super.toString();
    }
}
