package co.edu.uniquindio.gestorcontactos.modelo;
import java.io.File;
import java.time.LocalDate;
import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString

public class Contacto {
    private String id;
    private String nombre;
    private String apellido;
    private String telefono;
    private LocalDate fechaCumpleanos;
    private String email;
    private File seleccionarImagen;// Nuevo campo para almacenar la imagen

}
