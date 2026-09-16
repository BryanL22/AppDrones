package co.edu.poli.sw2;

import co.edu.poli.sw2.services.Conexion;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Screen;
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

        ScrollPane contenedor = new ScrollPane(root);
        contenedor.setFitToWidth(true);
        contenedor.setFitToHeight(true);

        Scene scene = new Scene(contenedor);
        scene.getStylesheets().add(getClass().getResource("css/style.css").toExternalForm());

        stage.setTitle("Gestion de Drones");
        stage.setScene(scene);
        stage.show();

        ajustarAPantalla(stage);
    }

    /**
     * Recorta la ventana al area visible de la pantalla y la centra.
     *
     * <p>La escena toma su tamano del contenido, que es mas alto que muchos
     * monitores. Sin este ajuste la ventana nace mas grande que la pantalla
     * y la barra de titulo queda fuera del area visible, de modo que no se
     * puede cerrar ni maximizar con el raton. El {@link ScrollPane} se
     * encarga de que el contenido recortado siga siendo alcanzable.</p>
     *
     * @param stage ventana principal ya mostrada, con su tamano calculado.
     */
    private void ajustarAPantalla(Stage stage) {
        Rectangle2D visible = Screen.getPrimary().getVisualBounds();
        stage.setWidth(Math.min(stage.getWidth(), visible.getWidth()));
        stage.setHeight(Math.min(stage.getHeight(), visible.getHeight()));
        stage.centerOnScreen();
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
