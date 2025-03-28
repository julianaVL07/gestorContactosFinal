package co.edu.uniquindio.gestorcontactos.controladores;

import co.edu.uniquindio.gestorcontactos.modelo.Contacto;
import co.edu.uniquindio.gestorcontactos.modelo.GestionContacto;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import java.io.File;
import javafx.geometry.Pos;
import javafx.geometry.Insets;

public class ControladorInterfazUsuario implements Initializable {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtTelefono;

    @FXML
    private DatePicker txtFechaCumpleanos;

    @FXML
    private TextField txtEmail;

    @FXML
    private Button btnSeleccionarImagen; // Botón para seleccionar la imagen

    @FXML
    private ImageView imageView; // ImageView para mostrar la imagen

    @FXML
    private ComboBox<String> txtBusqueda;

    @FXML
    private TextField txtBusquedaTexto;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnReestablecer;

    @FXML
    private TableView<Contacto> tablaContactos;

    @FXML
    private TableColumn<Contacto, String> colNombre;

    @FXML
    private TableColumn<Contacto, String> colApellido;

    @FXML
    private TableColumn<Contacto, String> colTelefono;

    @FXML
    private TableColumn<Contacto, String> colFechaCumpleanos;

    @FXML
    private TableColumn<Contacto, String> colEmail;

    private File imagenSeleccionada;

    private final GestionContacto gestionContacto; //Instancia de la clase GestionContacto

    private Contacto contactoSeleccionado; //contacto seleccionada de la tabla

    private ObservableList<Contacto> ContactosObservable; //Lista observable de contactos

    public ControladorInterfazUsuario() {
        gestionContacto = new GestionContacto();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Asignar las propiedades de la nota a las columnas de la tabla
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));
        colFechaCumpleanos.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFechaCumpleanos().toString()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        txtNombre.setText("pepito");
        txtApellido.setText("perez");
        txtTelefono.setText("1234567890");
        txtFechaCumpleanos.setValue(LocalDate.of(2025, 10, 10));
        txtEmail.setText("pepito@email.com");



        //Cargar busquedas en el ComboBox
        txtBusqueda.setItems( FXCollections.observableList(gestionContacto.listarBusqueda()) );

        //Inicializar lista observable y cargar los contactos
        ContactosObservable = FXCollections.observableArrayList();
        cargarContactos();

        //Evento click en la tabla
        tablaContactos.setOnMouseClicked(e -> {
            //Obtener el contacto seleccionado
            contactoSeleccionado = tablaContactos.getSelectionModel().getSelectedItem();

            if(contactoSeleccionado != null){
                txtNombre.setText(contactoSeleccionado.getNombre());
                txtApellido.setText(contactoSeleccionado.getApellido());
                txtTelefono.setText(contactoSeleccionado.getTelefono());
                txtFechaCumpleanos.setValue(contactoSeleccionado.getFechaCumpleanos());
                txtEmail.setText(contactoSeleccionado.getEmail());

            }
            // Mostrar la imagen del contacto seleccionado
            if (contactoSeleccionado.getSeleccionarImagen() != null) { // Cambio en el nombre del método
                imageView.setImage(new Image(contactoSeleccionado.getSeleccionarImagen().toURI().toString()));
            } else {
                imageView.setImage(null);
            }

        });

    }

//Funcionalidades

    //manejar por que búsqueda selecciona
    @FXML
    private void manejarBusqueda(ActionEvent e) {
        String criterio = txtBusqueda.getValue();

        if (criterio == null || txtBusquedaTexto.getText().trim().isEmpty()) {
            mostrarAlerta("Seleccione un criterio y escriba un valor para buscar.", Alert.AlertType.WARNING);
            return;
        }

        if ("Nombre".equals(criterio)) {
            buscarPorNombre();
        } else if ("Telefono".equals(criterio)) {
            buscarPorTelefono();
        } else {
            mostrarAlerta("Criterio no válido.", Alert.AlertType.WARNING);
        }
    }

    // Búsqueda por nombre
    public void buscarPorNombre() {
        String nombre = txtBusquedaTexto.getText().trim().toLowerCase();

        if (!nombre.isEmpty()) {
            List<Contacto> encontrados = gestionContacto.obtenerContactosPorNombre(nombre);

            if (!encontrados.isEmpty()) {
                ContactosObservable.setAll(encontrados); // Modificar la lista observable en lugar de cambiar Items
                tablaContactos.refresh(); // Asegurar que la tabla se actualiza
            } else {
                mostrarAlerta("No se encontraron contactos con ese nombre.", Alert.AlertType.WARNING);
            }
        } else {
            mostrarAlerta("Por favor, ingrese un nombre para buscar.", Alert.AlertType.WARNING);
        }
    }

    // Búsqueda por telefono
    public void buscarPorTelefono() {
        String telefono = txtBusquedaTexto.getText().trim();

        if (!telefono.isEmpty()) {
            Contacto contactoEncontrado = gestionContacto.obtenerContactoPorTelefono(telefono);

            if (contactoEncontrado != null) {
                Dialog<Void> dialog = new Dialog<>();
                dialog.setTitle("Detalles del Contacto");
                dialog.setHeaderText("Información del contacto");

                // Crear un VBox más grande y con color de fondo
                VBox contenido = new VBox(15);
                contenido.setAlignment(Pos.CENTER);
                contenido.setPrefSize(400, 250); // Ajustar el tamaño del VBox
                contenido.setPadding(new Insets(15)); // Agregar padding
                contenido.setStyle("-fx-background-color:    #D4FFF9; -fx-border-radius: 10; -fx-background-radius: 10;"); // Color y bordes redondeados

                Label lblInfo = new Label(
                        "Nombre: " + contactoEncontrado.getNombre() + "\n" +
                                "Apellido: " + contactoEncontrado.getApellido() + "\n" +
                                "Teléfono: " + contactoEncontrado.getTelefono() + "\n" +
                                "Cumpleaños: " + contactoEncontrado.getFechaCumpleanos() + "\n" +
                                "Email: " + contactoEncontrado.getEmail()
                );
                lblInfo.setStyle("-fx-font-family: 'Century Gothic'; -fx-font-size: 14px;");

                // Mostrar la imagen si el contacto tiene una asociada
                ImageView imageView = new ImageView();
                if (contactoEncontrado.getSeleccionarImagen() != null) {
                    imageView.setImage(new Image(contactoEncontrado.getSeleccionarImagen().toURI().toString()));
                    imageView.setFitWidth(120); // Tamaño ajustable
                    imageView.setPreserveRatio(true);
                    imageView.setStyle("-fx-border-color: black; -fx-border-radius: 10;");
                }

                contenido.getChildren().addAll(lblInfo, imageView);

                dialog.getDialogPane().setContent(contenido);
                dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
                dialog.showAndWait();
            } else {
                mostrarAlerta("No se encontró un contacto con ese teléfono.", Alert.AlertType.WARNING);
            }
        } else {
            mostrarAlerta("Por favor, ingrese un teléfono para buscar.", Alert.AlertType.WARNING);
        }
    }

    //Reestablecer tabla
    @FXML
    private void reestablecerTabla(ActionEvent e) {
        txtBusquedaTexto.clear();
        txtBusqueda.setValue(null);

        // Restaurar la lista original
        actualizarContactos();
    }

    //Crear contacto
    public void crearContacto(ActionEvent e) {
        try {
            if (imagenSeleccionada == null) {
                mostrarAlerta("Debe seleccionar una imagen para el contacto.", Alert.AlertType.WARNING);
                return;
            }

            gestionContacto.agregarContacto(
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtFechaCumpleanos.getValue(),
                    txtEmail.getText(),
                    imagenSeleccionada
            );

            // Restablecer imagen
            imagenSeleccionada = null;
            imageView.setImage(null); // Asegurar que la imagen desaparezca de la UI

            limpiarCampos();
            actualizarContactos();
            mostrarAlerta("Contacto creado correctamente", Alert.AlertType.INFORMATION);
        } catch (Exception ex) {
            mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    //Eliminar contacto
    public void eliminarContacto(ActionEvent e) {
        Contacto seleccionado = tablaContactos.getSelectionModel().getSelectedItem();

        if (seleccionado != null) {
            try {
                gestionContacto.eliminarContacto(seleccionado.getId());

                // Limpiar selección y refrescar la tabla
                tablaContactos.getSelectionModel().clearSelection();
                limpiarCampos();

                actualizarContactos();

                mostrarAlerta("Contacto eliminado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
            }
        } else {
            mostrarAlerta("Debe seleccionar un contacto de la tabla", Alert.AlertType.WARNING);
        }
    }

    //Editar contacto
    public void editarContacto(ActionEvent e) {

        if(contactoSeleccionado != null) {
            try {
                gestionContacto.actualizarContacto(
                        contactoSeleccionado.getId(),
                        txtNombre.getText(),
                        txtApellido.getText(),
                        txtTelefono.getText(),
                        txtFechaCumpleanos.getValue(),
                        txtEmail.getText()
                );

                limpiarCampos();
                actualizarContactos();
                mostrarAlerta("Contacto actualizado correctamente", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                mostrarAlerta(ex.getMessage(), Alert.AlertType.ERROR);
            }
        }else{
            mostrarAlerta("Debe seleccionar un contacto de la tabla", Alert.AlertType.WARNING);
        }
    }

    /**
     * Agrega los contactos a la lista observable y los muestra en la tabla
     */
    private void cargarContactos() {
        ContactosObservable.setAll(gestionContacto.getContactos());
        tablaContactos.setItems(ContactosObservable);
    }

    /**
     * Actualiza la lista observable de contactos
     */
    public void actualizarContactos() {

        ContactosObservable.setAll(gestionContacto.getContactos());
    }

    //Seleccionar imagen
    @FXML
    private void seleccionarImagen(ActionEvent e) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        File file = fileChooser.showOpenDialog((Stage) btnSeleccionarImagen.getScene().getWindow());

        if (file != null) {
            imagenSeleccionada = file; // Guardar la imagen seleccionada
            imageView.setImage(new Image(file.toURI().toString())); // Mostrar en ImageView
        }
    }



    /**
     * Muestra una alerta en pantalla
     * @param mensaje Mensaje a mostrar
     * @param tipo Tipo de alerta
     */
    private void mostrarAlerta(String mensaje, Alert.AlertType tipo){
        Alert alert = new Alert(tipo);
        alert.setTitle("Información");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }

    /**
     * Limpiar los campos de texto del formulario
     */
    private void limpiarCampos(){
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtFechaCumpleanos.setValue(null);
        txtEmail.clear();
        imagenSeleccionada = null;

    }
}
