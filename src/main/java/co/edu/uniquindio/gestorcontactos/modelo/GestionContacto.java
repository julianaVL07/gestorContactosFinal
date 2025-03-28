package co.edu.uniquindio.gestorcontactos.modelo;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.io.File;
import java.util.UUID;



public class GestionContacto {
    private final List<Contacto> contactos;

    public GestionContacto() {
        contactos = new ArrayList<>();
    }

    //Validar que el telefono use solo numeros
    private boolean esNumero(String telefono) {
        for (int i = 0; i < telefono.length(); i++) {
            char caracter = telefono.charAt(i);
            if (caracter < 48 || caracter > 57) { // 48 = '0', 57 = '9'
                return false;
            }
        }
        return true;
    }

    //agregar un contacto en la Lista de contactos
    public void agregarContacto(String nombre, String apellido, String telefono, LocalDate fechaCumpleanos, String email, File imagen)throws Exception {
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || fechaCumpleanos == null || email.isEmpty() || imagen == null)
            throw new Exception("Todos los campos son obligatorios");

        if (!email.contains("@")) {
            throw new Exception("El email no es válido");
        }

        if (telefono.length() != 10 || !esNumero(telefono)) {
            throw new Exception("El teléfono no es válido");
        }

        if (obtenerContactoPorTelefono(telefono) != null) {
            throw new Exception("El contacto con este teléfono ya existe");
        }

        Contacto contacto = Contacto.builder()
                .id(UUID.randomUUID().toString())
                .nombre(nombre)
                .apellido(apellido)
                .telefono(telefono)
                .fechaCumpleanos(fechaCumpleanos)
                .email(email)
                .seleccionarImagen(imagen) // Guardar imagen en el contacto
                .build();

        contactos.add(contacto);
    }

    //obtener un contacto por telefono en la Lista de contactos
    public Contacto obtenerContactoPorTelefono(String telefono) {

        for (Contacto contacto : contactos) {
            if (contacto.getTelefono().equals(telefono)) {
                return contacto;
            }
        }
        return null;
    }

    //obtener un contacto en la Lista de contactos por id
    public Contacto obtenerContactoId(String id) {

        for (Contacto contacto : contactos) {
            if (contacto.getId().equals(id)) {
                return contacto;
            }
        }
        return null;
    }


    //obtener un contacto en la Lista de contactos por nombre
    public List<Contacto> obtenerContactosPorNombre(String nombre) {
        List<Contacto> encontrados = new ArrayList<>();

        for (Contacto contacto : contactos) {
            if (contacto.getNombre().toLowerCase().contains(nombre.toLowerCase())) {
                encontrados.add(contacto);
            }
        }
        return encontrados;
    }

    //Eliminar un contacto en la Lista de contactos
    public void eliminarContacto(String id) throws Exception{
        Contacto contactoBuscado = obtenerContactoId(id);

        // Si el contacto no existe, lanzar una excepción
        if(contactoBuscado==null){
            throw new Exception("No existe el id dado");
        }else{
            contactos.remove(contactoBuscado);
        }
    }

    //Actualizar un contacto por Id en la Lista de contactos

    public void actualizarContacto(String id, String nombre, String apellido, String telefono, LocalDate fechaCumpleanos, String email) throws Exception {
        // Buscar el contacto por ID
        Contacto contactoBuscado = obtenerContactoId(id);

        if (contactoBuscado == null) {
            throw new Exception("No existe un contacto con el ID dado");
        }

        // Validaciones
        if (nombre.isEmpty() || apellido.isEmpty() || telefono.isEmpty() || fechaCumpleanos == null || email.isEmpty()) {
            throw new Exception("Todos los campos son obligatorios");
        }

        if (!email.contains("@")) {
            throw new Exception("El email no es válido");
        }

        if (telefono.length() != 10 || !esNumero(telefono)) {
            throw new Exception("El teléfono no es válido");
        }

        // Verificar si el nuevo teléfono ya está en uso por otro contacto
        Contacto contactoExistente = obtenerContactoPorTelefono(telefono);
        if (contactoExistente != null && !contactoExistente.getId().equals(id)) {
            throw new Exception("Ya existe un contacto con el mismo teléfono");
        }

        // Actualizar los datos
        contactoBuscado.setNombre(nombre);
        contactoBuscado.setApellido(apellido);
        contactoBuscado.setTelefono(telefono);
        contactoBuscado.setFechaCumpleanos(fechaCumpleanos);
        contactoBuscado.setEmail(email);
    }

    public ArrayList<String> listarBusqueda() {
        ArrayList<String> busqueda = new ArrayList<>();
        busqueda.add("Telefono");
        busqueda.add("Nombre");

        return busqueda;
    }

    public List<Contacto> getContactos() {
        return contactos;
    }

    /**
     * Método para seleccionar una imagen y asignarla a un contacto.
     *
     * @param stage     El Stage (ventana) desde donde se abre el FileChooser.
     * @param imageView El ImageView donde se mostrará la imagen seleccionada.
     * @param contacto  El contacto al que se le asignará la imagen.
     */


    public void seleccionarImagen(Stage stage, ImageView imageView, Contacto contacto) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Imagen");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg", "*.gif"));

        // Abrir el diálogo para seleccionar un archivo
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            contacto.setSeleccionarImagen(file); // Asignar la imagen al contacto
            imageView.setImage(new Image(file.toURI().toString())); // Mostrar la imagen en el ImageView
        }
    }
}