package co.edu.poli.sw2.services;

import org.junit.jupiter.api.Test;

import co.edu.poli.sw2.services.Conexion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConexionTest {

    @Test
    void parsearEnvIgnoraComentariosYLineasEnBlanco() {
        Map<String, String> variables = Conexion.parsearEnv(List.of(
                "# comentario",
                "",
                "   ",
                "DB_USER=root"
        ));

        assertEquals(1, variables.size());
        assertEquals("root", variables.get("DB_USER"));
    }

    @Test
    void parsearEnvRecortaEspaciosAlrededorDeClaveYValor() {
        Map<String, String> variables = Conexion.parsearEnv(List.of("  DB_PASSWORD =  1234  "));

        assertEquals("1234", variables.get("DB_PASSWORD"));
    }

    @Test
    void parsearEnvSoportaValoresConSignosDeIgualAdicionales() {
        Map<String, String> variables = Conexion.parsearEnv(List.of(
                "DB_URL=jdbc:mysql://localhost:3306/appdrones?useSSL=false&serverTimezone=UTC"
        ));

        assertEquals(
                "jdbc:mysql://localhost:3306/appdrones?useSSL=false&serverTimezone=UTC",
                variables.get("DB_URL")
        );
    }

    @Test
    void parsearEnvIgnoraLineasSinSignoDeIgual() {
        Map<String, String> variables = Conexion.parsearEnv(List.of("ESTO_NO_ES_VALIDO"));

        assertTrue(variables.isEmpty());
    }

    @Test
    void obtenerInstanciaSiempreDevuelveLaMismaInstancia() {
        Conexion primera = Conexion.obtenerInstancia();
        Conexion segunda = Conexion.obtenerInstancia();

        assertSame(primera, segunda);
    }

    @Test
    void getConnectionReutilizaLaMismaConexionMientrasSigaAbierta() throws SQLException {
        Conexion conexion = Conexion.obtenerInstancia();

        Connection primera = conexion.getConnection();
        Connection segunda = conexion.getConnection();

        assertSame(primera, segunda);
        assertFalse(primera.isClosed());
    }

    @Test
    void getConnectionAbreUnaNuevaConexionSiLaAnteriorFueCerrada() throws SQLException {
        Conexion conexion = Conexion.obtenerInstancia();

        Connection primera = conexion.getConnection();
        conexion.cerrar();
        assertTrue(primera.isClosed());

        Connection segunda = conexion.getConnection();

        assertNotSame(primera, segunda);
        assertFalse(segunda.isClosed());
    }
}
