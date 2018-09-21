package modelo;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author estagio
 */
@Entity
@Table(name = "pacientes")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Pacientes.findAll", query = "SELECT p FROM Pacientes p")
    , @NamedQuery(name = "Pacientes.findById", query = "SELECT p FROM Pacientes p WHERE p.id = :id")
    , @NamedQuery(name = "Pacientes.findByPctNome", query = "SELECT p FROM Pacientes p WHERE p.pctNome = :pctNome")
    , @NamedQuery(name = "Pacientes.findByPctFone", query = "SELECT p FROM Pacientes p WHERE p.pctFone = :pctFone")
    , @NamedQuery(name = "Pacientes.findByPctNascimento", query = "SELECT p FROM Pacientes p WHERE p.pctNascimento = :pctNascimento")})
public class Pacientes implements Serializable {

    @OneToMany(mappedBy = "cnsPaciente")
    private List<Consultas> consultasList;

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "pct_nome")
    private String pctNome;
    @Basic(optional = false)
    @Column(name = "pct_endereco")
    private String pctEndereco;
    @Basic(optional = false)
    @Column(name = "pct_sexo")
    private String pctSexo;
    @Column(name = "pct_fone")
    private String pctFone;
    @Column(name = "pct_nascimento")
    @Temporal(TemporalType.DATE)
    private Date pctNascimento;

    public Pacientes() {
    }

    public Pacientes(Integer id) {
        this.id = id;
    }

    public Pacientes(Integer id, String pctNome, String pctEndereco, String pctSexo) {
        this.id = id;
        this.pctNome = pctNome;
        this.pctEndereco = pctEndereco;
        this.pctSexo = pctSexo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPctNome() {
        return pctNome;
    }

    public void setPctNome(String pctNome) {
        this.pctNome = pctNome;
    }

    public String getPctEndereco() {
        return pctEndereco;
    }

    public void setPctEndereco(String pctEndereco) {
        this.pctEndereco = pctEndereco;
    }

    public String getPctSexo() {
        return pctSexo;
    }

    public void setPctSexo(String pctSexo) {
        this.pctSexo = pctSexo;
    }

    public String getPctFone() {
        return pctFone;
    }

    public void setPctFone(String pctFone) {
        this.pctFone = pctFone;
    }

    public Date getPctNascimento() {
        return pctNascimento;
    }

    public void setPctNascimento(Date pctNascimento) {
        this.pctNascimento = pctNascimento;
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
        if (!(object instanceof Pacientes)) {
            return false;
        }
        Pacientes other = (Pacientes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return pctNome + " ";
    }

    @XmlTransient
    public List<Consultas> getConsultasList() {
        return consultasList;
    }

    public void setConsultasList(List<Consultas> consultasList) {
        this.consultasList = consultasList;
    }
    
}
