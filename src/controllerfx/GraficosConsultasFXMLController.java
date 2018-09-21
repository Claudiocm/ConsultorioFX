package controllerfx;

import dao.ConsultaDAO;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import modelo.Consultas;

public class GraficosConsultasFXMLController implements Initializable {

    @FXML
    private BarChart<String, Date> graficoConsultas;
    @FXML
    private Button btnCarregar;
    private List<Consultas> chamados;
    private ObservableList<Consultas> chamadofx;
    private ConsultaDAO consultaDao = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            consultaDao = new ConsultaDAO(); 
            carregarGrafico();
    }

    @FXML
    private void carregarGrafico() {
        XYChart.Series<String, Date> series = new XYChart.Series<>();
        try {
            chamados = consultaDao.buscarTodos();
            for (Consultas c : chamados) {
                series.getData().add(new XYChart.Data<>(c.getCnsExame().getExmDescricao(), c.getCnsData()));
            }
            graficoConsultas.getData().add(series);

        } catch (Exception e) {

        }
    }

}
