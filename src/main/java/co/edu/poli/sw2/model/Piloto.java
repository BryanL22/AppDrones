package co.edu.poli.sw2.model;

/**
 * Representa al piloto responsable de operar un dron.
 */
public class Piloto {

    private String id;
    private String nombre;
    private String licencia;
    private String telefono;

    public Piloto() {
    }

    public Piloto(String id, String nombre, String licencia, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.licencia = licencia;
        this.telefono = telefono;
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

    public String getLicencia() {
        return licencia;
    }

    public void setLicencia(String licencia) {
        this.licencia = licencia;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public String toString() {
        return "Piloto{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", licencia='" + licencia + '\'' +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
