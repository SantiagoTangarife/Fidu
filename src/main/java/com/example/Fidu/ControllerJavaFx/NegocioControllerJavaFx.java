package com.example.Fidu.ControllerJavaFx;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Service.NegocioService;
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
import java.sql.Date;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class NegocioControllerJavaFx implements Initializable {
    @Autowired
    NegocioService negocioService;
    @Autowired
    private ConfigurableApplicationContext context;
    private Long id;
    private Negocio item;

    @FXML
    private TableView<Negocio> listaNegocios;

    @FXML
    private TableColumn<Negocio, Long> colNegociosId;
    @FXML
    private TableColumn<Negocio, String> colNegociosNombre;
    @FXML
    private TableColumn<Negocio, String> colNegociosDescripcion;
    @FXML
    private TableColumn<Negocio, Date> colNegociosFechaI;
    @FXML
    private TableColumn<Negocio, Date> colNegociosFechaF;
    @FXML
    private TextField idNegocioBusqueda, nombreAddN,descripcionAddN,idNegocio;
    @FXML
    private DatePicker fechaI,fechaF;



    @FXML
    private Button buscarN, guardarN, cancelarN, aggN,actualizarN,eliminarN,backNe,findObligacion;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colNegociosId.setCellValueFactory(new PropertyValueFactory<>("negocioId"));
        colNegociosNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNegociosDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colNegociosFechaI.setCellValueFactory(new PropertyValueFactory<>("fechaInicio"));
        colNegociosFechaF.setCellValueFactory(new PropertyValueFactory<>("fechaFin"));

        listaNegocios();


    }
    @FXML
    private void openObligacionesView() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/Negocio_Obligacion.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) findObligacion.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        String mensaje = "Obligaciones de "+item.getNombre();
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
        Stage stage = (Stage) backNe.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();

    }
    @FXML
    private void buscarNegocio() {
        String numeroD = idNegocioBusqueda.getText();//capturo id negocio
        if (numeroD != null && !numeroD.isEmpty()) {//evaluo que no sea nulo
            Optional<Negocio> negocio = Optional.ofNullable(negocioService.findById(Long.valueOf(numeroD)));//lo combierto a long
            if (negocio.isPresent()) {
                listaNegocios.setItems(FXCollections.observableArrayList(negocio.get()));
            } else {
                listaNegocios.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Verificar Datos","Negocio no encontrado");
                System.out.println("Negocio no encontrado.");
            }
        } else {
            // Si no se ha ingresado un número de documento, mostrar todas las personas
            listaNegocios.setItems(FXCollections.observableList(negocioService.findAll()));
        }
    }
    @FXML
    private void toAdd(){
        limpiarCampos();
        toNormal(false);
        toAgg(true);
    }

    @FXML
    private void agregarNegocio() {
        if (validarCampos()) {
            Long idnegocio= Long.valueOf(idNegocio.getText());
            String nombre = nombreAddN.getText();
            String descripcion = descripcionAddN.getText();
            Date fechaIn = Date.valueOf(fechaI.getValue());
            Date fechaFn = Date.valueOf(fechaF.getValue());

            Negocio negocio = new Negocio();
            negocio.setNegocioId(idnegocio);
            negocio.setNombre(nombre);
            negocio.setDescripcion(descripcion);
            negocio.setFechaInicio(fechaIn);
            negocio.setFechaFin(fechaFn);

            negocioService.save(negocio);
            limpiarCampos();
            listaNegocios();
        }
    }

    @FXML
    private void cancelarNegocio(){
        limpiarCampos();
        toNormal(true);
    }

    @FXML
    public void update(){
        long provicional=item.getNegocioId();
        item.setNombre(nombreAddN.getText());
        item.setNegocioId(provicional);//--> el id de negocio nunca se modifica
        item.setDescripcion(descripcionAddN.getText());
        item.setFechaFin(Date.valueOf(fechaF.getValue()));
        item.setFechaInicio(Date.valueOf(fechaF.getValue()));
        negocioService.updateNegocio(provicional,item);
        limpiarCampos();
        listaNegocios();

    }

    @FXML
    public void delete(){
        negocioService.deleteById(item.getNegocioId());
        limpiarCampos();
        listaNegocios();
    }

    private void toUpdate(boolean visible) {
        idNegocio.setEditable(!visible);
        eliminarN.setVisible(visible);
        findObligacion.setVisible(visible);
        actualizarN.setVisible(visible);
        if (item != null) {
            actualizarN.setVisible(visible);
            eliminarN.setVisible(visible);
            findObligacion.setVisible(visible);
            toAgg(visible);
            guardarN.setVisible(false);
            nombreAddN.setText(item.getNombre());
            idNegocio.setText(item.getNegocioId().toString());
            descripcionAddN.setText(item.getDescripcion());
            fechaI.setValue(item.getFechaInicio().toLocalDate());
            fechaF.setValue(item.getFechaFin().toLocalDate());

        } else {
            mostrarAlerta("Cargando datos...", "Espera un momento por Favor.");
        }
    }
    //ocultar botones por defecto
    private void toNormal(boolean visible){
        toAgg(false);
        toUpdate(false);
        buscarN.setVisible(visible);
        idNegocioBusqueda.setVisible(visible);
        aggN.setVisible(visible);

    }

    //ocultar botones cuando vaya a agg info
    private void toAgg(boolean visible){
        idNegocio.setVisible(visible);
        nombreAddN.setVisible(visible);
        descripcionAddN.setVisible(visible);
        fechaI.setVisible(visible);
        fechaF.setVisible(visible);
        guardarN.setVisible(visible);
        cancelarN.setVisible(visible);
        aggN.setVisible(!visible);

    }

    //no se pueden datos vacios o con espacios, ademas que alfanumerico solo se permite en el DNI
    private boolean validarCampos() {
        // Intentar convertir idNegocio a Long
        Long idnegocio;
        try {
            idnegocio = Long.valueOf(idNegocio.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El ID de negocio debe contener solo números.");
            return false;
        }

        // Obtener los demás valores de los campos
        String nombre = nombreAddN.getText();
        String descripcion = descripcionAddN.getText();
        LocalDate fechaIni =fechaI.getValue();
        LocalDate fechaFni = fechaF.getValue();


        // Validar si todos los campos están llenos
        if (nombre == null || nombre.isEmpty() ||
                descripcion == null || descripcion.isEmpty()||
                fechaFni==null ||fechaIni==null) {
            mostrarAlerta("Error de validación", "Todos los campos deben estar llenos.");
            return false;
        }

        Date fechaIn = Date.valueOf(fechaI.getValue());
        Date fechaFn = Date.valueOf(fechaF.getValue());
        // Validar si la fecha de inicio es posterior a la fecha de fin
        if (fechaIn.after(fechaFn)) {
            mostrarAlerta("Error de validación", "La fecha de inicio no puede ser posterior a la fecha de fin.");
            return false;
        }
        if(negocioService.findById(idnegocio)!=null){//el negocio ya existe
            mostrarAlerta("Error de validación", "El ID ingresado ya esta en Uso.");
            return false;
        }

        // Si todas las validaciones pasan, retornar true
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
        idNegocio.clear();
        nombreAddN.clear();
        descripcionAddN.clear();
        fechaI.setValue(null);
        fechaF.setValue(null);
    }

    public Negocio item(){
        return negocioService.findById(id);
    }

    public void listaNegocios() {
        toNormal(true);
        listaNegocios.getSelectionModel().clearSelection();  // Desactiva la selección automática
        listaNegocios.setItems(FXCollections.observableList(negocioService.findAll()));

        listaNegocios.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue)->
        {
            if (newValue != null){
                id= newValue.getNegocioId();
                item=item();
                //asigno los campos del item seleccionado
                toUpdate(true);
            }
        });

    }


}
