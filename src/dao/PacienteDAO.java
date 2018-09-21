package dao;

import java.util.Date;
import jpa.PacientesJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Pacientes;


public class PacienteDAO implements ControleInterface<Pacientes> {
    private final PacientesJpaController pacienteDao;
    private final EntityManagerFactory emf ;

    public PacienteDAO() {
        emf = Persistence.createEntityManagerFactory("consultorioFXPU");
        pacienteDao = new PacientesJpaController(emf);
    }
        
    @Override
    public void salvar(Pacientes p) {
        pacienteDao.create(p);
    }

    @Override
    public void atualizar(Pacientes p) {
        try {
            pacienteDao.edit(p);
        } catch (Exception ex) {
            Logger.getLogger(PacienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void apagar(int id) {
        try {
            pacienteDao.destroy(id);
        } catch (jpa.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(PacienteDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Pacientes buscarId(int id) {
        return pacienteDao.findPacientes(id);
    }

    @Override
    public List<Pacientes> buscarTodos() {
        return pacienteDao.findPacientesEntities();
    }
    
    public Pacientes buscarNascimento(Date nascimento){
        return pacienteDao.findPacienteNascimento(nascimento);
    }
    
}
