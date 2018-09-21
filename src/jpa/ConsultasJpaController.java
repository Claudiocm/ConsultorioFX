/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.exceptions.NonexistentEntityException;
import modelo.Consultas;
import modelo.Exames;
import modelo.Tecnicos;
import modelo.Pacientes;

/**
 *
 * @author estagio
 */
public class ConsultasJpaController implements Serializable {

    public ConsultasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consultas consultas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Exames cnsExame = consultas.getCnsExame();
            if (cnsExame != null) {
                cnsExame = em.getReference(cnsExame.getClass(), cnsExame.getId());
                consultas.setCnsExame(cnsExame);
            }
            Tecnicos cnsTecnico = consultas.getCnsTecnico();
            if (cnsTecnico != null) {
                cnsTecnico = em.getReference(cnsTecnico.getClass(), cnsTecnico.getId());
                consultas.setCnsTecnico(cnsTecnico);
            }
            Pacientes cnsPaciente = consultas.getCnsPaciente();
            if (cnsPaciente != null) {
                cnsPaciente = em.getReference(cnsPaciente.getClass(), cnsPaciente.getId());
                consultas.setCnsPaciente(cnsPaciente);
            }
            em.persist(consultas);
            if (cnsExame != null) {
                cnsExame.getConsultasList().add(consultas);
                cnsExame = em.merge(cnsExame);
            }
            if (cnsTecnico != null) {
                cnsTecnico.getConsultasList().add(consultas);
                cnsTecnico = em.merge(cnsTecnico);
            }
            if (cnsPaciente != null) {
                cnsPaciente.getConsultasList().add(consultas);
                cnsPaciente = em.merge(cnsPaciente);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Consultas consultas) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Consultas persistentConsultas = em.find(Consultas.class, consultas.getId());
            Exames cnsExameOld = persistentConsultas.getCnsExame();
            Exames cnsExameNew = consultas.getCnsExame();
            Tecnicos cnsTecnicoOld = persistentConsultas.getCnsTecnico();
            Tecnicos cnsTecnicoNew = consultas.getCnsTecnico();
            Pacientes cnsPacienteOld = persistentConsultas.getCnsPaciente();
            Pacientes cnsPacienteNew = consultas.getCnsPaciente();
            if (cnsExameNew != null) {
                cnsExameNew = em.getReference(cnsExameNew.getClass(), cnsExameNew.getId());
                consultas.setCnsExame(cnsExameNew);
            }
            if (cnsTecnicoNew != null) {
                cnsTecnicoNew = em.getReference(cnsTecnicoNew.getClass(), cnsTecnicoNew.getId());
                consultas.setCnsTecnico(cnsTecnicoNew);
            }
            if (cnsPacienteNew != null) {
                cnsPacienteNew = em.getReference(cnsPacienteNew.getClass(), cnsPacienteNew.getId());
                consultas.setCnsPaciente(cnsPacienteNew);
            }
            consultas = em.merge(consultas);
            if (cnsExameOld != null && !cnsExameOld.equals(cnsExameNew)) {
                cnsExameOld.getConsultasList().remove(consultas);
                cnsExameOld = em.merge(cnsExameOld);
            }
            if (cnsExameNew != null && !cnsExameNew.equals(cnsExameOld)) {
                cnsExameNew.getConsultasList().add(consultas);
                cnsExameNew = em.merge(cnsExameNew);
            }
            if (cnsTecnicoOld != null && !cnsTecnicoOld.equals(cnsTecnicoNew)) {
                cnsTecnicoOld.getConsultasList().remove(consultas);
                cnsTecnicoOld = em.merge(cnsTecnicoOld);
            }
            if (cnsTecnicoNew != null && !cnsTecnicoNew.equals(cnsTecnicoOld)) {
                cnsTecnicoNew.getConsultasList().add(consultas);
                cnsTecnicoNew = em.merge(cnsTecnicoNew);
            }
            if (cnsPacienteOld != null && !cnsPacienteOld.equals(cnsPacienteNew)) {
                cnsPacienteOld.getConsultasList().remove(consultas);
                cnsPacienteOld = em.merge(cnsPacienteOld);
            }
            if (cnsPacienteNew != null && !cnsPacienteNew.equals(cnsPacienteOld)) {
                cnsPacienteNew.getConsultasList().add(consultas);
                cnsPacienteNew = em.merge(cnsPacienteNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = consultas.getId();
                if (findConsultas(id) == null) {
                    throw new NonexistentEntityException("The consultas with id " + id + " no longer exists.");
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
            Consultas consultas;
            try {
                consultas = em.getReference(Consultas.class, id);
                consultas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consultas with id " + id + " no longer exists.", enfe);
            }
            Exames cnsExame = consultas.getCnsExame();
            if (cnsExame != null) {
                cnsExame.getConsultasList().remove(consultas);
                cnsExame = em.merge(cnsExame);
            }
            Tecnicos cnsTecnico = consultas.getCnsTecnico();
            if (cnsTecnico != null) {
                cnsTecnico.getConsultasList().remove(consultas);
                cnsTecnico = em.merge(cnsTecnico);
            }
            Pacientes cnsPaciente = consultas.getCnsPaciente();
            if (cnsPaciente != null) {
                cnsPaciente.getConsultasList().remove(consultas);
                cnsPaciente = em.merge(cnsPaciente);
            }
            em.remove(consultas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Consultas> findConsultasEntities() {
        return findConsultasEntities(true, -1, -1);
    }

    public List<Consultas> findConsultasEntities(int maxResults, int firstResult) {
        return findConsultasEntities(false, maxResults, firstResult);
    }

    private List<Consultas> findConsultasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consultas.class));
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

    public Consultas findConsultas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consultas.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consultas> rt = cq.from(Consultas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
