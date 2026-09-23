package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;

import java.util.List;

/**
 * Fachada (Facade) del subsistema de factorias.
 *
 * <p>Crear un dron con el Factory Method obliga al cliente a conocer tres
 * detalles: que factoria concreta corresponde a cada tipo, que cada una se
 * configura por un constructor distinto ({@link AgriculturaFactory} recibe la
 * capacidad del tanque y {@link VigilanciaFactory} la deteccion termica), y
 * que solo despues de instanciarla se le puede pedir el dron con
 * {@code crear(...)}. Esta clase concentra esos pasos en una sola llamada.</p>
 *
 * <p>No reemplaza al Factory Method: {@link DroneFactory} y sus dos
 * subclases quedan intactas y siguen haciendo el trabajo. La fachada solo se
 * pone delante para que el controlador no tenga que conocerlas.</p>
 */
public class FabricaDrones {

    /**
     * Tipo de dron especializado en agricultura.
     */
    public static final String TIPO_AGRICULTURA = DroneFactory.TIPO_AGRICULTURA;

    /**
     * Tipo de dron especializado en vigilancia.
     */
    public static final String TIPO_VIGILANCIA = DroneFactory.TIPO_VIGILANCIA;

    /**
     * Devuelve los tipos de dron que el subsistema puede construir, para
     * llenar el selector de la vista.
     *
     * @return lista con los tipos disponibles.
     */
    public List<String> tiposDisponibles() {
        return DroneFactory.tiposDisponibles();
    }

    /**
     * Crea el dron del tipo indicado eligiendo la factoria concreta y
     * configurandola con el dato que corresponda. El parametro de la
     * especializacion que no aplique al tipo se ignora.
     *
     * @param tipo tipo de dron, {@link #TIPO_AGRICULTURA} o {@link #TIPO_VIGILANCIA}.
     * @param id identificador del dron.
     * @param serial serial del dron.
     * @param modelo modelo del dron.
     * @param fabricante fabricante del dron.
     * @param peso peso del dron en kilogramos.
     * @param capacidadTanque capacidad del tanque, solo para agricultura.
     * @param deteccionTermica si lleva deteccion termica, solo para vigilancia.
     * @return el dron ya construido por la factoria correspondiente.
     */
    public Drone crear(String tipo, String id, String serial, String modelo, String fabricante,
            double peso, double capacidadTanque, boolean deteccionTermica) {
        if (TIPO_AGRICULTURA.equals(tipo)) {
            return new AgriculturaFactory(capacidadTanque).crear(id, serial, modelo, fabricante, peso);
        }

        return new VigilanciaFactory(deteccionTermica).crear(id, serial, modelo, fabricante, peso);
    }
}
