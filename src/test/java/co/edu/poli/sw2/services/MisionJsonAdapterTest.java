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
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    // ------------------------------------------------------------------
    // Formato del JSON generado
    // ------------------------------------------------------------------

    @Test
    void generaElJsonCompletoConLaIndentacionEsperada() throws IOException {
        Mision mision = new Mision("M010", "Prueba", "Bogotá", "2026-09-19");
        mision.setDrones(List.of(new Agricultura("D010", "SN-010", "ModeloX", "FabricanteX", 2.5, 10.0)));
        Path destino = carpeta.resolve("mision-M010.json");

        String json = exportar(mision, destino);

        assertEquals(String.join("\n",
                "{",
                "  \"id\": \"M010\",",
                "  \"nombre\": \"Prueba\",",
                "  \"ubicacion\": \"Bogotá\",",
                "  \"fecha\": \"2026-09-19\",",
                "  \"drones\": [",
                "    {",
                "      \"id\": \"D010\",",
                "      \"serial\": \"SN-010\",",
                "      \"modelo\": \"ModeloX\",",
                "      \"fabricante\": \"FabricanteX\",",
                "      \"peso\": 2.5",
                "    }",
                "  ]",
                "}") + "\n", json);
    }

    @Test
    void separaConComaLosDronesDeLaMision() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");

        String json = exportar(misionConDrones(), destino);

        assertTrue(json.contains("    },\n    {\n"));
        assertEquals(2, json.split("\"serial\"", -1).length - 1);
    }

    @Test
    void elPesoSeEscribeComoNumeroSinComillas() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");

        String json = exportar(misionConDrones(), destino);

        assertTrue(json.contains("\"peso\": 12.4\n"));
        assertFalse(json.contains("\"peso\": \"12.4\""));
    }

    @Test
    void elJsonEmpiezaConLlaveYTerminaConLlaveYSaltoDeLinea() throws IOException {
        Path destino = carpeta.resolve("mision-M001.json");

        String json = exportar(misionConDrones(), destino);

        assertTrue(json.startsWith("{\n"));
        assertTrue(json.endsWith("}\n"));
    }

    // ------------------------------------------------------------------
    // Valores especiales
    // ------------------------------------------------------------------

    @Test
    void escribeNullSinComillasCuandoUnValorNoTieneDato() throws IOException {
        Path destino = carpeta.resolve("mision-vacia.json");

        String json = exportar(new Mision(), destino);

        assertTrue(json.contains("\"id\": null,"));
        assertTrue(json.contains("\"nombre\": null,"));
        assertTrue(json.contains("\"ubicacion\": null,"));
        assertTrue(json.contains("\"fecha\": null,"));
        assertTrue(json.contains("\"drones\": []"));
    }

    @Test
    void escribeNullEnLosCamposSinDatoDeUnDrone() throws IOException {
        Mision mision = new Mision("M004", "Reconocimiento", "Guatapé", "2026-09-22");
        mision.setDrones(List.of(new Vigilancia("D004", null, null, "Parrot", 8.1, true)));
        Path destino = carpeta.resolve("mision-M004.json");

        String json = exportar(mision, destino);

        assertTrue(json.contains("\"serial\": null,"));
        assertTrue(json.contains("\"modelo\": null,"));
        assertTrue(json.contains("\"fabricante\": \"Parrot\","));
    }

    @Test
    void escapaBarrasInvertidasSaltosDeLineaTabulacionesYRetornos() throws IOException {
        Mision mision = new Mision("M005", "Ruta C:\\vuelos\nSegunda línea\tTab\r", "Hangar 3", "2026-09-23");
        Path destino = carpeta.resolve("mision-M005.json");

        String json = exportar(mision, destino);

        assertTrue(json.contains("\"nombre\": \"Ruta C:\\\\vuelos\\nSegunda línea\\tTab\\r\""));
    }

    @Test
    void conservaLosCaracteresEspecialesDelEspanolEnElArchivo() throws IOException {
        Mision mision = new Mision("M006", "Inspección de cultivos", "Guatapé, Antioquia", "2026-09-24");
        Path destino = carpeta.resolve("mision-M006.json");

        String json = exportar(mision, destino);

        assertTrue(json.contains("Inspección"));
        assertTrue(json.contains("Guatapé"));
    }

    // ------------------------------------------------------------------
    // Comportamiento del adaptador frente al Adaptee y al cliente
    // ------------------------------------------------------------------

    @Test
    void elAdaptadorSeUsaComoUnExportadorMision() {
        ExportadorMision exportador = new MisionJsonAdapter(new ArchivoJson(carpeta.resolve("x.json").toString()));

        assertInstanceOf(MisionJsonAdapter.class, exportador);
    }

    @Test
    void delegaEnElServicioAdaptadoUnUnicoTextoConLaMisionEnJson() throws IOException {
        AtomicReference<String> recibido = new AtomicReference<>();
        AtomicReference<Integer> llamadas = new AtomicReference<>(0);
        ArchivoJson espia = new ArchivoJson("no-se-escribe.json") {
            @Override
            public void escribir(String contenido) {
                recibido.set(contenido);
                llamadas.set(llamadas.get() + 1);
            }
        };

        new MisionJsonAdapter(espia).exportar(misionConDrones());

        assertEquals(1, llamadas.get());
        assertTrue(recibido.get().contains("\"id\": \"M001\""));
        assertTrue(recibido.get().contains("\"serial\": \"SN-VIG-0932\""));
    }

    @Test
    void exportarDosVecesConElMismoServicioReemplazaElContenido() throws IOException {
        Path destino = carpeta.resolve("mision.json");
        ExportadorMision exportador = new MisionJsonAdapter(new ArchivoJson(destino.toString()));

        exportador.exportar(new Mision("M001", "Primera", "Rionegro", "2026-09-16"));
        exportador.exportar(new Mision("M002", "Segunda", "Guatapé", "2026-09-17"));

        String json = Files.readString(destino, StandardCharsets.UTF_8);
        assertTrue(json.contains("\"id\": \"M002\""));
        assertFalse(json.contains("M001"));
    }

    @Test
    void exportarNoModificaLaMision() throws IOException {
        Mision mision = misionConDrones();
        String antes = mision.toString();

        exportar(mision, carpeta.resolve("mision-M001.json"));

        assertEquals(antes, mision.toString());
        assertEquals(2, mision.getDrones().size());
    }

    @Test
    void propagaLaIOExceptionDelServicioAdaptado() {
        ExportadorMision exportador = new MisionJsonAdapter(new ArchivoJson(carpeta.toString()));
        Mision mision = misionConDrones();

        assertThrows(IOException.class, () -> exportador.exportar(mision));
    }
}
