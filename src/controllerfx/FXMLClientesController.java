package controllerfx;

import dao.PacienteDAO;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Pacientes;

public class FXMLClientesController implements Initializable {

    @FXML
    private TextField txtEndereco;

    @FXML
    private TextField txtNome;

    @FXML
    private TextField txtTelefone;

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNascimento;

    @FXML
    private TextField txtSexo;

    @FXML
    private TableView<Pacientes> tabelaPacientes;
    @FXML
    private TableColumn<Pacientes, Number> columnCodigo;
    @FXML
    private TableColumn<Pacientes, String> columnNome;
    @FXML
    private TableColumn<Pacientes, String> columnTelefone;
    @FXML
    private TableColumn<Pacientes, Character> columnSexo;

    private List<Pacientes> listaPacientes;
    private ObservableList<Pacientes> pacientes;
    private PacienteDAO pacienteDao = null;
    @FXML
    private Button btnCriar;

    @FXML
    private Button btnAtualizar;

    @FXML
    private Button btnExcluir;

    @FXML
    private Button btnNovo;

    @FXML
    private Button btnLimpar;
    private final SimpleDateFormat formatar = new SimpleDateFormat("dd/MM/yyyy");

    @FXML
    void novoPaciente() {
        limparCliente();
    }

    @FXML
    void criarPaciente() throws ParseException {
        Pacientes p = new Pacientes();
        String nome = txtNome.getText();
        String sexo = txtSexo.getText();
        String nascimento = txtNascimento.getText();

        if (!nome.trim().isEmpty() && !nascimento.trim().isEmpty() && !sexo.trim().isEmpty()) {
            p.setPctNome(nome);
            p.setPctSexo(sexo);
            p.setPctNascimento(formatar.parse(nascimento));
            p.setPctFone(txtTelefone.getText());
            p.setPctEndereco(txtEndereco.getText());

            pacienteDao.salvar(p);
            pacientes.add(p);
            tabelaPacientes.getItems().setAll(pacientes);
            limparCliente();
            atualizaTabela();
        } else {
            if (nome.trim().isEmpty()) {
                txtNome.setText("nome esta vazio");
                txtNome.requestFocus();
            }
            if (nascimento.trim().isEmpty()) {
                txtNascimento.setText("Nascimento  está vazio");
                txtNascimento.requestFocus();
            }
            if (sexo.trim().isEmpty()) {
                txtSexo.setText("Sexo está vazio");
                txtSexo.requestFocus();
            }
            alertas("Alerta!", "Preencha os campos vazios!");
        }
    }

    @FXML
    void atualizarPaciente() throws ParseException {
        Pacientes p = tabelaPacientes.getSelectionModel().getSelectedItem();
        if (p != null) {
            p.setId(Integer.parseInt(txtCodigo.getText()));
            p.setPctNome(txtNome.getText());
            p.setPctSexo(txtSexo.getText());
            p.setPctNascimento(formatar.parse(txtNascimento.getText()));
            p.setPctFone(txtTelefone.getText());
            p.setPctEndereco(txtEndereco.getText());

            pacienteDao.atualizar(p);
            tabelaPacientes.getItems().setAll(pacientes);
            limparCliente();
            atualizaTabela();

        } else {
            alertas("Alerta!", "Selecione um paciente");
        }
    }

    @FXML
    void excluirPaciente() {
        Pacientes p = tabelaPacientes.getSelectionModel().getSelectedItem();
        if (p != null) {
            pacienteDao.apagar(p.getId());
            atualizaTabela();
        } else {
            alertas("Alerta!", "Selecione um paciente");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pacienteDao = new PacienteDAO();
        preencheTabela();
        tabelaPacientes.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selecionaItemTabela(newValue));
    }

    public void preencheTabela() {
        atualizaTabela();
        columnCodigo.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnNome.setCellValueFactory(new PropertyValueFactory<>("pctNome"));
        columnSexo.setCellValueFactory(new PropertyValueFactory<>("pctSexo"));
        columnTelefone.setCellValueFactory(new PropertyValueFactory<>("pctFone"));
    }

    private void atualizaTabela() {
        listaPacientes = pacienteDao.buscarTodos();
        pacientes = FXCollections.observableArrayList(listaPacientes);
        tabelaPacientes.setItems(pacientes);

    }

    public void selecionaItemTabela(Pacientes p) {
        if (p != null) {
            txtCodigo.setText(String.valueOf(p.getId()));
            txtNome.setText(p.getPctNome());
            txtEndereco.setText(p.getPctEndereco());
            txtTelefone.setText(p.getPctFone());
            txtSexo.setText(p.getPctSexo());
            txtNascimento.setText(formatar.format(p.getPctNascimento()));
        }else{
            limparCliente();
        }

    }

    @FXML
    private void limparCliente() {
        txtCodigo.clear();
        txtNome.clear();
        txtEndereco.clear();
        txtSexo.clear();
        txtTelefone.clear();
        txtNascimento.clear();
        tabelaPacientes.getSelectionModel().clearSelection();
    }

    public void alertas(String titulo, String frase) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(frase);
        alert.showAndWait();
    }

}
