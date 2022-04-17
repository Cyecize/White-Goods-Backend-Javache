package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

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
}
