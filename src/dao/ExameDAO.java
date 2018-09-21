package dao;

import consultoriofx.exceptions.NonexistentEntityException;
import jpa.ExamesJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Exames;

public class ExameDAO implements ControleInterface<Exames> {

    private final ExamesJpaController exameDao;
    private final EntityManagerFactory emf;

    public ExameDAO() {
        emf = Persistence.createEntityManagerFactory("consultorioFXPU");
        exameDao = new ExamesJpaController(emf);
    }

    @Override
    public void salvar(Exames e) {
        exameDao.create(e);
    }

    @Override
    public void atualizar(Exames e) {
        try {
            exameDao.edit(e);
        } catch (Exception ex) {
            Logger.getLogger(ExameDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void apagar(int id) {
        try {
            exameDao.destroy(id);
        } catch (jpa.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(ExameDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Exames buscarId(int id) {
        return exameDao.findExames(id);
    }

    @Override
    public List<Exames> buscarTodos() {
        return exameDao.findExamesEntities();
    }

}
