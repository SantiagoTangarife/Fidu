package com.example.Fidu.ControllerJavaFx;

import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Service.ObligacionService;
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
public class ObligacionControllerJavaFx implements Initializable {

    @Autowired
    ObligacionService obligacionService;
    @Autowired
    private ConfigurableApplicationContext context;
    private Long id;
    private Obligacion item;

    @FXML
    private TableView<Obligacion> listaObligaciones;

    @FXML
    private TableColumn<Obligacion, Long> colObligacionesId;
    @FXML
    private TableColumn<Obligacion, Float> colObligacionesMonto;
    @FXML
    private TableColumn<Obligacion, String> colObligacionesDescripcion;

    @FXML
    private TableColumn<Obligacion, Date> colObligacionesFechaF;
    @FXML
    private TextField idObligacionBusqueda, montoAddO, descripcionAddO, idObligacion;
    @FXML
    private DatePicker fechaF;



    @FXML
    private Button buscarO, guardarO, cancelarO, aggO, actualizarO, eliminarO,backOb;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colObligacionesId.setCellValueFactory(new PropertyValueFactory<>("obligacionId"));
        colObligacionesMonto.setCellValueFactory(new PropertyValueFactory<>("Monto"));
        colObligacionesDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colObligacionesFechaF.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));

        listaObligaciones();


    }

    @FXML
    public void inicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/FiduApplication.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) backOb.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    private void buscarObligacion() {
        String numeroD = idObligacionBusqueda.getText();//capturo id Obligacion
        if (numeroD != null && !numeroD.isEmpty()) {//evaluo que no sea nulo
            Optional<Obligacion> obligacion = Optional.ofNullable(obligacionService.getObligacionById(Long.valueOf(numeroD)));//lo combierto a long
            if (obligacion.isPresent()) {
                listaObligaciones.setItems(FXCollections.observableArrayList(obligacion.get()));
            } else {
                listaObligaciones.setItems(FXCollections.emptyObservableList());
                mostrarAlerta("Verificar Datos","Obligacion no encontrada");
                System.out.println("Obligacion no encontrada.");
            }
        } else {
            // Si no se ha ingresado un número de documento, mostrar todas las obligaciones
            listaObligaciones.setItems(FXCollections.observableList(obligacionService.getAllObligaciones()));
        }
    }
    @FXML
    private void toAdd(){
        limpiarCampos();
        toNormal(false);
        toAgg(true);
    }

    @FXML
    private void agregarObligacion() {
        if (validarCampos()) {
            Long idobligacion = Long.valueOf(idObligacion.getText());
            Float monto = Float.valueOf(montoAddO.getText());
            String descripcion = descripcionAddO.getText();
            Date fechaFn = Date.valueOf(fechaF.getValue());

            Obligacion obligacion = new Obligacion();
            obligacion.setObligacionId(idobligacion);
            obligacion.setMonto(monto);
            obligacion.setDescripcion(descripcion);
            obligacion.setFechaVencimiento(fechaFn);

            obligacionService.createObligacion(obligacion);
            limpiarCampos();
            listaObligaciones();
        }
    }


    @FXML
    private void cancelarObligacion(){
        limpiarCampos();
        toNormal(true);
    }

    @FXML
    public void update(){
        long provicional=item.getObligacionId();
        item.setMonto(Float.valueOf(montoAddO.getText()));
        item.setObligacionId(provicional);//--> el id de Obligacion nunca se modifica
        item.setDescripcion(descripcionAddO.getText());
        item.setFechaVencimiento(Date.valueOf(fechaF.getValue()));
        obligacionService.updateObligacion(provicional,item);
        limpiarCampos();
        listaObligaciones();

    }

    @FXML
    public void delete(){
        obligacionService.deleteObligacion(item.getObligacionId());
        limpiarCampos();
        listaObligaciones();
    }

    private void toUpdate(boolean visible) {
        idObligacion.setEditable(!visible);
        eliminarO.setVisible(visible);
        actualizarO.setVisible(visible);
        if (item != null) {
            actualizarO.setVisible(visible);
            eliminarO.setVisible(visible);
            toAgg(visible);
            guardarO.setVisible(false);
            montoAddO.setText(String.valueOf(item.getMonto()));
            idObligacion.setText(item.getObligacionId().toString());
            descripcionAddO.setText(item.getDescripcion());
            fechaF.setValue(item.getFechaVencimiento().toLocalDate());

        } else {
            //los id's siempre van a existir si aparecen en la interfaz grafica, esta alerta aparecera cuando hay cargas perezosas
            mostrarAlerta("Cargando datos...", "Espera un momento por Favor.");
        }
    }
    //ocultar botones por defecto
    private void toNormal(boolean visible){
        toAgg(false);
        toUpdate(false);
        buscarO.setVisible(visible);
        idObligacionBusqueda.setVisible(visible);
        aggO.setVisible(visible);

    }

    //ocultar botones cuando vaya a agg info
    private void toAgg(boolean visible){
        idObligacion.setVisible(visible);
        montoAddO.setVisible(visible);
        descripcionAddO.setVisible(visible);
        fechaF.setVisible(visible);
        guardarO.setVisible(visible);
        cancelarO.setVisible(visible);
        aggO.setVisible(!visible);

    }

    //no se pueden datos vacios o con espacios, ademas que alfanumerico solo se permite en el DNI
    private boolean validarCampos() {
        // Intentar convertir idObligacion a Long
        Long idobligacion;
        try {
            idobligacion = Long.valueOf(idObligacion.getText());
        } catch (NumberFormatException e) {
            mostrarAlerta("Error de validación", "El ID de Obligacion debe contener solo números.");
            return false;
        }

        // Obtener los demás valores de los campos
        String monto = montoAddO.getText();
        String descripcion = descripcionAddO.getText();
        LocalDate fechaFni = fechaF.getValue();


        // Validar si todos los campos están llenos
        if (monto == null || monto.isEmpty() || descripcion == null || descripcion.isEmpty() || fechaFni == null) {
            mostrarAlerta("Error de validación", "Todos los campos deben estar llenos.");
            return false;
        }
        if(obligacionService.getObligacionById(idobligacion)!=null){//la obligacion ya existe
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
        idObligacion.clear();
        montoAddO.clear();
        descripcionAddO.clear();
        fechaF.setValue(null);
    }

    public Obligacion item(){
        return obligacionService.getObligacionById(id);
    }

    public void listaObligaciones() {
        toNormal(true);
        listaObligaciones.getSelectionModel().clearSelection();  // Desactiva la selección automática
        listaObligaciones.setItems(FXCollections.observableList(obligacionService.getAllObligaciones()));

        listaObligaciones.getSelectionModel().selectedItemProperty().addListener((obs, old, newValue)->
        {
            if (newValue != null){
                id= newValue.getObligacionId();
                item=item();
                //asigno los campos del item seleccionado
                toUpdate(true);
            }
        });

    }


}
