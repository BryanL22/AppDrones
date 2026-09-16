package co.edu.poli.sw2.services;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Servicio adaptado (Adaptee) del patrón Adapter: crea el archivo
 * {@code .json} en disco.
 *
 * <p>Su interfaz es incompatible con la del cliente a propósito: solo sabe
 * recibir texto ya formateado y guardarlo, y no conoce
 * {@link co.edu.poli.sw2.model.Mision} ni ninguna otra clase del modelo.
 * Es {@link MisionJsonAdapter} quien traduce la misión al texto que esta
 * clase espera.</p>
 */
public class ArchivoJson {

    private final String ruta;

    /**
     * Crea el servicio apuntando al archivo indicado.
     *
     * @param ruta ruta del archivo a escribir, por ejemplo
     *             {@code misiones/mision-M001.json}.
     */
    public ArchivoJson(String ruta) {
        this.ruta = ruta;
    }

    /**
     * Escribe el contenido recibido en el archivo, creando las carpetas
     * intermedias si hacen falta y reemplazando el archivo si ya existía.
     *
     * @param contenido texto a guardar, ya con formato JSON.
     * @throws IOException si el archivo no se puede crear o escribir.
     */
    public void escribir(String contenido) throws IOException {
        Path destino = Path.of(ruta);
        Path carpeta = destino.getParent();
        if (carpeta != null) {
            Files.createDirectories(carpeta);
        }

        Files.writeString(destino, contenido, StandardCharsets.UTF_8);
    }

    /**
     * Devuelve la ruta absoluta del archivo, para poder mostrarle al usuario
     * dónde quedó guardado.
     *
     * @return la ruta absoluta del archivo.
     */
    public String getRutaAbsoluta() {
        return Path.of(ruta).toAbsolutePath().toString();
    }
}
