/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpa;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Consultas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import jpa.exceptions.NonexistentEntityException;
import modelo.Exames;

/**
 *
 * @author estagio
 */
public class ExamesJpaController implements Serializable {

    public ExamesJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Exames exames) {
        if (exames.getConsultasList() == null) {
            exames.setConsultasList(new ArrayList<Consultas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Consultas> attachedConsultasList = new ArrayList<Consultas>();
            for (Consultas consultasListConsultasToAttach : exames.getConsultasList()) {
                consultasListConsultasToAttach = em.getReference(consultasListConsultasToAttach.getClass(), consultasListConsultasToAttach.getId());
                attachedConsultasList.add(consultasListConsultasToAttach);
            }
            exames.setConsultasList(attachedConsultasList);
            em.persist(exames);
            for (Consultas consultasListConsultas : exames.getConsultasList()) {
                Exames oldCnsExameOfConsultasListConsultas = consultasListConsultas.getCnsExame();
                consultasListConsultas.setCnsExame(exames);
                consultasListConsultas = em.merge(consultasListConsultas);
                if (oldCnsExameOfConsultasListConsultas != null) {
                    oldCnsExameOfConsultasListConsultas.getConsultasList().remove(consultasListConsultas);
                    oldCnsExameOfConsultasListConsultas = em.merge(oldCnsExameOfConsultasListConsultas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Exames exames) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Exames persistentExames = em.find(Exames.class, exames.getId());
            List<Consultas> consultasListOld = persistentExames.getConsultasList();
            List<Consultas> consultasListNew = exames.getConsultasList();
            List<Consultas> attachedConsultasListNew = new ArrayList<Consultas>();
            for (Consultas consultasListNewConsultasToAttach : consultasListNew) {
                consultasListNewConsultasToAttach = em.getReference(consultasListNewConsultasToAttach.getClass(), consultasListNewConsultasToAttach.getId());
                attachedConsultasListNew.add(consultasListNewConsultasToAttach);
            }
            consultasListNew = attachedConsultasListNew;
            exames.setConsultasList(consultasListNew);
            exames = em.merge(exames);
            for (Consultas consultasListOldConsultas : consultasListOld) {
                if (!consultasListNew.contains(consultasListOldConsultas)) {
                    consultasListOldConsultas.setCnsExame(null);
                    consultasListOldConsultas = em.merge(consultasListOldConsultas);
                }
            }
            for (Consultas consultasListNewConsultas : consultasListNew) {
                if (!consultasListOld.contains(consultasListNewConsultas)) {
                    Exames oldCnsExameOfConsultasListNewConsultas = consultasListNewConsultas.getCnsExame();
                    consultasListNewConsultas.setCnsExame(exames);
                    consultasListNewConsultas = em.merge(consultasListNewConsultas);
                    if (oldCnsExameOfConsultasListNewConsultas != null && !oldCnsExameOfConsultasListNewConsultas.equals(exames)) {
                        oldCnsExameOfConsultasListNewConsultas.getConsultasList().remove(consultasListNewConsultas);
                        oldCnsExameOfConsultasListNewConsultas = em.merge(oldCnsExameOfConsultasListNewConsultas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = exames.getId();
                if (findExames(id) == null) {
                    throw new NonexistentEntityException("The exames with id " + id + " no longer exists.");
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
            Exames exames;
            try {
                exames = em.getReference(Exames.class, id);
                exames.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The exames with id " + id + " no longer exists.", enfe);
            }
            List<Consultas> consultasList = exames.getConsultasList();
            for (Consultas consultasListConsultas : consultasList) {
                consultasListConsultas.setCnsExame(null);
                consultasListConsultas = em.merge(consultasListConsultas);
            }
            em.remove(exames);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Exames> findExamesEntities() {
        return findExamesEntities(true, -1, -1);
    }

    public List<Exames> findExamesEntities(int maxResults, int firstResult) {
        return findExamesEntities(false, maxResults, firstResult);
    }

    private List<Exames> findExamesEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Exames.class));
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

    public Exames findExames(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Exames.class, id);
        } finally {
            em.close();
        }
    }

    public int getExamesCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Exames> rt = cq.from(Exames.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
