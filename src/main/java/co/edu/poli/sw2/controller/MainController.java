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
 *
 * <p>Siguiendo el patron MVC, esta clase solo lee/escribe los controles de
 * la vista y traduce las acciones del usuario en llamadas a {@link DroneDAO}.
 * No contiene SQL ni logica de acceso a datos: toda la persistencia vive en
 * la capa de modelo (paquetes {@code dao} y {@code database}).</p>
 */
public class MainController {

    @FXML
    private TextField txtId;
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
        tablaDrones.getSelectionModel().selectedItemProperty()
                .addListener((observable, anterior, seleccionado) -> {
                    if (seleccionado != null) {
                        llenarFormulario(seleccionado);
                    }
                });

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

        Double peso = parsearPeso(pesoTexto);
        if (peso == null) {
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
        Integer id = parsearId();
        if (id == null) {
            return;
        }

        Drone drone = droneDAO.obtenerPorId(id);
        if (drone == null) {
            mostrarAlerta(AlertType.INFORMATION, "No existe un drone con el ID " + id + ".");
            return;
        }

        llenarFormulario(drone);
        tablaDrones.getSelectionModel().select(drone);
    }

    @FXML
    private void onActualizar(ActionEvent event) {
        Integer id = parsearId();
        if (id == null) {
            return;
        }

        String serial = txtSerial.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String pesoTexto = txtPeso.getText();

        if (esVacio(serial) || esVacio(modelo) || esVacio(fabricante) || esVacio(pesoTexto)) {
            mostrarAlerta(AlertType.WARNING, "Todos los campos son obligatorios.");
            return;
        }

        Double peso = parsearPeso(pesoTexto);
        if (peso == null) {
            return;
        }

        Drone drone = new Drone(id, serial, modelo, fabricante, peso);

        if (droneDAO.actualizar(drone)) {
            limpiarFormulario();
            cargarDrones();
        } else {
            mostrarAlerta(AlertType.ERROR, "No se pudo actualizar el drone. Verifica que el ID exista.");
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        Integer id = parsearId();
        if (id == null) {
            return;
        }

        if (droneDAO.eliminar(id)) {
            limpiarFormulario();
            cargarDrones();
        } else {
            mostrarAlerta(AlertType.ERROR, "No se pudo eliminar el drone. Verifica que el ID exista.");
        }
    }

    private void cargarDrones() {
        drones.setAll(droneDAO.obtenerTodos());
    }

    private void llenarFormulario(Drone drone) {
        txtId.setText(String.valueOf(drone.getIdDrone()));
        txtSerial.setText(drone.getSerial());
        txtModelo.setText(drone.getModelo());
        txtFabricante.setText(drone.getFabricante());
        txtPeso.setText(String.valueOf(drone.getPeso()));
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private Integer parsearId() {
        String idTexto = txtId.getText();
        if (esVacio(idTexto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar el ID del drone.");
            return null;
        }

        try {
            return Integer.parseInt(idTexto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El ID debe ser un valor numerico.");
            return null;
        }
    }

    private Double parsearPeso(String pesoTexto) {
        try {
            return Double.parseDouble(pesoTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El peso debe ser un valor numerico.");
            return null;
        }
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}
