package controllerfx;

import dao.ExameDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Exames;

/**
 * FXML Controller class
 *
 * @author estagio
 */
public class FXMLExamesController implements Initializable {

    @FXML
    private Button btnNovo;
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnAtualizar;
    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnLimpar;
    @FXML
    private TableView<Exames> tabelaExame;
    @FXML
    private TableColumn<Exames, Integer> columnCodigo;
    @FXML
    private TableColumn<Exames, String> columnDescricao;
    @FXML
    private TableColumn<Exames, Double> columnValor;
    @FXML
    private TextField txtCodigo;
    @FXML
    private TextField txtDescricao;
    @FXML
    private TextField txtValor;
    @FXML
    private Label lblMensagem;

    private List<Exames> exames;
    private ObservableList<Exames> examesfx;
    private ExameDAO examesDao = null;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        examesDao = new ExameDAO();
        preencheTabelaExames();
        tabelaExame.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selecionaItemTabela(newValue));
    }

    @FXML
    private void novoExame() {
        limpaExame();
    }

    @FXML
    private void criarExame() {
        Exames e = new Exames();
        String exame = txtDescricao.getText();
        Double valor = Double.valueOf(txtValor.getText());

        if (!exame.trim().isEmpty() && valor != null) {
            e.setId(Integer.parseInt(txtCodigo.getText()));
            e.setExmDescricao(exame);
            e.setExmValor(valor);

            examesDao.atualizar(e);
            exames.add(e);
            tabelaExame.getItems().setAll(exames);
            atualizaTabela();
            limpaExame();
        } else {
            if (exame.trim().isEmpty()) {
                txtDescricao.setText("Preencha a Descrição!");
                txtDescricao.requestFocus();
            }
            if (valor == null) {
                txtValor.setText("Preencha o valor");
                txtValor.requestFocus();
            }
            alertas("Atenção", "Preencha os campos vazios!");
        }
    }

    @FXML
    private void atualizarExame() {
        Exames e = tabelaExame.getSelectionModel().getSelectedItem();
        if (e != null) {
            e.setId(Integer.parseInt(txtCodigo.getText()));
            e.setExmDescricao(txtDescricao.getText());
            e.setExmValor(Double.parseDouble(txtValor.getText()));
            examesDao.atualizar(e);
              tabelaExame.getItems().setAll(exames);
            atualizaTabela();
            limpaExame();
        } else {
            alertas("Atenção", "Selecione um exame");
        }
    }

    @FXML
    private void excluirExame() {
        Exames e = tabelaExame.getSelectionModel().getSelectedItem();
        if (e != null) {
            examesDao.apagar(e.getId());
            atualizaTabela();
        } else {
            alertas("Atenção", "Selecione um exame");
        }
    }

    @FXML
    private void limpaExame() {
        txtCodigo.clear();
        txtDescricao.clear();
        txtValor.clear();
        tabelaExame.getSelectionModel().clearSelection();
    }

    private void preencheTabelaExames() {
        atualizaTabela();

        columnCodigo.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnDescricao.setCellValueFactory(new PropertyValueFactory<>("exmDescricao"));
        columnValor.setCellValueFactory(new PropertyValueFactory<>("exmValor"));
    }

    private void atualizaTabela() {
        exames = examesDao.buscarTodos();
        examesfx = FXCollections.observableArrayList(exames);
        tabelaExame.setItems(examesfx);
    }

    private void selecionaItemTabela(Exames e) {
        if (e != null) {
            txtCodigo.setText(String.valueOf(e));
            txtDescricao.setText(e.getExmDescricao());
            txtValor.setText(String.valueOf(e.getExmValor()));
        } else {
            limpaExame();
        }
    }

    public void alertas(String titulo, String frase) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(frase);
        alert.showAndWait();
    }

}
