package com.example.Fidu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FiduApplication extends Application {

	public static ConfigurableApplicationContext context;
	public static void main(String[] args) {
		launch();
		SpringApplication.run(FiduApplication.class, args);

	}



	@Override
	public void start(Stage stage) throws Exception {
		context = SpringApplication.run(FiduApplication.class);
		FXMLLoader fxml= new FXMLLoader(getClass().getResource("/com/example/Fidu/FiduApplication.fxml"));
		fxml.setControllerFactory(context::getBean);

		Scene scene= new Scene(fxml.load());
		stage.setTitle("Crud Fiducia");
		stage.setScene(scene);
		stage.show();

	}

}
