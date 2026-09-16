package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Mision;

import java.io.IOException;

/**
 * Interfaz con el cliente (Target) del patrón Adapter.
 *
 * <p>Declara el único protocolo que el código cliente conoce para exportar
 * una misión: entregar la {@link Mision} y olvidarse del formato y del
 * medio en el que termina. Gracias a esta interfaz el cliente no se acopla
 * a {@link MisionJsonAdapter} ni a {@link ArchivoJson}, y mañana se pueden
 * agregar exportadores a otros formatos sin tocar la vista.</p>
 *
 * <p>Implementación disponible: {@link MisionJsonAdapter}.</p>
 */
public interface ExportadorMision {

    /**
     * Exporta la misión recibida al formato y destino que decida la
     * implementación.
     *
     * @param mision misión que se desea exportar.
     * @throws IOException si el destino no se puede escribir.
     */
    void exportar(Mision mision) throws IOException;
}
