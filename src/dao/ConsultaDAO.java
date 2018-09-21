package dao;

import consultoriofx.exceptions.NonexistentEntityException;
import jpa.ConsultasJpaController;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import modelo.Consultas;

public class ConsultaDAO implements ControleInterface<Consultas> {

    private final ConsultasJpaController consultaDao;
    private final EntityManagerFactory emf;

    public ConsultaDAO() {
        emf = Persistence.createEntityManagerFactory("consultorioFXPU");
        consultaDao = new ConsultasJpaController(emf);
    }

    @Override
    public void salvar(Consultas c) {
        consultaDao.create(c);
    }

    @Override
    public void atualizar(Consultas c) {
        try {
            consultaDao.edit(c);
        } catch (Exception ex) {
            Logger.getLogger(ConsultaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void apagar(int id) {
        try {
            consultaDao.destroy(id);
        } catch (jpa.exceptions.NonexistentEntityException ex) {
            Logger.getLogger(ConsultaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Consultas buscarId(int id) {
        return consultaDao.findConsultas(id);
    }

    @Override
    public List<Consultas> buscarTodos() {
        return consultaDao.findConsultasEntities();
    }

    public List<Consultas> buscarConsultaNome(String nome) {
        Stream<Consultas> streamConsultas = this.buscarTodos().stream();
        return streamConsultas.filter(m -> !m.getCnsPaciente().getPctNome().trim().isEmpty() && m.getCnsPaciente().getPctNome().trim().toLowerCase().contains(nome.toLowerCase())).collect(Collectors.toList());

    }

}
