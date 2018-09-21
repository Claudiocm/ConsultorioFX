package controllerfx;

import dao.TecnicoDAO;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Tecnicos;

public class FXMLTecnicosController implements Initializable {

    @FXML
    private TableColumn<Tecnicos, Integer> columnId;
    @FXML
    private TableColumn<Tecnicos, String> columnNome;
    @FXML
    private TableColumn<Tecnicos, String> columnTelefone;
    @FXML
    private TableColumn<Tecnicos, String> columnEspecialidade;
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
    private TextField txtID;
    @FXML
    private TextField txtNome;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtExpecialidade;

    private List<Tecnicos> tecnicos;
    private ObservableList<Tecnicos> tecnicosfx;
    private TecnicoDAO tecnicoDao = null;
    @FXML
    private TableView<Tecnicos> tabelaTecnicos;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tecnicoDao = new TecnicoDAO();
        preencheTabela();
        tabelaTecnicos.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selecionaItemTabela(newValue));
    }

    @FXML
    private void novoTecnico() {
        limparTecnicos();
    }

    @FXML
    private void criarTecnico() {
        Tecnicos t = new Tecnicos();
        String nome = txtNome.getText();
        String especialidade = txtExpecialidade.getText();

        if (!nome.trim().isEmpty() && !especialidade.trim().isEmpty()) {
            t.setId(Integer.parseInt(txtID.getText()));
            t.setTcnNome(nome);
            t.setTcnFone(txtTelefone.getText());
            t.setTcnEspecialidade(especialidade);

            tecnicoDao.salvar(t);
            tecnicos.add(t);
            tabelaTecnicos.getItems().setAll(tecnicos);
            atualizaTabela();
            limparTecnicos();
        } else {
            if (nome.trim().isEmpty()) {
                txtNome.setText("O campo nome esta vazio");
                txtNome.requestFocus();
            }
            if (especialidade.trim().isEmpty()) {
                txtExpecialidade.setText("O campo especialidade esta vazio");
                txtExpecialidade.requestFocus();
            }
            alertas("Alerta!", "Preencha os campos vazios");
        }
    }

    @FXML
    private void atualizarTecnico() {
        Tecnicos t = tabelaTecnicos.getSelectionModel().getSelectedItem();
        if (t != null) {
            t.setId(Integer.parseInt(txtID.getText()));
            t.setTcnNome(txtNome.getText());
            t.setTcnFone(txtTelefone.getText());
            t.setTcnEspecialidade(txtExpecialidade.getText());
            
            tecnicoDao.atualizar(t);
            tabelaTecnicos.getItems().setAll(tecnicos);
            atualizaTabela();
            limparTecnicos();
        } else {
            alertas("Alerta!", "Selecione um técnico");
        }
    }

    @FXML
    private void excluirTecnico() {
        Tecnicos t = tabelaTecnicos.getSelectionModel().getSelectedItem();
        if (t != null) {
            tecnicoDao.apagar(t.getId());
            atualizarTecnico();
            limparTecnicos();
        } else {
            alertas("Alerta!", "Selecione um técnico");
        }
    }

    private void preencheTabela() {
        atualizaTabela();

        columnId.setCellValueFactory(new PropertyValueFactory("Id"));
        columnNome.setCellValueFactory(new PropertyValueFactory("tcnNome"));
        columnTelefone.setCellValueFactory(new PropertyValueFactory("tcnFone"));
        columnEspecialidade.setCellValueFactory(new PropertyValueFactory("tcnEspecialidade"));
    }

    private void atualizaTabela() {
        tecnicos = tecnicoDao.buscarTodos();
        tecnicosfx = FXCollections.observableArrayList(tecnicos);
        tabelaTecnicos.setItems(tecnicosfx);
    }

    private void selecionaItemTabela(Tecnicos t) {
        if (t != null) {
            txtID.setText(String.valueOf(t.getId()));
            txtNome.setText(t.getTcnNome());
            txtTelefone.setText(t.getTcnFone());
            txtExpecialidade.setText(t.getTcnEspecialidade());
        } else {
            limparTecnicos();

        }
    }

    @FXML
    private void limparTecnicos() {
        txtID.clear();
        txtNome.clear();
        txtTelefone.clear();
        txtExpecialidade.clear();
        tabelaTecnicos.getSelectionModel().clearSelection();
    }

    public void alertas(String titulo, String frase) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(frase);
        alert.showAndWait();
    }

}
