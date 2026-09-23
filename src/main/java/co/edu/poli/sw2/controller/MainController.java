package co.edu.poli.sw2.controller;

import co.edu.poli.sw2.dao.DroneDAO;
import co.edu.poli.sw2.model.Agricultura;
import co.edu.poli.sw2.model.Drone;
import co.edu.poli.sw2.model.Mision;
import co.edu.poli.sw2.model.Sensor;
import co.edu.poli.sw2.model.Vigilancia;
import co.edu.poli.sw2.services.ArchivoJson;
import co.edu.poli.sw2.services.BateriaAdicional;
import co.edu.poli.sw2.services.Composite;
import co.edu.poli.sw2.services.ControlAutonomo;
import co.edu.poli.sw2.services.ControlBasico;
import co.edu.poli.sw2.services.ControlVuelo;
import co.edu.poli.sw2.services.DroneBuilder;
import co.edu.poli.sw2.services.DronComponent;
import co.edu.poli.sw2.services.DronePrototype;
import co.edu.poli.sw2.services.DronService;
import co.edu.poli.sw2.services.DronServiceProxy;
import co.edu.poli.sw2.services.DronWrapper;
import co.edu.poli.sw2.services.ExportadorMision;
import co.edu.poli.sw2.services.FabricaDrones;
import co.edu.poli.sw2.services.MisionJsonAdapter;
import co.edu.poli.sw2.services.ServiceInterface;
import co.edu.poli.sw2.services.WrapperSensor;

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
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Controlador asociado a la vista principal (GestorDrones.fxml).
 *
 * <p>
 * Siguiendo el patron MVC, esta clase solo lee/escribe los controles de
 * la vista y traduce las acciones del usuario en llamadas a {@link DroneDAO}.
 * No contiene SQL ni logica de acceso a datos: toda la persistencia vive en
 * la capa de modelo (paquetes {@code dao} y {@code database}).
 * </p>
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
    private RadioButton rbControlBasico;
    @FXML
    private RadioButton rbControlAutonomo;
    @FXML
    private ToggleGroup grupoControl;
    @FXML
    private Label lblIdentidadOriginal;
    @FXML
    private Label lblIdentidadClon;
    @FXML
    private CheckBox chkBateriaAdicional;

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
    private final FabricaDrones fabrica = new FabricaDrones();
    private final ServiceInterface dronService = new DronServiceProxy(new DronService());
    private final ObservableList<Drone> drones = FXCollections.observableArrayList();

    /**
     * Crea el controlador. No recibe dependencias: {@link javafx.fxml.FXMLLoader}
     * lo instancia por reflexion al cargar {@code GestorDrones.fxml}.
     */
    public MainController() {
    }

    @FXML
    private void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colSerial.setCellValueFactory(new PropertyValueFactory<>("serial"));
        colModelo.setCellValueFactory(new PropertyValueFactory<>("modelo"));
        colFabricante.setCellValueFactory(new PropertyValueFactory<>("fabricante"));
        colPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        colCapacidadTanque.setCellValueFactory(datos -> new SimpleStringProperty(capacidadTanqueDe(datos.getValue())));
        colDeteccionTermica
                .setCellValueFactory(datos -> new SimpleStringProperty(deteccionTermicaDe(datos.getValue())));

        colCampoComparacion.setCellValueFactory(new PropertyValueFactory<>("campo"));
        colOriginalComparacion.setCellValueFactory(new PropertyValueFactory<>("original"));
        colClonComparacion.setCellValueFactory(new PropertyValueFactory<>("clon"));

        cbTipo.setItems(FXCollections.observableArrayList(fabrica.tiposDisponibles()));
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

    /**
     * Devuelve el tipo de control seleccionado actualmente en los RadioButtons.
     *
     * @return "Control autónomo" o "Control básico".
     */
    private String getTipoControlSeleccionado() {
        return rbControlAutonomo != null && rbControlAutonomo.isSelected()
                ? ControlAutonomo.NOMBRE
                : ControlBasico.NOMBRE;
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
            if (droneDAO.obtenerPorId(id) == null) {
                mostrarAlerta(AlertType.ERROR, "No se pudo eliminar el drone. Verifica que el ID exista.");
                return;
            }

            String password = pedirPassword();
            if (password == null) {
                return;
            }

            // El borrado pasa por el Proxy: si la contrasena no es correcta,
            // la solicitud no llega al servicio real.
            if (dronService.eliminarDron(id, password)) {
                limpiarFormulario();
                cargarDrones();
            } else {
                mostrarAlerta(AlertType.WARNING, "Contrasena incorrecta. No se elimino el drone.");
            }
        } catch (SQLException | IOException e) {
            mostrarAlerta(AlertType.ERROR, "Error de base de datos: " + e.getMessage());
        }
    }

    /**
     * Pide la contrasena que protege el borrado. Vive en el controlador y no
     * en el proxy para que la capa de servicios no dependa de JavaFX.
     *
     * @return la contrasena escrita, o {@code null} si se cancelo el dialogo.
     */
    private String pedirPassword() {
        Dialog<String> dialogo = new Dialog<>();
        dialogo.setTitle("Eliminar drone");
        dialogo.setHeaderText("Ingresa la contrasena para eliminar el drone.");

        ButtonType btnAceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        dialogo.getDialogPane().getButtonTypes().addAll(btnAceptar, ButtonType.CANCEL);

        PasswordField campoPassword = new PasswordField();
        campoPassword.setPromptText("Contrasena");
        dialogo.getDialogPane().setContent(campoPassword);

        dialogo.setResultConverter(boton -> boton == btnAceptar ? campoPassword.getText() : null);

        return dialogo.showAndWait().orElse(null);
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
                new FilaComparacion("Deteccion termica", deteccionTermicaDe(original), deteccionTermicaDe(clon))));
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
     * Toma el drone seleccionado en la tabla y arma la cadena del patron
     * Decorator ({@link DronWrapper} -&gt; {@link BateriaAdicional}, ambos
     * implementando {@link DronComponent}) para mostrar, en un Alert, la
     * descripcion del drone. Si {@link #chkBateriaAdicional} esta marcado,
     * se le agrega la bateria adicional; si no, se muestra sin decorar.
     */
    @FXML
    private void onDecorator(ActionEvent event) {
        Drone seleccionado = tablaDrones.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta(AlertType.WARNING, "Selecciona un drone de la tabla para ver el Decorator.");
            return;
        }

        DronWrapper wrapper = new DronWrapper(seleccionado);

        String mensaje;
        if (chkBateriaAdicional.isSelected()) {
            BateriaAdicional conBateriaAdicional = new BateriaAdicional(wrapper, 20.0);
            mensaje = "Con Bateria Adicional (Decorator):\n" + conBateriaAdicional.describir()
                    + "\nAutonomia adicional: +" + conBateriaAdicional.getAutonomiaAdicionalMinutos() + " min";
        } else {
            mensaje = "Sin decorar:\n" + wrapper.describir();
        }

        mostrarAlerta(AlertType.INFORMATION, mensaje);
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
     * segun el tipo elegido en {@link #cbTipo}, delegando en la fachada
     * {@link FabricaDrones}. El controlador ya no elige la factoria ni la
     * configura: solo lee y valida desde la vista el campo propio de la
     * especializacion y entrega los datos.
     */
    private Drone construirDrone(String tipo, String id, String serial, String modelo, String fabricante,
            double peso) {
        double capacidadTanque = 0;
        if (FabricaDrones.TIPO_AGRICULTURA.equals(tipo)) {
            Double valor = parsearCapacidadTanque();
            if (valor == null) {
                return null;
            }
            capacidadTanque = valor;
        }

        return fabrica.crear(tipo, id, serial, modelo, fabricante, peso,
                capacidadTanque, chkDeteccionTermica.isSelected());
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
        boolean esAgricultura = FabricaDrones.TIPO_AGRICULTURA.equals(tipo);
        boolean esVigilancia = FabricaDrones.TIPO_VIGILANCIA.equals(tipo);

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

        // El dron no almacena tipo de control (no es responsabilidad suya);
        // el selector de control vuelve a su valor por defecto al cargar el formulario.
        rbControlBasico.setSelected(true);

        if (drone instanceof Agricultura agricultura) {
            cbTipo.setValue(FabricaDrones.TIPO_AGRICULTURA);
            txtCapacidadTanque.setText(String.valueOf(agricultura.getCapacidadTanque()));
        } else if (drone instanceof Vigilancia vigilancia) {
            cbTipo.setValue(FabricaDrones.TIPO_VIGILANCIA);
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
        rbControlBasico.setSelected(true);
    }

    /**
     * Demuestra el patron Bridge: el usuario selecciona el tipo de control
     * (RadioButtons) y este metodo arma un {@link ControlVuelo} (la
     * Abstraccion del puente) asociando ese control con el dron
     * seleccionado, sin que el dron conozca ni almacene esa asociacion.
     * El resultado que se muestra es, unicamente, el mensaje breve pedido
     * por negocio: "Dron con control autónomo" o "Dron con control básico".
     */
    @FXML
    private void onEjecutarControl(ActionEvent event) {
        Drone drone = tablaDrones.getSelectionModel().getSelectedItem();
        if (drone == null) {
            String id = txtId.getText();
            String serial = txtSerial.getText();
            String modelo = txtModelo.getText();
            String fabricante = txtFabricante.getText();
            String pesoTexto = txtPeso.getText();
            String tipo = cbTipo.getValue();

            if (!esVacio(id) && !esVacio(serial) && !esVacio(modelo) && !esVacio(fabricante)
                    && !esVacio(pesoTexto) && !esVacio(tipo)) {
                Double peso = parsearPeso(pesoTexto);
                if (peso != null) {
                    drone = construirDrone(tipo, id.trim(), serial, modelo, fabricante, peso);
                }
            }
        }

        if (drone == null) {
            mostrarAlerta(AlertType.WARNING,
                    "Selecciona un drone de la tabla o completa el formulario para probar el control.");
            return;
        }

        ControlVuelo controlVuelo = ControlVuelo.crear(getTipoControlSeleccionado(), drone);

        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Control de vuelo");
        alerta.setHeaderText(null);
        alerta.setContentText(controlVuelo.descripcionBreve());
        alerta.showAndWait();
    }

    /**
     * Demuestra el patrón Adapter: toma una {@link Mision} precargada y la
     * exporta a un archivo JSON. El controlador solo conoce la interfaz
     * {@link ExportadorMision}; quien traduce la misión a texto es
     * {@link MisionJsonAdapter} y quien crea el archivo es
     * {@link ArchivoJson}.
     */
    @FXML
    private void onExportarMisionJson(ActionEvent event) {
        Mision mision = misionDeEjemplo();
        ArchivoJson archivo = new ArchivoJson("misiones/mision-" + mision.getId() + ".json");
        ExportadorMision exportador = new MisionJsonAdapter(archivo);

        try {
            exportador.exportar(mision);
            mostrarAlerta(AlertType.INFORMATION,
                    "Archivo JSON creado con la misión \"" + mision.getNombre() + "\" y "
                            + mision.getDrones().size() + " drones.\n\n"
                            + archivo.getRutaAbsoluta());
        } catch (IOException e) {
            mostrarAlerta(AlertType.ERROR, "No se pudo crear el archivo JSON: " + e.getMessage());
        }
    }

    /**
     * Misión precargada que sirve de evidencia para el patrón Adapter: se
     * arma en memoria, sin pasar por la base de datos, para que el botón
     * siempre exporte el mismo contenido.
     *
     * @return la misión de ejemplo con sus drones.
     */
    private Mision misionDeEjemplo() {
        Mision mision = new Mision("M001", "Inspección de cultivos",
                "Finca La Esperanza, Rionegro", "2026-09-16");
        mision.setDrones(List.of(
                new Agricultura("D001", "SN-AGR-1145", "AgroWing X2", "DJI", 12.4, 18.0),
                new Vigilancia("D002", "SN-VIG-0932", "SkyGuard 500", "Parrot", 8.1, true)));
        return mision;
    }

    /**
     * Demuestra el patron Composite armando el arbol de sensores del dron.
     *
     * <p>El arbol se construye aqui, en el controlador: los grupos son
     * {@link Composite} y los sensores concretos son {@link WrapperSensor}.
     * "Sensor Digital" es a la vez hijo de "Sensor Sonido" y padre de SPI y
     * UART, lo que demuestra que el arbol se puede anidar a cualquier
     * profundidad. Al final basta una sola llamada a {@code leer()} sobre la
     * raiz para recorrerlo completo.</p>
     */
    @FXML
    private void onComposite(ActionEvent event) {
        WrapperSensor infrarrojo = new WrapperSensor(new Sensor("T-01", "Sensor Infrarrojo", "Melexis"));
        WrapperSensor rtd = new WrapperSensor(new Sensor("T-02", "RTD", "Honeywell"));
        WrapperSensor cmos = new WrapperSensor(new Sensor("C-01", "Sensor CMOS", "Sony"));
        WrapperSensor ccd = new WrapperSensor(new Sensor("C-02", "Sensor CCD", "Teledyne"));
        WrapperSensor analogico = new WrapperSensor(new Sensor("S-01", "Sensor Analogico", "Bosch"));
        WrapperSensor spi = new WrapperSensor(new Sensor("D-01", "SPI", "Texas Instruments"));
        WrapperSensor uart = new WrapperSensor(new Sensor("D-02", "UART", "FTDI"));
        WrapperSensor inteligente = new WrapperSensor(new Sensor("I-01", "Sensor Inteligente", "Nvidia"));

        Composite temperatura = new Composite();
        temperatura.add(infrarrojo);
        temperatura.add(rtd);

        Composite camara = new Composite();
        camara.add(cmos);
        camara.add(ccd);

        Composite digital = new Composite();
        digital.add(spi);
        digital.add(uart);

        Composite sonido = new Composite();
        sonido.add(analogico);
        sonido.add(digital);

        Composite general = new Composite();
        general.add(temperatura);
        general.add(camara);
        general.add(sonido);
        general.add(inteligente);

        StringBuilder arbol = new StringBuilder();
        arbol.append("Sensor General\n");
        arbol.append("├── Sensor Temperatura\n");
        arbol.append("│   ├── ").append(infrarrojo.leer()).append("\n");
        arbol.append("│   └── ").append(rtd.leer()).append("\n");
        arbol.append("├── Sensor Camara\n");
        arbol.append("│   ├── ").append(cmos.leer()).append("\n");
        arbol.append("│   └── ").append(ccd.leer()).append("\n");
        arbol.append("├── Sensor Sonido\n");
        arbol.append("│   ├── ").append(analogico.leer()).append("\n");
        arbol.append("│   └── Sensor Digital\n");
        arbol.append("│       ├── ").append(spi.leer()).append("\n");
        arbol.append("│       └── ").append(uart.leer()).append("\n");
        arbol.append("└── ").append(inteligente.leer()).append("\n");
        arbol.append("\nUna sola llamada a general.leer() recorre los ")
                .append(general.leer().split("\n").length)
                .append(" sensores del arbol.");

        // Fuente monoespaciada para que la sangria del arbol quede alineada.
        Label contenido = new Label(arbol.toString());
        contenido.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px;");

        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Composite - Arbol de sensores");
        alerta.setHeaderText(null);
        alerta.getDialogPane().setContent(contenido);
        alerta.showAndWait();
    }

    /**
     * Demuestra el patron Facade: le pide a {@link FabricaDrones} un dron de
     * cada tipo con una sola llamada por tipo.
     *
     * <p>A diferencia de los demas patrones, este no vive solo en su boton:
     * es la misma via por la que ya pasan {@link #onCrear} y
     * {@link #onActualizar}. El controlador no instancia ninguna factoria
     * concreta ni sabe como se configuran.</p>
     */
    @FXML
    private void onFacade(ActionEvent event) {
        Drone agricultura = fabrica.crear(FabricaDrones.TIPO_AGRICULTURA,
                "A-DEMO", "SN-AGR-DEMO", "AgroWing X2", "DJI", 12.4, 18.0, false);
        Drone vigilancia = fabrica.crear(FabricaDrones.TIPO_VIGILANCIA,
                "V-DEMO", "SN-VIG-DEMO", "SkyGuard 500", "Parrot", 8.1, 0, true);

        String mensaje = "Tipos que ofrece el subsistema:\n  "
                + String.join(", ", fabrica.tiposDisponibles())
                + "\n\nUna sola llamada por tipo:\n"
                + "  fabrica.crear(TIPO_AGRICULTURA, ...)\n"
                + "    -> " + agricultura.getClass().getSimpleName()
                + " con " + capacidadTanqueDe(agricultura) + " L de tanque\n"
                + "  fabrica.crear(TIPO_VIGILANCIA, ...)\n"
                + "    -> " + vigilancia.getClass().getSimpleName()
                + " con deteccion termica: " + deteccionTermicaDe(vigilancia)
                + "\n\nEl controlador no instancio AgriculturaFactory ni\n"
                + "VigilanciaFactory: la fachada eligio por el.\n\n"
                + "Este mismo camino es el que usan los botones\n"
                + "Crear y Actualizar.";

        // Fuente monoespaciada para que la sangria del mensaje quede alineada.
        Label contenido = new Label(mensaje);
        contenido.setStyle("-fx-font-family: 'Consolas', 'Courier New', monospace; -fx-font-size: 13px;");

        Alert alerta = new Alert(AlertType.INFORMATION);
        alerta.setTitle("Facade - FabricaDrones");
        alerta.setHeaderText(null);
        alerta.getDialogPane().setContent(contenido);
        alerta.showAndWait();
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

        /**
         * Devuelve el nombre del campo comparado (por ejemplo, "ID" o "Peso (kg)").
         *
         * @return el nombre del campo.
         */
        public String getCampo() {
            return campo;
        }

        /**
         * Devuelve el valor de este campo en el drone original.
         *
         * @return el valor en el original.
         */
        public String getOriginal() {
            return original;
        }

        /**
         * Devuelve el valor de este campo en el drone clonado.
         *
         * @return el valor en el clon.
         */
        public String getClon() {
            return clon;
        }
    }
}
