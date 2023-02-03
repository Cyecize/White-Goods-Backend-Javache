package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public Product persist(Product product) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(product);
        return product;
    }

    @Transactional
    public void merge(Product product) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(product);
    }

    @Transactional
    public void mergeAll(List<Product> productsToSave) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        for (Product product : productsToSave) {
            entityManager.merge(product);
        }
    }

    @Transactional
    public boolean existsById(Long id) {
        if (id == null) {
            return false;
        }

        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select case when (count(p) > 0)  then true else false end from Product p where p.id = ?1",
                        Boolean.class
                ).setParameter(1, id)
                .getSingleResult();

    }

    @Transactional
    public boolean existsByIdAndQuantityGreaterOrEqual(Long id, Integer quantity) {
        if (id == null || quantity == null) {
            return false;
        }

        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select case when (count(p) > 0)  then true else false end from Product p "
                                + "where p.id = :pid and p.quantity >= :qty",
                        Boolean.class
                )
                .setParameter("pid", id)
                .setParameter("qty", quantity)
                .getSingleResult();
    }

    @Transactional
    public boolean subtractQuantity(Long id, Integer quantity) {
        if (id == null || quantity == null) {
            return false;
        }

        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "update Product p set p.quantity = p.quantity - :qty "
                                + "where p.id = :pid and p.quantity >= :qty")
                .setParameter("pid", id)
                .setParameter("qty", quantity)
                .executeUpdate() >= 1;
    }

    @Transactional
    public Product find(Long productId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select p from Product p where p.id = :pid", Product.class)
                .setParameter("pid", productId)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public List<Product> findAllNoFetch(Collection<Long> ids) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select p from Product p where p.id in :ids",
                        Product.class)
                .setParameter("ids", ids)
                .getResultList();
    }
}
