package co.edu.poli.sw2.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas unitarias para el servicio adaptado (Adaptee) del patrón de diseño
 * Adapter: {@link ArchivoJson}. Verifican que solo sabe guardar el texto que
 * recibe, sin conocer ninguna clase del modelo; la traducción de una misión a
 * ese texto se prueba en {@link MisionJsonAdapterTest}.
 */
class ArchivoJsonTest {

    @TempDir
    Path carpeta;

    @Test
    void escribeElContenidoTalComoLoRecibe() throws IOException {
        Path destino = carpeta.resolve("datos.json");
        String contenido = "{\n  \"clave\": \"valor\"\n}\n";

        new ArchivoJson(destino.toString()).escribir(contenido);

        assertEquals(contenido, Files.readString(destino, StandardCharsets.UTF_8));
    }

    @Test
    void guardaElArchivoEnUtf8() throws IOException {
        Path destino = carpeta.resolve("acentos.json");
        String contenido = "{\"nombre\": \"Inspección de cultivos en Guatapé\"}";

        new ArchivoJson(destino.toString()).escribir(contenido);

        assertArrayEquals(contenido.getBytes(StandardCharsets.UTF_8), Files.readAllBytes(destino));
    }

    @Test
    void creaLasCarpetasIntermediasQueNoExisten() throws IOException {
        Path destino = carpeta.resolve("misiones").resolve("2026").resolve("mision.json");

        new ArchivoJson(destino.toString()).escribir("{}");

        assertTrue(Files.isDirectory(carpeta.resolve("misiones").resolve("2026")));
        assertTrue(Files.exists(destino));
    }

    @Test
    void reemplazaElArchivoSiYaExistia() throws IOException {
        Path destino = carpeta.resolve("mision.json");
        ArchivoJson archivo = new ArchivoJson(destino.toString());

        archivo.escribir("{\"version\": 1, \"contenido\": \"largo largo largo\"}");
        archivo.escribir("{\"version\": 2}");

        assertEquals("{\"version\": 2}", Files.readString(destino, StandardCharsets.UTF_8));
    }

    @Test
    void escribirUnTextoVacioCreaUnArchivoVacio() throws IOException {
        Path destino = carpeta.resolve("vacio.json");

        new ArchivoJson(destino.toString()).escribir("");

        assertTrue(Files.exists(destino));
        assertEquals(0, Files.size(destino));
    }

    @Test
    void escribirEnUnaRutaSinCarpetaPadreUsaLaCarpetaDeTrabajo() throws IOException {
        String nombre = "archivo-json-test-sin-carpeta.json";
        Path destino = Path.of(nombre);

        try {
            new ArchivoJson(nombre).escribir("{}");

            assertTrue(Files.exists(destino));
            assertEquals("{}", Files.readString(destino, StandardCharsets.UTF_8));
        } finally {
            Files.deleteIfExists(destino);
        }
    }

    @Test
    void laRutaAbsolutaCoincideConLaRutaIndicadaCuandoYaEraAbsoluta() {
        Path destino = carpeta.resolve("mision.json");

        ArchivoJson archivo = new ArchivoJson(destino.toString());

        assertEquals(destino.toAbsolutePath().toString(), archivo.getRutaAbsoluta());
    }

    @Test
    void laRutaAbsolutaConvierteUnaRutaRelativaEnAbsoluta() {
        ArchivoJson archivo = new ArchivoJson("misiones/mision-M001.json");

        String absoluta = archivo.getRutaAbsoluta();

        assertTrue(Path.of(absoluta).isAbsolute());
        assertTrue(absoluta.endsWith(Path.of("misiones", "mision-M001.json").toString()));
    }

    @Test
    void obtenerLaRutaAbsolutaNoCreaNingunArchivo() {
        Path destino = carpeta.resolve("no-se-crea.json");

        new ArchivoJson(destino.toString()).getRutaAbsoluta();

        assertFalse(Files.exists(destino));
    }

    @Test
    void lanzaIOExceptionSiLaRutaEsUnaCarpeta() {
        ArchivoJson archivo = new ArchivoJson(carpeta.toString());

        assertThrows(IOException.class, () -> archivo.escribir("{}"));
    }

    @Test
    void lanzaIOExceptionSiUnaCarpetaIntermediaEsEnRealidadUnArchivo() throws IOException {
        Path bloqueo = Files.writeString(carpeta.resolve("bloqueo"), "soy un archivo");
        Path destino = bloqueo.resolve("sub").resolve("mision.json");
        ArchivoJson archivo = new ArchivoJson(destino.toString());

        assertThrows(IOException.class, () -> archivo.escribir("{}"));
    }
}
