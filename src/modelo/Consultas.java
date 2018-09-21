
package modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author estagio
 */
@Entity
@Table(name = "consultas")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Consultas.findAll", query = "SELECT c FROM Consultas c")
    , @NamedQuery(name = "Consultas.findById", query = "SELECT c FROM Consultas c WHERE c.id = :id")
    , @NamedQuery(name = "Consultas.findByCnsData", query = "SELECT c FROM Consultas c WHERE c.cnsData = :cnsData")
    , @NamedQuery(name = "Consultas.findByCnsHora", query = "SELECT c FROM Consultas c WHERE c.cnsHora = :cnsHora")})
public class Consultas implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "cns_data")
    @Temporal(TemporalType.DATE)
    private Date cnsData;
    @Basic(optional = false)
    @Column(name = "cns_hora")
    @Temporal(TemporalType.TIME)
    private Date cnsHora;
    @JoinColumn(name = "cns_exame", referencedColumnName = "id")
    @ManyToOne
    private Exames cnsExame;
    @JoinColumn(name = "cns_tecnico", referencedColumnName = "id")
    @ManyToOne
    private Tecnicos cnsTecnico;
    @JoinColumn(name = "cns_paciente", referencedColumnName = "id")
    @ManyToOne
    private Pacientes cnsPaciente;

    public Consultas() {
    }

    public Consultas(Integer id) {
        this.id = id;
    }

    public Consultas(Integer id, Date cnsData, Date cnsHora) {
        this.id = id;
        this.cnsData = cnsData;
        this.cnsHora = cnsHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCnsData() {
        return cnsData;
    }

    public void setCnsData(Date cnsData) {
        this.cnsData = cnsData;
    }

    public Date getCnsHora() {
        return cnsHora;
    }

    public void setCnsHora(Date cnsHora) {
        this.cnsHora = cnsHora;
    }

    public Exames getCnsExame() {
        return cnsExame;
    }

    public void setCnsExame(Exames cnsExame) {
        this.cnsExame = cnsExame;
    }

    public Tecnicos getCnsTecnico() {
        return cnsTecnico;
    }

    public void setCnsTecnico(Tecnicos cnsTecnico) {
        this.cnsTecnico = cnsTecnico;
    }

    public Pacientes getCnsPaciente() {
        return cnsPaciente;
    }

    public void setCnsPaciente(Pacientes cnsPaciente) {
        this.cnsPaciente = cnsPaciente;
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
        if (!(object instanceof Consultas)) {
            return false;
        }
        Consultas other = (Consultas) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return id + " ";
    }
    
}
