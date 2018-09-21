package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Consultas;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;
import modelo.Pacientes;

/**
 *
 * @author estagio
 */
public class PacientesJpaController implements Serializable {

    public PacientesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pacientes pacientes) {
        if (pacientes.getConsultasList() == null) {
            pacientes.setConsultasList(new ArrayList<Consultas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Consultas> attachedConsultasList = new ArrayList<Consultas>();
            for (Consultas consultasListConsultasToAttach : pacientes.getConsultasList()) {
                consultasListConsultasToAttach = em.getReference(consultasListConsultasToAttach.getClass(), consultasListConsultasToAttach.getId());
                attachedConsultasList.add(consultasListConsultasToAttach);
            }
            pacientes.setConsultasList(attachedConsultasList);
            em.persist(pacientes);
            for (Consultas consultasListConsultas : pacientes.getConsultasList()) {
                Pacientes oldCnsPacienteOfConsultasListConsultas = consultasListConsultas.getCnsPaciente();
                consultasListConsultas.setCnsPaciente(pacientes);
                consultasListConsultas = em.merge(consultasListConsultas);
                if (oldCnsPacienteOfConsultasListConsultas != null) {
                    oldCnsPacienteOfConsultasListConsultas.getConsultasList().remove(consultasListConsultas);
                    oldCnsPacienteOfConsultasListConsultas = em.merge(oldCnsPacienteOfConsultasListConsultas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pacientes pacientes) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes persistentPacientes = em.find(Pacientes.class, pacientes.getId());
            List<Consultas> consultasListOld = persistentPacientes.getConsultasList();
            List<Consultas> consultasListNew = pacientes.getConsultasList();
            List<Consultas> attachedConsultasListNew = new ArrayList<Consultas>();
            for (Consultas consultasListNewConsultasToAttach : consultasListNew) {
                consultasListNewConsultasToAttach = em.getReference(consultasListNewConsultasToAttach.getClass(), consultasListNewConsultasToAttach.getId());
                attachedConsultasListNew.add(consultasListNewConsultasToAttach);
            }
            consultasListNew = attachedConsultasListNew;
            pacientes.setConsultasList(consultasListNew);
            pacientes = em.merge(pacientes);
            for (Consultas consultasListOldConsultas : consultasListOld) {
                if (!consultasListNew.contains(consultasListOldConsultas)) {
                    consultasListOldConsultas.setCnsPaciente(null);
                    consultasListOldConsultas = em.merge(consultasListOldConsultas);
                }
            }
            for (Consultas consultasListNewConsultas : consultasListNew) {
                if (!consultasListOld.contains(consultasListNewConsultas)) {
                    Pacientes oldCnsPacienteOfConsultasListNewConsultas = consultasListNewConsultas.getCnsPaciente();
                    consultasListNewConsultas.setCnsPaciente(pacientes);
                    consultasListNewConsultas = em.merge(consultasListNewConsultas);
                    if (oldCnsPacienteOfConsultasListNewConsultas != null && !oldCnsPacienteOfConsultasListNewConsultas.equals(pacientes)) {
                        oldCnsPacienteOfConsultasListNewConsultas.getConsultasList().remove(consultasListNewConsultas);
                        oldCnsPacienteOfConsultasListNewConsultas = em.merge(oldCnsPacienteOfConsultasListNewConsultas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pacientes.getId();
                if (findPacientes(id) == null) {
                    throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pacientes pacientes;
            try {
                pacientes = em.getReference(Pacientes.class, id);
                pacientes.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pacientes with id " + id + " no longer exists.", enfe);
            }
            List<Consultas> consultasList = pacientes.getConsultasList();
            for (Consultas consultasListConsultas : consultasList) {
                consultasListConsultas.setCnsPaciente(null);
                consultasListConsultas = em.merge(consultasListConsultas);
            }
            em.remove(pacientes);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pacientes> findPacientesEntities() {
        return findPacientesEntities(true, -1, -1);
    }

    public List<Pacientes> findPacientesEntities(int maxResults, int firstResult) {
        return findPacientesEntities(false, maxResults, firstResult);
    }

    private List<Pacientes> findPacientesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pacientes.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Pacientes findPacientes(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pacientes.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacientesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pacientes> rt = cq.from(Pacientes.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }

    public Pacientes findPacienteNascimento(Date nascimento) {
        return emf.createEntityManager().createQuery("Select p from Pacientes p where pctNascimento =: nascimento", Pacientes.class)
                .setParameter("pctNascimento", nascimento)
                .getSingleResult();

    }

}
