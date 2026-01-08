package com.cyecize.app.api.product.productspec;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSpecificationRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public ProductSpecification persist(ProductSpecification productSpecification) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(productSpecification);
        return productSpecification;
    }

    @Transactional
    public void merge(ProductSpecification specification) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(specification);
    }
}
