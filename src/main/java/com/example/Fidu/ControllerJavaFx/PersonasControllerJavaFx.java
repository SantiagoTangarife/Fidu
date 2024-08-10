package com.example.Fidu.ControllerJavaFx;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import com.example.Fidu.Entity.Persona;
import com.example.Fidu.Service.PersonaService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class PersonasControllerJavaFx implements Initializable {

    @Autowired
    PersonaService personaService;
    @Autowired
    private ConfigurableApplicationContext context;
    private Long id;
    private Persona item;

    @FXML
    private TableView<Persona> listaPersonas;

    @FXML
    private TableColumn<Persona, Long> colPersonaId;
    @FXML
    private TableColumn<Persona, String> colNombre;
    @FXML
    private TableColumn<Persona, String> colApellido;
    @FXML
    private TableColumn<Persona, String> colTipoDocumento;
    @FXML
    private TableColumn<Persona, Long> colNumeroDocumento;

    @FXML
    private TextField numeroDocumento, nombreAdd,apellidoAdd,numeroDocAdd;
    @FXML
    private Button buscar,guardar,cancelar,agg,actualizar,eliminar,backPe,findNegocio;
    @FXML
    private ComboBox<String> tipoDoc;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colPersonaId.setCellValueFactory(new PropertyValueFactory<>("personaId"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(new PropertyValueFactory<>("apellido"));
        colTipoDocumento.setCellValueFactory(new PropertyValueFactory<>("tipoDocumento"));
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<>("numeroDocumento"));

        listaPersonas();
        // Inicializa el ComboBox con opciones de tipo de documento
        tipoDoc.setItems(FXCollections.observableArrayList("CC", "NIT"));

    }
    @FXML
    private void openNegociosView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Persona_Negocio.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) findNegocio.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        String mensaje = "Negocios de "+item.getNombre()+ " "+ item.getApellido();
        stage.setTitle(mensaje);
        stage.setScene(scene);
        // Modificar los campos en OtraVentanaController

        stage.show();
    }
    @FXML
    public void inicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/FiduApplication.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) backPe.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();

    }


    @FXML
    private void buscarPersona() {
        String numeroD = numeroDocumento.getText();//capturo e numero de documento
        if (numeroD != null && !numeroD.isEmpty()) {//evaluo que no sea nulo
            Optional<Persona> persona = Optional.ofNullable(personaService.getPersonaById(Long.valueOf(numeroD)));//lo combierto a long
            if (persona.isPresent()) {
                listaPersonas.setItems(FXCollections.observableArrayList(persona.get()));
            } else {
                listaPersonas.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Informacion","Persona no encontrada.");
                System.out.println("Persona no encontrada.");
            }
        } else {
            // Si no se ha ingresado un número de documento, mostrar todas las personas
            listaPersonas.setItems(FXCollections.observableList(personaService.getAllPersonas()));
        }
    }

    @FXML
    private void toAdd(){
        limpiarCampos();
        toNormal(false);
        toAgg(true);
    }

    @FXML
    private void agregarPersona() {
        if (validarCampos()) {
            String nombre = nombreAdd.getText();
            String apellido = apellidoAdd.getText();
            String tipoDocumento = tipoDoc.getValue();
            String numeroDocumento = numeroDocAdd.getText();

            Persona persona = new Persona();
            persona.setNombre(nombre);
            persona.setApellido(apellido);
            persona.setTipoDocumento(tipoDocumento);
            persona.setNumeroDocumento(Long.valueOf(numeroDocumento));
            personaService.createPersona(persona);
            limpiarCampos();
            listaPersonas();


        }
    }

    @FXML
    private void cancelarPersona(){
        limpiarCampos();
        toNormal(true);
    }

    @FXML
    public void update(){
        long provicional=item.getNumeroDocumento();
        item.setNombre(nombreAdd.getText());
        item.setApellido(apellidoAdd.getText());
        item.setTipoDocumento(tipoDoc.getValue());
        item.setNumeroDocumento(Long.valueOf(numeroDocAdd.getText()));
        personaService.updatePersona(provicional,item);
        limpiarCampos();
        listaPersonas();

    }


    @FXML
    public void delete(){
        personaService.deletePersona(item.getPersonaId());
        limpiarCampos();
        listaPersonas();
    }
    private void toUpdate(boolean visible) {
        eliminar.setVisible(visible);
        findNegocio.setVisible(visible);
        actualizar.setVisible(visible);
        if (item != null) {
            actualizar.setVisible(visible);
            eliminar.setVisible(visible);
            findNegocio.setVisible(visible);
            toAgg(visible);
            guardar.setVisible(false);
            nombreAdd.setText(item.getNombre());
            apellidoAdd.setText(item.getApellido());
            numeroDocAdd.setText(item.getNumeroDocumento().toString());
            tipoDoc.setValue(item.getTipoDocumento());

        } else {
            mostrarAlerta("Cargando datos...", "Espera un momento por Favor.");
        }
    }
    //ocultar botones cuando vaya a agg info
    private void toNormal(boolean visible){
        toAgg(false);
        toUpdate(false);
        buscar.setVisible(visible);
        numeroDocumento.setVisible(visible);
        agg.setVisible(visible);

    }
    //ocultar botones por defecto
    private void toAgg(boolean visible){
        nombreAdd.setVisible(visible);
        apellidoAdd.setVisible(visible);
        numeroDocAdd.setVisible(visible);
        tipoDoc.setVisible(visible);
        guardar.setVisible(visible);
        cancelar.setVisible(visible);
        agg.setVisible(!visible);



    }

    //no se pueden datos vacios o con espacios, ademas que alfanumerico solo se permite en el DNI
    private boolean validarCampos() {
        String nombre = nombreAdd.getText();
        String apellido = apellidoAdd.getText();
        String tipoDocumento = tipoDoc.getValue();
        String numeroDocumento = numeroDocAdd.getText();

        if (nombre == null || nombre.isEmpty() ||
                apellido == null || apellido.isEmpty() ||
                tipoDocumento == null || tipoDocumento.isEmpty() ||
                numeroDocumento == null || numeroDocumento.isEmpty()) {
            mostrarAlerta("Error de validación", "Todos los campos deben estar llenos.");
            return false;
        }

        if (!tipoDocumento.equals("DNI") && !numeroDocumento.matches("\\d+")) {
            mostrarAlerta("Error de validación", "El número de documento no debe contener letras (excepto si es DNI y dichas opcion aun no se ha implementado).");
            return false;
        }

        return true;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void limpiarCampos() {
        nombreAdd.clear();
        apellidoAdd.clear();
        numeroDocAdd.clear();
        tipoDoc.setValue(null);
    }

    public Persona item(){
        return personaService.getPersonaById(id);
    }



    public void listaPersonas() {
        toNormal(true);
        listaPersonas.getSelectionModel().clearSelection();  // Desactiva la selección automática
        listaPersonas.setItems(FXCollections.observableList(personaService.getAllPersonas()));

        listaPersonas.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue)->
        {
            if (newValue != null){
                id= newValue.getNumeroDocumento();
                item=item();
                //asigno los campos del item seleccionado
                toUpdate(true);


            }


        });
    }





}
