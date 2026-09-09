package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Vigilancia;
import co.edu.poli.sw2.services.AgriculturaFactory;
import co.edu.poli.sw2.services.DroneBuilder;
import co.edu.poli.sw2.services.DroneFactory;
import co.edu.poli.sw2.services.DronePrototype;
import co.edu.poli.sw2.services.VigilanciaFactory;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.SQLException;

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
    private Label lblIdentidadOriginal;
    @FXML
    private Label lblIdentidadClon;

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
    private TableColumn<Drone, String> colCapacidadTanque;
    @FXML
    private TableColumn<Drone, String> colDeteccionTermica;

    @FXML
    private TableView<FilaComparacion> tablaComparacion;
    @FXML
    private TableColumn<FilaComparacion, String> colCampoComparacion;
    @FXML
    private TableColumn<FilaComparacion, String> colOriginalComparacion;
    @FXML
    private TableColumn<FilaComparacion, String> colClonComparacion;

    private final DroneDAO droneDAO = new DroneDAO();
    private final ObservableList<Drone> drones = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colCapacidadTanque.setCellValueFactory(datos -> new SimpleStringProperty(capacidadTanqueDe(datos.getValue())));
        colDeteccionTermica.setCellValueFactory(datos -> new SimpleStringProperty(deteccionTermicaDe(datos.getValue())));

        colCampoComparacion.setCellValueFactory(new PropertyValueFactory<>("campo"));
        colOriginalComparacion.setCellValueFactory(new PropertyValueFactory<>("original"));
        colClonComparacion.setCellValueFactory(new PropertyValueFactory<>("clon"));

        cbTipo.setItems(FXCollections.observableArrayList(DroneFactory.tiposDisponibles()));
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

        Drone drone = construirDrone(tipo, id, serial, modelo, fabricante, peso);
        if (drone == null) {
            return;
        }

        try {
            if (droneDAO.obtenerPorId(id) != null) {
                mostrarAlerta(AlertType.WARNING, "Ya existe un drone con el ID '" + id + "'. Elige otro.");
                return;
            }

            if (droneDAO.crear(drone)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.ERROR, "No se pudo guardar el drone en la base de datos.");
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
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

        try {
            Drone drone = droneDAO.obtenerPorId(id);
            if (drone == null) {
                mostrarAlerta(AlertType.INFORMATION, "No existe un drone con el ID " + id + ".");
                return;
            }

            llenarFormulario(drone);
            tablaDrones.getSelectionModel().select(drone);
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
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

        try {
            if (droneDAO.actualizar(drone)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.ERROR,
                        "No se pudo actualizar el drone. Verifica que el ID exista y que el tipo coincida con el registrado.");
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
    }

    @FXML
    private void onEliminar(ActionEvent event) {
        String id = parsearId();
        if (id == null) {
            return;
        }

        try {
            if (droneDAO.eliminar(id)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.ERROR, "No se pudo eliminar el drone. Verifica que el ID exista.");
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * Clona (patron Prototype, via {@link DronePrototype}) el drone
     * seleccionado en la tabla y carga el clon en el formulario. Muestra la
     * identidad de memoria del original y del clon para evidenciar que son
     * dos objetos distintos con los mismos datos.
     */
    @FXML
    private void onClonar(ActionEvent event) {
        Drone original = tablaDrones.getSelectionModel().getSelectedItem();
        if (original == null) {
            mostrarAlerta(AlertType.WARNING, "Selecciona un drone de la tabla para clonarlo.");
            return;
        }

        Drone clon = DronePrototype.clonar(original);
        llenarFormulario(clon);

        lblIdentidadOriginal.setText("Original: " + identidadDe(original));
        lblIdentidadClon.setText("Clon: " + identidadDe(clon));

        tablaComparacion.setItems(FXCollections.observableArrayList(
                new FilaComparacion("ID", original.getId(), clon.getId()),
                new FilaComparacion("Serial", original.getSerial(), clon.getSerial()),
                new FilaComparacion("Modelo", original.getModelo(), clon.getModelo()),
                new FilaComparacion("Fabricante", original.getFabricante(), clon.getFabricante()),
                new FilaComparacion("Peso (kg)", String.valueOf(original.getPeso()), String.valueOf(clon.getPeso())),
                new FilaComparacion("Capacidad tanque (L)", capacidadTanqueDe(original), capacidadTanqueDe(clon)),
                new FilaComparacion("Deteccion termica", deteccionTermicaDe(original), deteccionTermicaDe(clon))
        ));
    }

    /**
     * Abre un dialogo minimo (patron Builder, via {@link DroneBuilder}) que
     * permite construir un drone de Agricultura llenando unicamente 3
     * atributos (ID, peso y capacidad del tanque); el resto de los atributos
     * comunes se completan con valores por defecto.
     */
    @FXML
    private void onBuilder(ActionEvent event) {
        Dialog<ButtonType> dialogo = new Dialog<>();
        dialogo.setTitle("Builder");
        dialogo.setHeaderText("Construir un drone de Agricultura llenando solo 3 atributos.");

        ButtonType btnConstruir = new ButtonType("Construir", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnConstruir, ButtonType.CANCEL);

        TextField campoId = new TextField();
        TextField campoPeso = new TextField();
        TextField campoCapacidadTanque = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.addRow(0, new Label("ID:"), campoId);
        grid.addRow(1, new Label("Peso (kg):"), campoPeso);
        grid.addRow(2, new Label("Capacidad tanque (L):"), campoCapacidadTanque);
        dialogo.getDialogPane().setContent(grid);

        dialogo.showAndWait().ifPresent(boton -> {
            if (boton == btnConstruir) {
                construirConBuilder(campoId.getText(), campoPeso.getText(), campoCapacidadTanque.getText());
            }
        });
    }

    /**
     * Valida y parsea los 3 atributos del dialogo del Builder, arma el drone
     * de Agricultura con {@link DroneBuilder} (usando valores por defecto
     * para serial, modelo y fabricante) y lo persiste con {@link #droneDAO}.
     */
    private void construirConBuilder(String idTexto, String pesoTexto, String capacidadTanqueTexto) {
        if (esVacio(idTexto) || esVacio(pesoTexto) || esVacio(capacidadTanqueTexto)) {
            mostrarAlerta(AlertType.WARNING, "Debes llenar ID, peso y capacidad del tanque.");
            return;
        }

        double peso;
        double capacidadTanque;
        try {
            peso = Double.parseDouble(pesoTexto.trim());
            capacidadTanque = Double.parseDouble(capacidadTanqueTexto.trim());
        } catch (NumberFormatException e) {
            mostrarAlerta(AlertType.WARNING, "Peso y capacidad del tanque deben ser valores numericos.");
            return;
        }

        String id = idTexto.trim();
        Drone drone;
        try {
            drone = new DroneBuilder()
                    .id(id)
                    .serial("AUTO-" + id)
                    .modelo("Generico")
                    .fabricante("GenericoSA")
                    .peso(peso)
                    .capacidadTanque(capacidadTanque)
                    .construir();
        } catch (IllegalStateException e) {
            mostrarAlerta(AlertType.ERROR, e.getMessage());
            return;
        }

        try {
            if (droneDAO.crear(drone)) {
                cargarDrones();
                mostrarAlerta(AlertType.INFORMATION, "Drone de Agricultura creado con el Builder.");
            } else {
                mostrarAlerta(AlertType.ERROR, "No se pudo guardar el drone construido en la base de datos.");
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * Representa la identidad de un objeto en memoria, con el mismo formato
     * que usa {@link Object#toString()} por defecto ({@code Clase@hash}),
     * util para comprobar visualmente que dos referencias no apuntan a la
     * misma instancia.
     */
    private String identidadDe(Drone drone) {
        return drone.getClass().getSimpleName() + "@" + Integer.toHexString(System.identityHashCode(drone));
    }

    /**
     * Construye la instancia concreta ({@link Agricultura} o {@link Vigilancia})
     * segun el tipo elegido en {@link #cbTipo}, delegando en la fabrica
     * concreta ({@link AgriculturaFactory} o {@link VigilanciaFactory}) que
     * extiende la plantilla {@link DroneFactory}. Antes de eso, lee y valida
     * desde la vista el campo propio de la especializacion elegida.
     */
    private Drone construirDrone(String tipo, String id, String serial, String modelo, String fabricante,
                                  double peso) {
        if (DroneFactory.TIPO_AGRICULTURA.equals(tipo)) {
            Double capacidadTanque = parsearCapacidadTanque();
            if (capacidadTanque == null) {
                return null;
            }
            return new AgriculturaFactory(capacidadTanque).crear(id, serial, modelo, fabricante, peso);
        }

        return new VigilanciaFactory(chkDeteccionTermica.isSelected()).crear(id, serial, modelo, fabricante, peso);
    }

    private void cargarDrones() {
        try {
            drones.setAll(droneDAO.obtenerTodos());
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * Devuelve la capacidad del tanque para mostrar en la tabla, tomada de
     * la tabla {@code agricultura}; vacio si el dron no es {@link Agricultura}.
     */
    private String capacidadTanqueDe(Drone drone) {
        if (drone instanceof Agricultura agricultura) {
            return String.valueOf(agricultura.getCapacidadTanque());
        }
        return "";
    }

    /**
     * Devuelve la deteccion termica para mostrar en la tabla, tomada de la
     * tabla {@code vigilancia}; vacio si el dron no es {@link Vigilancia}.
     */
    private String deteccionTermicaDe(Drone drone) {
        if (drone instanceof Vigilancia vigilancia) {
            return vigilancia.isDeteccionTermica() ? "Si" : "No";
        }
        return "";
    }

    private void mostrarCamposDeTipo(String tipo) {
        boolean esAgricultura = DroneFactory.TIPO_AGRICULTURA.equals(tipo);
        boolean esVigilancia = DroneFactory.TIPO_VIGILANCIA.equals(tipo);

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
            cbTipo.setValue(DroneFactory.TIPO_AGRICULTURA);
            txtCapacidadTanque.setText(String.valueOf(agricultura.getCapacidadTanque()));
        } else if (drone instanceof Vigilancia vigilancia) {
            cbTipo.setValue(DroneFactory.TIPO_VIGILANCIA);
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

    /**
     * Fila de la tabla comparativa de {@link #onClonar}: un campo del dron
     * con su valor en el original y en el clon, para evidenciar visualmente
     * que ambos objetos tienen los mismos datos aunque sean instancias
     * distintas. Sus getters siguen la convencion de propiedades de JavaFX
     * porque {@link #colCampoComparacion} y compania los leen via
     * {@link PropertyValueFactory}.
     */
    public static class FilaComparacion {
        private final String campo;
        private final String original;
        private final String clon;

        private FilaComparacion(String campo, String original, String clon) {
            this.campo = campo;
            this.original = original;
            this.clon = clon;
        }

        public String getCampo() {
            return campo;
        }

        public String getOriginal() {
            return original;
        }

        public String getClon() {
            return clon;
        }
    }
}
