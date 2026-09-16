package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Mision;

import java.io.IOException;
import java.util.List;

/**
 * Adaptador (Adapter) que permite exportar una {@link Mision} a un archivo
 * JSON usando un servicio que solo sabe escribir texto.
 *
 * <p>Implementa la interfaz que el cliente conoce
 * ({@link ExportadorMision}) y envuelve una instancia de {@link ArchivoJson}.
 * Cuando el cliente pide exportar, el adaptador recorre la misión, arma un
 * String con formato JSON y se lo entrega al servicio, que es quien crea el
 * archivo. Así el cliente nunca habla con {@link ArchivoJson} y
 * {@link ArchivoJson} nunca conoce la clase {@link Mision}.</p>
 */
public class MisionJsonAdapter implements ExportadorMision {

    private final ArchivoJson adaptee;

    /**
     * Crea el adaptador sobre el servicio de archivos indicado.
     *
     * @param adaptee servicio que escribirá el archivo.
     */
    public MisionJsonAdapter(ArchivoJson adaptee) {
        this.adaptee = adaptee;
    }

    /**
     * Convierte la misión en un String con formato JSON y le pide al
     * servicio adaptado que lo guarde.
     *
     * @param mision misión que se desea exportar.
     * @throws IOException si el archivo no se puede crear o escribir.
     */
    @Override
    public void exportar(Mision mision) throws IOException {
        adaptee.escribir(convertirAJson(mision));
    }

    /**
     * Traduce la misión y los drones que participan en ella al formato que
     * espera el servicio: un único String con notación JSON.
     *
     * @param mision misión a convertir.
     * @return el texto JSON que representa la misión.
     */
    private String convertirAJson(Mision mision) {
        StringBuilder json = new StringBuilder();
        json.append("{\n");
        json.append("  \"id\": ").append(literal(mision.getId())).append(",\n");
        json.append("  \"nombre\": ").append(literal(mision.getNombre())).append(",\n");
        json.append("  \"ubicacion\": ").append(literal(mision.getUbicacion())).append(",\n");
        json.append("  \"fecha\": ").append(literal(mision.getFecha())).append(",\n");
        json.append("  \"drones\": [");

        List<Drone> drones = mision.getDrones();
        for (int i = 0; i < drones.size(); i++) {
            Drone drone = drones.get(i);
            json.append(i == 0 ? "\n" : ",\n");
            json.append("    {\n");
            json.append("      \"id\": ").append(literal(drone.getId())).append(",\n");
            json.append("      \"serial\": ").append(literal(drone.getSerial())).append(",\n");
            json.append("      \"modelo\": ").append(literal(drone.getModelo())).append(",\n");
            json.append("      \"fabricante\": ").append(literal(drone.getFabricante())).append(",\n");
            json.append("      \"peso\": ").append(drone.getPeso()).append("\n");
            json.append("    }");
        }

        if (!drones.isEmpty()) {
            json.append("\n  ");
        }

        json.append("]\n");
        json.append("}\n");
        return json.toString();
    }

    /**
     * Devuelve el valor como literal JSON: entre comillas y con los
     * caracteres especiales escapados, o {@code null} si no tiene valor.
     *
     * @param valor texto a convertir.
     * @return el literal JSON correspondiente.
     */
    private String literal(String valor) {
        if (valor == null) {
            return "null";
        }

        StringBuilder escapado = new StringBuilder("\"");
        for (char caracter : valor.toCharArray()) {
            switch (caracter) {
                case '"' -> escapado.append("\\\"");
                case '\\' -> escapado.append("\\\\");
                case '\n' -> escapado.append("\\n");
                case '\r' -> escapado.append("\\r");
                case '\t' -> escapado.append("\\t");
                default -> escapado.append(caracter);
            }
        }

        return escapado.append('"').toString();
    }
}
