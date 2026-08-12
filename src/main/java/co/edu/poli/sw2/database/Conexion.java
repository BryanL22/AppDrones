package co.edu.poli.sw2.database;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Encargada de administrar la conexion JDBC hacia la base de datos MySQL.
 *
 * <p>Las credenciales nunca se dejan escritas en el codigo fuente. Se
 * resuelven en este orden de prioridad:</p>
 * <ol>
 *     <li>Variables de entorno del sistema operativo ({@code DB_URL},
 *     {@code DB_USER}, {@code DB_PASSWORD}).</li>
 *     <li>Archivo {@code .env} en la raiz del proyecto (ver
 *     {@code .env.example}), pensado solo para desarrollo local.</li>
 *     <li>Valores por defecto de desarrollo si ninguna de las anteriores
 *     esta definida.</li>
 * </ol>
 * <p>El archivo {@code .env} nunca debe subirse al repositorio: esta
 * excluido mediante {@code .gitignore}.</p>
 */
public class Conexion {

    private static final String ENV_FILE = ".env";

    private static final Map<String, String> VARIABLES_ENV = cargarArchivoEnv();

    private static final String URL = obtenerVariable("DB_URL",
            "jdbc:mysql://localhost:3306/appdrones?useSSL=false&serverTimezone=UTC");
    private static final String USUARIO = obtenerVariable("DB_USER", "root");
    private static final String PASSWORD = obtenerVariable("DB_PASSWORD", "");

    private Connection connection;

    public Connection getConnection() throws SQLException {
        connection = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        return connection;
    }

    public void cerrar() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Resuelve el valor de una variable de configuracion, dando prioridad a
     * las variables de entorno del sistema operativo y usando el archivo
     * {@code .env} (o un valor por defecto) como respaldo.
     *
     * @param clave nombre de la variable (p. ej. {@code DB_USER}).
     * @param porDefecto valor a usar si la variable no esta definida en ningun origen.
     * @return el valor resuelto para la variable.
     */
    private static String obtenerVariable(String clave, String porDefecto) {
        String valor = System.getenv(clave);
        if (valor == null || valor.isBlank()) {
            valor = VARIABLES_ENV.get(clave);
        }
        return (valor == null || valor.isBlank()) ? porDefecto : valor;
    }

    /**
     * Carga las variables definidas en el archivo {@code .env} de la raiz
     * del proyecto, si existe. El formato esperado es {@code CLAVE=valor},
     * una entrada por linea, admitiendo lineas en blanco y comentarios que
     * inicien con {@code #}.
     *
     * @return mapa con las variables encontradas; vacio si el archivo no existe.
     */
    private static Map<String, String> cargarArchivoEnv() {
        Map<String, String> variables = new HashMap<>();
        Path ruta = Path.of(ENV_FILE);

        if (!Files.exists(ruta)) {
            return variables;
        }

        try {
            for (String linea : Files.readAllLines(ruta)) {
                String texto = linea.trim();
                if (texto.isEmpty() || texto.startsWith("#") || !texto.contains("=")) {
                    continue;
                }
                int separador = texto.indexOf('=');
                String clave = texto.substring(0, separador).trim();
                String valor = texto.substring(separador + 1).trim();
                variables.put(clave, valor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return variables;
    }
}
