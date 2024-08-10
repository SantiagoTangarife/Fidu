package com.example.Fidu.ControllerJavaFx;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Service.NegocioService;
import com.example.Fidu.Service.PersonaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@Component
public class PersonaNegocioControllerJavafx implements Initializable {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private PersonasControllerJavaFx personasControllerJavaFx;
    @Autowired
    private PersonaService personaService;

    @FXML
    private TableView<Negocio> listaNegociosPersona;
    @FXML
    private TableColumn<Negocio, Long> colNegociosId;
    @FXML
    private TableColumn<Negocio, String> colNegociosNombre;
    @FXML
    private TableColumn<Negocio, String> colNegociosDescripcion;
    @FXML
    private TableColumn<Negocio, String> colNegociosFechaI;
    @FXML
    private TableColumn<Negocio, String> colNegociosFechaF;

    @FXML
    private TextField NombreP, ApellidoP, NumeroDocP, TipoDocP, idNegocioAgg;
    @FXML
    private Button aggN,guardarN,cancelarN,backNP,descargar;

    private Long personaId;
    private Persona usuario;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNegociosId.setCellValueFactory(new PropertyValueFactory<>("negocioId"));
        colNegociosNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNegociosDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNegociosFechaI.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colNegociosFechaF.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));
        setPersonaId();
        info();
        agg(false);
        cargarDatosNegocios();


    }
    @FXML
    public void inicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Personas.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) backNP.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Personas");
        stage.setScene(scene);
        stage.show();

    }

    public void exportar(){

        List<String> dato=personaService.exportar(usuario);
        mostrarAlerta(dato.get(0), dato.get(1));

    }
    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert;
        if(titulo.equals("Exito")){
            alert= new Alert(Alert.AlertType.INFORMATION);}
        else {
           alert = new Alert(Alert.AlertType.ERROR);}


        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public void agg(boolean visible){
        aggN.setVisible(!visible);
        idNegocioAgg.setVisible(visible);
        guardarN.setVisible(visible);
        cancelarN.setVisible(visible);
    }
    public void guardar(){
        personaService.addNegocioToPersona(Long.valueOf(NumeroDocP.getText()),Long.valueOf(idNegocioAgg.getText()));
        idNegocioAgg.clear();
        agg(false);
        cargarDatosNegocios();
    }
    public void cancelar(){
        idNegocioAgg.clear();
        agg(false);
        cargarDatosNegocios();
    }

    public void agregar(){
        agg(true);
    }

    public void setPersonaId() {
        this.personaId = personasControllerJavaFx.item().getNumeroDocumento();
        this.usuario=personasControllerJavaFx.item();
        System.out.println(personaId);
    }

    private void cargarDatosNegocios() {

        // Obtener el conjunto de negocios
        Set<Negocio> negociosSet = personaService.getNegociosByPersonaId(usuario.getPersonaId());
        // Convertir el conjunto a una lista
        List<Negocio> negociosList = new ArrayList<>(negociosSet);
        // Actualizar la vista con la lista de negocios
        listaNegociosPersona.getSelectionModel().clearSelection();  // Desactiva la selección automática
        listaNegociosPersona.setItems(FXCollections.observableList(negociosList));

    }
    public void info(){
        Persona persona=personaService.getPersonaById(personaId);
        NombreP.setText(persona.getNombre());
        ApellidoP.setText(persona.getApellido());
        NumeroDocP.setText(personaId.toString());
        TipoDocP.setText(persona.getTipoDocumento());

    }



}