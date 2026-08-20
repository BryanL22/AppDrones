package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

    private static final String TIPO_AGRICULTURA = "Agricultura";
    private static final String TIPO_VIGILANCIA = "Vigilancia";

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
    private ComboBox<String> cbTipo;
    @FXML
    private Label lblCapacidadTanque;
    @FXML
    private TextField txtCapacidadTanque;
    @FXML
    private Label lblDeteccionTermica;
    @FXML
    private CheckBox chkDeteccionTermica;

    @FXML
    private TableView<Drone> tablaDrones;
    @FXML
    private TableColumn<Drone, String> colId;
    @FXML
    private TableColumn<Drone, String> colSerial;
    @FXML
    private TableColumn<Drone, String> colModelo;
    @FXML
    private TableColumn<Drone, String> colFabricante;
    @FXML
    private TableColumn<Drone, Double> colPeso;
    @FXML
    private TableColumn<Drone, String> colTipo;

    private final DroneDAO droneDAO = new DroneDAO();
    private final ObservableList<Drone> drones = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colTipo.setCellValueFactory(datos -> new SimpleStringProperty(tipoDe(datos.getValue())));

        cbTipo.setItems(FXCollections.observableArrayList(TIPO_AGRICULTURA, TIPO_VIGILANCIA));
        cbTipo.valueProperty().addListener((observable, anterior, nuevoTipo) -> mostrarCamposDeTipo(nuevoTipo));
        mostrarCamposDeTipo(null);

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
        String id = txtId.getText();
        String serial = txtSerial.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String pesoTexto = txtPeso.getText();
        String tipo = cbTipo.getValue();

        if (esVacio(id) || esVacio(serial) || esVacio(modelo) || esVacio(fabricante) || esVacio(pesoTexto)) {
            mostrarAlerta(AlertType.WARNING, "Todos los campos son obligatorios.");
            return;
        }

        if (esVacio(tipo)) {
            mostrarAlerta(AlertType.WARNING, "Selecciona el tipo de drone (Agricultura o Vigilancia).");
            return;
        }

        Double peso = parsearPeso(pesoTexto);
        if (peso == null) {
            return;
        }

        id = id.trim();

        if (droneDAO.obtenerPorId(id) != null) {
            mostrarAlerta(AlertType.WARNING, "Ya existe un drone con el ID '" + id + "'. Elige otro.");
            return;
        }

        Drone drone = construirDrone(tipo, id, serial, modelo, fabricante, peso);
        if (drone == null) {
            return;
        }

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
        String id = parsearId();
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
        String id = parsearId();
        if (id == null) {
            return;
        }

        String serial = txtSerial.getText();
        String modelo = txtModelo.getText();
        String fabricante = txtFabricante.getText();
        String pesoTexto = txtPeso.getText();
        String tipo = cbTipo.getValue();

        if (esVacio(serial) || esVacio(modelo) || esVacio(fabricante) || esVacio(pesoTexto)) {
            mostrarAlerta(AlertType.WARNING, "Todos los campos son obligatorios.");
            return;
        }

        if (esVacio(tipo)) {
            mostrarAlerta(AlertType.WARNING, "Selecciona el tipo de drone (Agricultura o Vigilancia).");
            return;
        }

        Double peso = parsearPeso(pesoTexto);
        if (peso == null) {
            return;
        }

        Drone drone = construirDrone(tipo, id, serial, modelo, fabricante, peso);
        if (drone == null) {
            return;
        }

        if (droneDAO.actualizar(drone)) {
            limpiarFormulario();
            cargarDrones();
        } else {
            mostrarAlerta(AlertType.ERROR,
                    "No se pudo actualizar el drone. Verifica que el ID exista y que el tipo coincida con el registrado.");
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String id = parsearId();
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

    /**
     * Construye la instancia concreta ({@link Agricultura} o {@link Vigilancia})
     * segun el tipo elegido en {@link #cbTipo}, leyendo el campo propio de
     * cada especializacion desde su control en la vista.
     */
    private Drone construirDrone(String tipo, String id, String serial, String modelo, String fabricante,
                                  double peso) {
        if (TIPO_AGRICULTURA.equals(tipo)) {
            Double capacidadTanque = parsearCapacidadTanque();
            if (capacidadTanque == null) {
                return null;
            }
            return new Agricultura(id, serial, modelo, fabricante, peso, capacidadTanque);
        }

        return new Vigilancia(id, serial, modelo, fabricante, peso, chkDeteccionTermica.isSelected());
    }

    private void cargarDrones() {
        drones.setAll(droneDAO.obtenerTodos());
    }

    /**
     * Determina el tipo de especializacion de un dron mediante {@code instanceof},
     * ya que {@link DroneDAO} devuelve instancias de {@link Agricultura} o
     * {@link Vigilancia} cuando corresponde.
     */
    private String tipoDe(Drone drone) {
        if (drone instanceof Agricultura) {
            return TIPO_AGRICULTURA;
        }
        if (drone instanceof Vigilancia) {
            return TIPO_VIGILANCIA;
        }
        return "-";
    }

    private void mostrarCamposDeTipo(String tipo) {
        boolean esAgricultura = TIPO_AGRICULTURA.equals(tipo);
        boolean esVigilancia = TIPO_VIGILANCIA.equals(tipo);

        lblCapacidadTanque.setVisible(esAgricultura);
        lblCapacidadTanque.setManaged(esAgricultura);
        txtCapacidadTanque.setVisible(esAgricultura);
        txtCapacidadTanque.setManaged(esAgricultura);

        lblDeteccionTermica.setVisible(esVigilancia);
        lblDeteccionTermica.setManaged(esVigilancia);
        chkDeteccionTermica.setVisible(esVigilancia);
        chkDeteccionTermica.setManaged(esVigilancia);
    }

    private void llenarFormulario(Drone drone) {
        txtId.setText(drone.getId());
        txtSerial.setText(drone.getSerial());
        txtModelo.setText(drone.getModelo());
        txtFabricante.setText(drone.getFabricante());
        txtPeso.setText(String.valueOf(drone.getPeso()));

        if (drone instanceof Agricultura agricultura) {
            cbTipo.setValue(TIPO_AGRICULTURA);
            txtCapacidadTanque.setText(String.valueOf(agricultura.getCapacidadTanque()));
        } else if (drone instanceof Vigilancia vigilancia) {
            cbTipo.setValue(TIPO_VIGILANCIA);
            chkDeteccionTermica.setSelected(vigilancia.isDeteccionTermica());
        } else {
            cbTipo.setValue(null);
        }
    }

    private void limpiarFormulario() {
        txtId.clear();
        txtSerial.clear();
        txtModelo.clear();
        txtFabricante.clear();
        txtPeso.clear();
        txtCapacidadTanque.clear();
        chkDeteccionTermica.setSelected(false);
        cbTipo.setValue(null);
    }

    private boolean esVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    private String parsearId() {
        String idTexto = txtId.getText();
        if (esVacio(idTexto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar el ID del drone.");
            return null;
        }

        return idTexto.trim();
    }

    private Double parsearPeso(String pesoTexto) {
        try {
            return Double.parseDouble(pesoTexto);
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "El peso debe ser un valor numerico.");
            return null;
        }
    }

    private Double parsearCapacidadTanque() {
        String texto = txtCapacidadTanque.getText();
        if (esVacio(texto)) {
            mostrarAlerta(AlertType.WARNING, "Debes indicar la capacidad del tanque.");
            return null;
        }

        try {
            return Double.parseDouble(texto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "La capacidad del tanque debe ser un valor numerico.");
            return null;
        }
    }

    private void mostrarAlerta(AlertType tipo, String mensaje) {
        new Alert(tipo, mensaje).showAndWait();
    }
}
