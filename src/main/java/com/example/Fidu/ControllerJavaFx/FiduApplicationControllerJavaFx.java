package com.example.Fidu.ControllerJavaFx;

import com.example.Fidu.Service.PersonaService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
@Component
public class FiduApplicationControllerJavaFx {
    @Autowired
    PersonaService personaService;

    @Autowired
    private ConfigurableApplicationContext context;

    @FXML
    private Button personas, negocios, obligaciones;

    @FXML
    public void personas() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Personas.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) personas.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Personas");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    public void negocios() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Negocios.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) negocios.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Negocios");
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    public void obligaciones() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Obligaciones.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) obligaciones.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Obligaciones");
        stage.setScene(scene);
        stage.show();

    }
}