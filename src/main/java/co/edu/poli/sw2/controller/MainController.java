package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.model.Drone;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Controlador asociado a la vista principal (GestorDrones.fxml).
 */
public class MainController {

    @FXML
    private TextField txtSerial;
    @FXML
    private TextField txtModelo;
    @FXML
    private TextField txtFabricante;
    @FXML
    private TextField txtPeso;

    @FXML
    private TableView<Drone> tablaDrones;
    @FXML
    private TableColumn<Drone, Integer> colId;
    @FXML
    private TableColumn<Drone, String> colSerial;
    @FXML
    private TableColumn<Drone, String> colModelo;
    @FXML
    private TableColumn<Drone, String> colFabricante;
    @FXML
    private TableColumn<Drone, Double> colPeso;

    private final DroneDAO droneDAO = new DroneDAO();
    private final ObservableList<Drone> drones = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idDrone"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));

        tablaDrones.setItems(drones);
        cargarDrones();
    }

    @FXML
    private void onCrear(ActionEvent event) {
        String serial = txtSerial.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String pesoTexto = txtPeso.getText();

        if (esVacio(serial) || esVacio(modelo) || esVacio(fabricante) || esVacio(pesoTexto)) {
            mostrarAlerta(AlertType.WARNING, "Todos los campos son obligatorios.");
            return;
        }

        double peso;
        try {
            peso = Double.parseDouble(pesoTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El peso debe ser un valor numerico.");
            return;
        }

        Drone drone = new Drone(0, serial, modelo, fabricante, peso);

        if (droneDAO.crear(drone)) {
            limpiarFormulario();
            cargarDrones();
        } else {
            mostrarAlerta(AlertType.ERROR, "No se pudo guardar el drone en la base de datos.");
        }
    }

    @FXML
    private void onConsultarTodos(ActionEvent event) {
        cargarDrones();
    }

    @FXML
    private void onConsultarPorId(ActionEvent event) {
        // TODO: implementar busqueda de un Drone por ID
        mostrarAlerta(AlertType.INFORMATION, "Funcionalidad pendiente de implementar.");
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        // TODO: implementar actualizacion de un Drone existente
        mostrarAlerta(AlertType.INFORMATION, "Funcionalidad pendiente de implementar.");
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        // TODO: implementar eliminacion de un Drone
        mostrarAlerta(AlertType.INFORMATION, "Funcionalidad pendiente de implementar.");
    }

    private void cargarDrones() {
        drones.setAll(droneDAO.obtenerTodos());
    }

    private void limpiarFormulario() {
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}
