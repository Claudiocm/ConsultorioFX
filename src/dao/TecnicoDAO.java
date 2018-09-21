package dao;

import consultoriofx.exceptions.NonexistentEntityException;
import jpa.TecnicosJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Tecnicos;

public class TecnicoDAO implements ControleInterface<Tecnicos> {

    private final TecnicosJpaController tecnicosDao;
    private final EntityManagerFactory emf;

    public TecnicoDAO() {
        emf = Persistence.createEntityManagerFactory("consultorioFXPU");
        tecnicosDao = new TecnicosJpaController(emf);
    }

    /**
     *
     * @param t
     */
    @Override
    public void salvar(Tecnicos t) {
        tecnicosDao.create(t);
    }

    @Override
    public void atualizar(Tecnicos t) {
        try {
            tecnicosDao.edit(t);
        } catch (Exception ex) {
            Logger.getLogger(TecnicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void apagar(int id) {
        try {
            tecnicosDao.destroy(id);
        } catch (jpa.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(TecnicoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Tecnicos buscarId(int id) {
        return tecnicosDao.findTecnicos(id);
    }

    @Override
    public List<Tecnicos> buscarTodos() {
        return tecnicosDao.findTecnicosEntities();
    }

}
