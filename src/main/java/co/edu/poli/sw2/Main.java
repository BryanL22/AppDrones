package co.edu.poli.sw2;

import co.edu.poli.sw2.services.Conexion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Punto de entrada de la aplicacion de escritorio JavaFX.
 */
public class Main extends Application {

    /**
     * Crea la aplicacion. No recibe dependencias: JavaFX la instancia por
     * reflexion al invocar {@link #launch(String...)}.
     */
    public Main() {
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("view/GestorDrones.fxml"));
        Parent root = loader.load();

        stage.setTitle("Gestion de Drones");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        Conexion.obtenerInstancia().cerrar();
    }

    /**
     * Punto de entrada del ejecutable; delega en JavaFX el arranque de la
     * aplicacion.
     *
     * @param args argumentos de linea de comandos (no se usan).
     */
    public static void main(String[] args) {
        launch(args);
    }
}
