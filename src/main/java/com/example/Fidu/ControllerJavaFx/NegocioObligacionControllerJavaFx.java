package com.example.Fidu.ControllerJavaFx;

import com.example.Fidu.Entity.Negocio;
import com.example.Fidu.Entity.Obligacion;
import com.example.Fidu.Service.NegocioService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

@Component
public class NegocioObligacionControllerJavaFx implements Initializable {

    @Autowired
    private ConfigurableApplicationContext context;
    @Autowired
    private NegocioControllerJavaFx negocioControllerJavaFx;
    @Autowired
    private NegocioService negocioService;

    @FXML
    private TableView<Obligacion> listaObligacionesNegocio;
    @FXML
    private TableColumn<Obligacion, Long> colObligacionesId;
    @FXML
    private TableColumn<Obligacion, Float> colObligacionesMonto;
    @FXML
    private TableColumn<Obligacion, String> colObligacionesDescripcion;
    @FXML
    private TableColumn<Obligacion, Date> colObligacionesFechaF;

    @FXML
    private TextField nombreN, descripcionNegocio, fechaFn, fechaIn, idObligacionAgg;
    @FXML
    private Button aggO, guardarO, cancelarO, backNO;

    private Long negocioId;
    private Negocio negocio;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        colObligacionesId.setCellValueFactory(new PropertyValueFactory<>("obligacionId"));
        colObligacionesMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colObligacionesDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        colObligacionesFechaF.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        setNegocioId();
        info();
        agg(false);
        cargarDatosNegocios();


    }
    @FXML
    public void inicio() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/Fidu/FiduApplication.fxml"));
        loader.setControllerFactory(context::getBean);

        // Obtener la escena actual y el stage
        Stage stage = (Stage) backNO.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setTitle("Inicio");
        stage.setScene(scene);
        stage.show();

    }

    public void agg(boolean visible){
        aggO.setVisible(!visible);
        idObligacionAgg.setVisible(visible);
        guardarO.setVisible(visible);
        cancelarO.setVisible(visible);
    }
    public void guardar(){
        negocioService.addObligacionToNegocio(negocio.getNegocioId(),Long.valueOf(idObligacionAgg.getText()));
        idObligacionAgg.clear();
        agg(false);
        cargarDatosNegocios();
    }
    public void cancelar(){
        idObligacionAgg.clear();
        agg(false);
        cargarDatosNegocios();
    }

    public void agregar(){
        agg(true);
    }

    public void setNegocioId() {
        this.negocioId = negocioControllerJavaFx.item().getNegocioId();
        this.negocio = negocioControllerJavaFx.item();
        System.out.println(negocioId);
    }

    private void cargarDatosNegocios() {

        // Obtener el conjunto de oblicaciones
        Set<Obligacion> obligacionesSet = negocioService.getObligacionesByNegocioId(negocio.getNegocioId());
        // Convertir el conjunto a una lista
        List<Obligacion> obligacionesList = new ArrayList<>(obligacionesSet);
        // Actualizar la vista con la lista de negocios
        listaObligacionesNegocio.getSelectionModel().clearSelection();  // Desactiva la selección automática
        listaObligacionesNegocio.setItems(FXCollections.observableList(obligacionesList));

    }
    public void info(){
        negocio= negocioService.findById(negocioId);
        nombreN.setText(negocio.getNombre());
        descripcionNegocio.setText(negocio.getDescripcion());
        fechaFn.setText(negocio.getFechaFin().toString());
        fechaIn.setText(negocio.getFechaInicio().toString());

    }



}