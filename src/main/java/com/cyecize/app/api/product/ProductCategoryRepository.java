package com.cyecize.app.api.product;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductCategoryRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public List<ProductCategory> findAll() {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager
                .createQuery("select pc from ProductCategory pc", ProductCategory.class)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.PRODUCT_CATEGORY_ALL)
                )
                .getResultList();
    }

    @Transactional
    public ProductCategory persist(ProductCategory productCategory) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(productCategory);
        return productCategory;
    }

    @Transactional
    public void merge(ProductCategory productCategory) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(productCategory);
    }

    @Transactional
    public ProductCategory find(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select pc from ProductCategory pc where pc.id = ?1",
                        ProductCategory.class)
                .setParameter(1, id)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public void deleteById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from ProductCategory where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }
}
