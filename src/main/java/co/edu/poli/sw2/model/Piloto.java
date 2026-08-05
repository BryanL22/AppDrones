package co.edu.poli.sw2.model;

public class Piloto {

    private int idPiloto;
    private String nombre;
    private int experiencia;
    private String telefono;

    public Piloto() {
    }

    public Piloto(int idPiloto, String nombre, int experiencia, String telefono) {
        this.idPiloto = idPiloto;
        this.nombre = nombre;
        this.experiencia = experiencia;
        this.telefono = telefono;
    }

    public int getIdPiloto() {
        return idPiloto;
    }

    public void setIdPiloto(int idPiloto) {
        this.idPiloto = idPiloto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
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
                "idPiloto=" + idPiloto +
                ", nombre='" + nombre + '\'' +
                ", experiencia=" + experiencia +
                ", telefono='" + telefono + '\'' +
                '}';
    }
}
