package controllerfx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javax.persistence.EntityManager;

/**
 *
 * @author estagio
 */
public class FXMLPrincipalController implements Initializable {

    EntityManager em;

    @FXML
    private Menu menuConsulta;

    @FXML
    private Menu menuCliente;

    @FXML
    private Menu menuExame;

    @FXML
    private MenuBar menuPrincipal;

    @FXML
    private Menu menuTecnico;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Menu menuInicio;
    @FXML
    private MenuItem itemSair;
    @FXML
    private MenuItem itemClientes;
    @FXML
    private MenuItem itemConsultas;
    @FXML
    private MenuItem itemExames;
    @FXML
    private MenuItem itemTecnicos;
    @FXML
    private MenuItem itemRelatorio;

    @FXML
    void menuInicio(ActionEvent event) {

    }

    @FXML
    void menuClientes(ActionEvent event) {

    }

    @FXML
    void menuConsultas(ActionEvent event) {

    }

    @FXML
    void menuExames(ActionEvent event) {

    }

    @FXML
    void menuTecnicos(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
   
    }

    @FXML
    private void sair(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void itemCliente(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/telas/FXMLClientes.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Pacientes");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void itemConsulta(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/telas/FXMLConsultas.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Consultas");
        stage.setScene(scene);
        stage.show();
        
    }

    @FXML
    private void itemExame(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/telas/FXMLExames.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Exames");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void itemTecnico(ActionEvent event) throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/telas/FXMLTecnicos.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Tecnicos");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void carregarGrafico() throws IOException {
        Parent parent = FXMLLoader.load(getClass().getResource("/telas/GraficosConsultasFXML.fxml"));
        Scene scene = new Scene(parent);
        Stage stage = new Stage();
        stage.setTitle("Relatório de Consultas");
        stage.setScene(scene);
        stage.show();
    }
    

}
