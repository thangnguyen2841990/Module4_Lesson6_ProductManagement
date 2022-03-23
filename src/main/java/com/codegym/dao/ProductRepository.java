package com.codegym.dao;

import com.codegym.model.Product;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class ProductRepository implements IProductRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Product> findAll() {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product as p", Product.class);
        return query.getResultList();
    }

    @Override
    public void save(Product product) {
        if (product.getId() != null) {
            em.merge(product);
        } else {
            em.persist(product);
        }
    }

    @Override
    public Product findById(Long id) {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product as p where p.id =:id", Product.class);
        query.setParameter("id", id);

        try {
            return query.getSingleResult();

        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Product> findByName(String name) {
        TypedQuery<Product> query = em.createQuery("SELECT p FROM Product as p where p.name like :name", Product.class);
        query.setParameter("name", name);
        return query.getResultList();
    }

    @Override
    public void delete(Long id) {
        Product product = findById(id);
        if (product != null) {
            em.remove(product);
        }
    }
}
