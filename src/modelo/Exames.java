package modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author estagio
 */
@Entity
@Table(name = "exames")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Exames.findAll", query = "SELECT e FROM Exames e")
    , @NamedQuery(name = "Exames.findById", query = "SELECT e FROM Exames e WHERE e.id = :id")
    , @NamedQuery(name = "Exames.findByExmDescricao", query = "SELECT e FROM Exames e WHERE e.exmDescricao = :exmDescricao")
    , @NamedQuery(name = "Exames.findByExmValor", query = "SELECT e FROM Exames e WHERE e.exmValor = :exmValor")})
public class Exames implements Serializable {

    @OneToMany(mappedBy = "cnsExame")
    private List<Consultas> consultasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "exm_descricao")
    private String exmDescricao;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "exm_valor")
    private Double exmValor;

    public Exames() {
    }

    public Exames(Integer id) {
        this.id = id;
    }

    public Exames(Integer id, String exmDescricao) {
        this.id = id;
        this.exmDescricao = exmDescricao;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getExmDescricao() {
        return exmDescricao;
    }

    public void setExmDescricao(String exmDescricao) {
        this.exmDescricao = exmDescricao;
    }

    public Double getExmValor() {
        return exmValor;
    }

    public void setExmValor(Double exmValor) {
        this.exmValor = exmValor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exames)) {
            return false;
        }
        Exames other = (Exames) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return  exmDescricao + " ";
    }

    @XmlTransient
    public List<Consultas> getConsultasList() {
        return consultasList;
    }

    public void setConsultasList(List<Consultas> consultasList) {
        this.consultasList = consultasList;
    }
    
}
