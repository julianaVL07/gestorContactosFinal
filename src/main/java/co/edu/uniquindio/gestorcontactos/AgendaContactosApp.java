package co.edu.uniquindio.gestorcontactos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AgendaContactosApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(AgendaContactosApp.class.getResource("agendaContacto.fxml"));
        Parent parent = loader.load();

        Scene scene = new Scene(parent, 1000, 550);
        stage.setScene(scene);
        stage.setTitle("Gestión Contactos");
        //stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch(AgendaContactosApp.class, args);
    }

}
