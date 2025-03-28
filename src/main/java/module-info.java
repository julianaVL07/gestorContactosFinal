module co.edu.uniquindio.gestorcontactos {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;

    opens co.edu.uniquindio.gestorcontactos.controladores to javafx.fxml;
    exports co.edu.uniquindio.gestorcontactos.controladores;

    exports co.edu.uniquindio.gestorcontactos to javafx.graphics;
}