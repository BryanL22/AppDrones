package co.edu.poli.sw2.services;

import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Mision;
import co.edu.poli.sw2.model.Vigilancia;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para verificar la implementación del patrón de diseño
 * Adapter: {@link ExportadorMision} (interfaz con el cliente),
 * {@link ArchivoJson} (servicio adaptado) y {@link MisionJsonAdapter}, que
 * traduce una {@link Mision} al String con formato JSON que el servicio
 * espera.
 */
class MisionJsonAdapterTest {

    @TempDir
    Path carpeta;

    private Mision misionConDrones() {
        Mision mision = new Mision("M001", "Inspección de cultivos",
                "Finca La Esperanza, Rionegro", "2026-09-16");
        mision.setDrones(List.of(
                new Agricultura("D001", "SN-AGR-1145", "AgroWing X2", "DJI", 12.4, 18.0),
                new Vigilancia("D002", "SN-VIG-0932", "SkyGuard 500", "Parrot", 8.1, true)));
        return mision;
    }

    private String exportar(Mision mision, Path destino) throws IOException {
        ExportadorMision exportador = new MisionJsonAdapter(new ArchivoJson(destino.toString()));
        exportador.exportar(mision);
        return Files.readString(destino, StandardCharsets.UTF_8);
    }

    @Test
    void creaElArchivoConLosDatosDeLaMision() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");

        String json = exportar(misionConDrones(), destino);

        assertTrue(Files.exists(destino));
        assertTrue(json.contains("\"id\": \"M001\""));
        assertTrue(json.contains("\"nombre\": \"Inspección de cultivos\""));
        assertTrue(json.contains("\"ubicacion\": \"Finca La Esperanza, Rionegro\""));
        assertTrue(json.contains("\"fecha\": \"2026-09-16\""));
    }

    @Test
    void incluyeLosDronesDeLaMision() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");

        String json = exportar(misionConDrones(), destino);

        assertTrue(json.contains("\"serial\": \"SN-AGR-1145\""));
        assertTrue(json.contains("\"modelo\": \"AgroWing X2\""));
        assertTrue(json.contains("\"serial\": \"SN-VIG-0932\""));
        assertTrue(json.contains("\"peso\": 8.1"));
    }

    @Test
    void creaLasCarpetasIntermedias() throws IOException {
        Path destino = carpeta.resolve("misiones").resolve("mision-M001.json");

        exportar(misionConDrones(), destino);

        assertTrue(Files.exists(destino));
    }

    @Test
    void escapaLasComillasDeLosValores() throws IOException {
        Mision mision = new Mision("M002", "Vuelo \"nocturno\"", "Hangar 3", "2026-09-20");
        Path destino = carpeta.resolve("mision-M002.json");

        String json = exportar(mision, destino);

        assertTrue(json.contains("\"nombre\": \"Vuelo \\\"nocturno\\\"\""));
    }

    @Test
    void escribeUnArregloVacioCuandoLaMisionNoTieneDrones() throws IOException {
        Mision mision = new Mision("M003", "Reconocimiento", "Guatapé", "2026-09-21");
        Path destino = carpeta.resolve("mision-M003.json");

        String json = exportar(mision, destino);

        assertTrue(json.contains("\"drones\": []"));
    }

    @Test
    void laRutaAbsolutaApuntaAlArchivoCreado() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");
        ArchivoJson archivo = new ArchivoJson(destino.toString());

        new MisionJsonAdapter(archivo).exportar(misionConDrones());

        assertEquals(destino.toAbsolutePath().toString(), archivo.getRutaAbsoluta());
    }
}
