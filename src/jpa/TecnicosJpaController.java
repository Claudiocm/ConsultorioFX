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
import modelo.Tecnicos;

/**
 *
 * @author estagio
 */
public class TecnicosJpaController implements Serializable {

    public TecnicosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tecnicos tecnicos) {
        if (tecnicos.getConsultasList() == null) {
            tecnicos.setConsultasList(new ArrayList<Consultas>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Consultas> attachedConsultasList = new ArrayList<Consultas>();
            for (Consultas consultasListConsultasToAttach : tecnicos.getConsultasList()) {
                consultasListConsultasToAttach = em.getReference(consultasListConsultasToAttach.getClass(), consultasListConsultasToAttach.getId());
                attachedConsultasList.add(consultasListConsultasToAttach);
            }
            tecnicos.setConsultasList(attachedConsultasList);
            em.persist(tecnicos);
            for (Consultas consultasListConsultas : tecnicos.getConsultasList()) {
                Tecnicos oldCnsTecnicoOfConsultasListConsultas = consultasListConsultas.getCnsTecnico();
                consultasListConsultas.setCnsTecnico(tecnicos);
                consultasListConsultas = em.merge(consultasListConsultas);
                if (oldCnsTecnicoOfConsultasListConsultas != null) {
                    oldCnsTecnicoOfConsultasListConsultas.getConsultasList().remove(consultasListConsultas);
                    oldCnsTecnicoOfConsultasListConsultas = em.merge(oldCnsTecnicoOfConsultasListConsultas);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tecnicos tecnicos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Tecnicos persistentTecnicos = em.find(Tecnicos.class, tecnicos.getId());
            List<Consultas> consultasListOld = persistentTecnicos.getConsultasList();
            List<Consultas> consultasListNew = tecnicos.getConsultasList();
            List<Consultas> attachedConsultasListNew = new ArrayList<Consultas>();
            for (Consultas consultasListNewConsultasToAttach : consultasListNew) {
                consultasListNewConsultasToAttach = em.getReference(consultasListNewConsultasToAttach.getClass(), consultasListNewConsultasToAttach.getId());
                attachedConsultasListNew.add(consultasListNewConsultasToAttach);
            }
            consultasListNew = attachedConsultasListNew;
            tecnicos.setConsultasList(consultasListNew);
            tecnicos = em.merge(tecnicos);
            for (Consultas consultasListOldConsultas : consultasListOld) {
                if (!consultasListNew.contains(consultasListOldConsultas)) {
                    consultasListOldConsultas.setCnsTecnico(null);
                    consultasListOldConsultas = em.merge(consultasListOldConsultas);
                }
            }
            for (Consultas consultasListNewConsultas : consultasListNew) {
                if (!consultasListOld.contains(consultasListNewConsultas)) {
                    Tecnicos oldCnsTecnicoOfConsultasListNewConsultas = consultasListNewConsultas.getCnsTecnico();
                    consultasListNewConsultas.setCnsTecnico(tecnicos);
                    consultasListNewConsultas = em.merge(consultasListNewConsultas);
                    if (oldCnsTecnicoOfConsultasListNewConsultas != null && !oldCnsTecnicoOfConsultasListNewConsultas.equals(tecnicos)) {
                        oldCnsTecnicoOfConsultasListNewConsultas.getConsultasList().remove(consultasListNewConsultas);
                        oldCnsTecnicoOfConsultasListNewConsultas = em.merge(oldCnsTecnicoOfConsultasListNewConsultas);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tecnicos.getId();
                if (findTecnicos(id) == null) {
                    throw new NonexistentEntityException("The tecnicos with id " + id + " no longer exists.");
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
            Tecnicos tecnicos;
            try {
                tecnicos = em.getReference(Tecnicos.class, id);
                tecnicos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tecnicos with id " + id + " no longer exists.", enfe);
            }
            List<Consultas> consultasList = tecnicos.getConsultasList();
            for (Consultas consultasListConsultas : consultasList) {
                consultasListConsultas.setCnsTecnico(null);
                consultasListConsultas = em.merge(consultasListConsultas);
            }
            em.remove(tecnicos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Tecnicos> findTecnicosEntities() {
        return findTecnicosEntities(true, -1, -1);
    }

    public List<Tecnicos> findTecnicosEntities(int maxResults, int firstResult) {
        return findTecnicosEntities(false, maxResults, firstResult);
    }

    private List<Tecnicos> findTecnicosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tecnicos.class));
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

    public Tecnicos findTecnicos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tecnicos.class, id);
        } finally {
            em.close();
        }
    }

    public int getTecnicosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tecnicos> rt = cq.from(Tecnicos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
