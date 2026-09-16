package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Sensor;

/**
 * Elemento individual (Leaf) del patron Composite.
 *
 * <p>Envuelve un {@link Sensor} del modelo para que pueda usarse como
 * {@link Component}. Es una hoja: no tiene hijos, asi que resuelve la lectura
 * por si mismo con los datos del sensor que envuelve. El modelo no se modifica;
 * esta clase solo lo lee.</p>
 */
public class WrapperSensor implements Component {

    Sensor sensor;

    /**
     * Envuelve el sensor recibido.
     *
     * @param sensor sensor del modelo que representa esta hoja.
     */
    public WrapperSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    /**
     * Devuelve la lectura del sensor envuelto.
     *
     * @return texto con el identificador, el tipo y el fabricante del sensor.
     */
    @Override
    public String leer() {
        return sensor.getId() + " - " + sensor.getTipo() + " (" + sensor.getFabricante() + ")";
    }
}
