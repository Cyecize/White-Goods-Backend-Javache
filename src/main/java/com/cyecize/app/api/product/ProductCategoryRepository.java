package com.cyecize.app.api.product;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

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
    public ProductCategory find(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select pc from ProductCategory pc where pc.id = ?1", ProductCategory.class)
                .getResultStream().findFirst().orElse(null);
    }
}
