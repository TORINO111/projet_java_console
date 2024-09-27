package dette.boutique.core.database.impl;

import java.util.List;

import dette.boutique.core.database.Repository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class RepositoryJpaImpl<T> implements Repository<T> {
    protected EntityManager em;
    protected EntityManagerFactory emf = Persistence.createEntityManagerFactory("MySqlBi");
    protected Class<T> type;

    public RepositoryJpaImpl(EntityManager em, Class<T> type) {
        this.type = type;
        if (em == null) {
            this.em = emf.createEntityManager();
        } else {
            this.em = em;
        }
    }

    @Override
    public void insert(T entity) {
        try {
            em.getTransaction().begin();
            em.merge(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public List<T> selectAll() {
        String entityName = type.getSimpleName();
        return this.em.createQuery("SELECT u FROM " + entityName + " u", type).getResultList();
    }

}
