package controllerfx;

import dao.ConsultaDAO;
import dao.ExameDAO;
import dao.PacienteDAO;
import dao.TecnicoDAO;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import modelo.Consultas;
import modelo.Exames;
import modelo.Pacientes;
import modelo.Tecnicos;

public class FXMLConsultasController implements Initializable {

    @FXML
    private Button btnExcluir;
    @FXML
    private Button btnNovo;
    @FXML
    private Button btnLimpar;
    @FXML
    private Button btnCriar;
    @FXML
    private Button btnAtualizar;
    private List<Consultas> consultas;
    private ObservableList<Consultas> consultasfx;
    private ConsultaDAO consultasDao;
    private List<Pacientes> pacientes;
    private ObservableList<Pacientes> pacientesfx;
    private PacienteDAO pacientesDao;
    private List<Exames> exames;
    private ObservableList<Exames> examesfx;
    private ExameDAO examesDao;
    private List<Tecnicos> tecnicos;
    private ObservableList<Tecnicos> tecnicosfx;
    private TecnicoDAO tecnicosDao;
    @FXML
    private ComboBox<Exames> cmbExame;
    @FXML
    private ComboBox<Pacientes> cmbPaciente;
    @FXML
    private ComboBox<Tecnicos> cmbTecnico;
    @FXML
    private TextField txtSequencia;
    @FXML
    private TextField txtData;
    @FXML
    private TextField txtHora;
    @FXML
    private TableView<Consultas> tabelaConsulta;
    @FXML
    private TableColumn<Consultas, Integer> columnSequencia;
    @FXML
    private TableColumn<Consultas, String> columnData;
    @FXML
    private TableColumn<Consultas, String> columnHora;
    @FXML
    private TableColumn<Consultas, Integer> columnPaciente;
    @FXML
    private TableColumn<Consultas, Integer> columnExame;
    SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat formataHora = new SimpleDateFormat("HH:mm");
    SimpleDateFormat formatarInverso = new SimpleDateFormat("yyyy/MM/dd");
    @FXML
    private TextField txtNascimento;
    @FXML
    private TextField txtTelefone;
    @FXML
    private TextField txtValor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        consultasDao = new ConsultaDAO();
        tecnicosDao = new TecnicoDAO();
        pacientesDao = new PacienteDAO();
        examesDao = new ExameDAO();

        mascaras();
        preencherComboTecnico();
        preencherComboPaciente();
        preencherComboExame();
        preencherTabelaConsulta();
        txtValor.setEditable(false);
        txtNascimento.setEditable(false);
        txtTelefone.setEditable(false);
       
        tabelaConsulta.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> selecionaItemTabela(newValue));
    }

    private void preencherTabelaConsulta() {
        atualizaTabela();
        columnSequencia.setCellValueFactory(new PropertyValueFactory<>("Id"));
        columnData.setCellValueFactory(data -> {
            SimpleStringProperty property = new SimpleStringProperty();
            property.setValue(formataData.format(data.getValue().getCnsData()));
            return property;
        });
        columnHora.setCellValueFactory(
                hora -> {
                    SimpleStringProperty property = new SimpleStringProperty();
                    property.setValue(formataHora.format(hora.getValue().getCnsHora()));
                    return property;
                }
        );
        columnPaciente.setCellValueFactory(new PropertyValueFactory<>("cnsPaciente"));
        columnExame.setCellValueFactory(new PropertyValueFactory<>("cnsExame"));

    }

    private void atualizaTabela() {
        consultas = consultasDao.buscarTodos();
        consultasfx = FXCollections.observableArrayList(consultas);
        tabelaConsulta.setItems(consultasfx);
    }

    private void selecionaItemTabela(Consultas c) {

        if (c != null) {
            String data = formataData.format(c.getCnsData());
            String hora = formataHora.format(c.getCnsHora());

            txtSequencia.setText(String.valueOf(c.getId()));
            txtData.setText(data);
            txtHora.setText(hora);
            cmbExame.setValue(c.getCnsExame());
            txtValor.setText(String.format("%2.00f", c.getCnsExame().getExmValor()));
            cmbPaciente.setValue(c.getCnsPaciente());
            txtNascimento.setText(formataData.format(c.getCnsPaciente().getPctNascimento()));
            txtTelefone.setText(c.getCnsPaciente().getPctFone());
            cmbTecnico.setValue(c.getCnsTecnico());
        } else {
            limpaConsulta();
        }
    }

    public void preencherComboExame() {
        exames = examesDao.buscarTodos();
        examesfx = FXCollections.observableArrayList(exames);
        cmbExame.setItems(examesfx);
    }

    public void preencherComboPaciente() {
        pacientes = pacientesDao.buscarTodos();
        pacientesfx = FXCollections.observableArrayList(pacientes);
        cmbPaciente.setItems(pacientesfx);
    }

    public void preencherComboTecnico() {
        tecnicos = tecnicosDao.buscarTodos();
        tecnicosfx = FXCollections.observableArrayList(tecnicos);
        cmbTecnico.setTooltip(new Tooltip());
        cmbTecnico.setItems(tecnicosfx);
    }

    @FXML
    private void novaConsulta() {
        limpaConsulta();
    }

    @FXML
    private void criarConsulta() throws ParseException {
        Consultas c = new Consultas();
        Pacientes p = cmbPaciente.getValue();
        Tecnicos t = cmbTecnico.getValue();
        Exames e = cmbExame.getValue();

        Date data = formatarInverso.parse(txtData.getText());
        Date hora = formataHora.parse(txtHora.getText());

        if (p != null && e != null && data != null && hora != null) {
            //c.setCnsId(Integer.parseInt(txtSequencia.getText()));
            c.setCnsPaciente(p);
            c.setCnsExame(e);
            c.setCnsTecnico(t);
            c.setCnsData(data);
            c.setCnsHora(hora);

            consultasDao.salvar(c);
            consultas.add(c);
            tabelaConsulta.getItems().setAll(consultas);
            atualizaTabela();
            limpaConsulta();
        } else {
            if (p == null) {
                cmbPaciente.setPromptText("Preencha o campo Paciente");
                cmbPaciente.requestFocus();
            }
            if (e == null) {
                cmbExame.setPromptText("Preencha o campo exame");
                cmbExame.requestFocus();
            }
            if (data == null) {
                txtData.requestFocus();
            }
            if (hora == null) {
                txtHora.requestFocus();
            }
            alertas("Alerta", "Preencha os campos vazios!");
        }
    }

    @FXML
    private void atualizarConsulta() throws ParseException {
        Consultas c = tabelaConsulta.getSelectionModel().getSelectedItem();

        if (c != null) {
            c.setId(Integer.parseInt(txtSequencia.getText()));
            c.setCnsPaciente(cmbPaciente.getValue());
            c.setCnsExame(cmbExame.getValue());
            c.setCnsTecnico(cmbTecnico.getValue());
            c.setCnsData(formataData.parse(txtData.getText()));
            c.setCnsHora(formataHora.parse(txtHora.getText()));

            consultasDao.atualizar(c);
            tabelaConsulta.getItems().setAll(consultas);
            atualizaTabela();
            limpaConsulta();
        } else {
            alertas("Alerta", "Selecione uma consulta!");
        }
    }

    @FXML
    private void excluirConsulta() {
        Consultas c = tabelaConsulta.getSelectionModel().getSelectedItem();

        if (c != null) {
            consultasDao.apagar(c.getId());
            tabelaConsulta.getItems().setAll(consultas);
            atualizaTabela();
        } else {
            alertas("Alerta", "Selecione uma consulta!");
        }
    }

    @FXML
    private void limpaConsulta() {
        txtSequencia.clear();
        txtData.clear();
        txtHora.clear();
        txtValor.clear();
        txtNascimento.clear();
        txtTelefone.clear();
        cmbExame.getItems().clear();
        cmbTecnico.getItems().clear();
        cmbPaciente.getItems().clear();
        tabelaConsulta.getSelectionModel().clearSelection();
    }

    public void alertas(String titulo, String frase) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(frase);
        alert.showAndWait();
    }

    private void mascaras() {

    }

   
}
