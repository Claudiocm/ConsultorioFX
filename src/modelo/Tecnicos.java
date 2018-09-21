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
@Table(name = "tecnicos")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tecnicos.findAll", query = "SELECT t FROM Tecnicos t")
    , @NamedQuery(name = "Tecnicos.findById", query = "SELECT t FROM Tecnicos t WHERE t.id = :id")
    , @NamedQuery(name = "Tecnicos.findByTcnNome", query = "SELECT t FROM Tecnicos t WHERE t.tcnNome = :tcnNome")
    , @NamedQuery(name = "Tecnicos.findByTcnFone", query = "SELECT t FROM Tecnicos t WHERE t.tcnFone = :tcnFone")
    , @NamedQuery(name = "Tecnicos.findByTcnEspecialidade", query = "SELECT t FROM Tecnicos t WHERE t.tcnEspecialidade = :tcnEspecialidade")})
public class Tecnicos implements Serializable {

    @OneToMany(mappedBy = "cnsTecnico")
    private List<Consultas> consultasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "tcn_nome")
    private String tcnNome;
    @Column(name = "tcn_fone")
    private String tcnFone;
    @Column(name = "tcn_especialidade")
    private String tcnEspecialidade;

    public Tecnicos() {
    }

    public Tecnicos(Integer id) {
        this.id = id;
    }

    public Tecnicos(Integer id, String tcnNome) {
        this.id = id;
        this.tcnNome = tcnNome;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTcnNome() {
        return tcnNome;
    }

    public void setTcnNome(String tcnNome) {
        this.tcnNome = tcnNome;
    }

    public String getTcnFone() {
        return tcnFone;
    }

    public void setTcnFone(String tcnFone) {
        this.tcnFone = tcnFone;
    }

    public String getTcnEspecialidade() {
        return tcnEspecialidade;
    }

    public void setTcnEspecialidade(String tcnEspecialidade) {
        this.tcnEspecialidade = tcnEspecialidade;
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
        if (!(object instanceof Tecnicos)) {
            return false;
        }
        Tecnicos other = (Tecnicos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tcnNome + " ";
    }

    @XmlTransient
    public List<Consultas> getConsultasList() {
        return consultasList;
    }

    public void setConsultasList(List<Consultas> consultasList) {
        this.consultasList = consultasList;
    }
    
}
