package com.cyecize.app.api.product.productspec;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class SpecificationTypeRepository {
    private final TransactionContext transactionContext;

    @Transactional
    public boolean existsById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                "select case when (count(st) > 0)  then true else false end from SpecificationType st where st.id = ?1",
                Boolean.class
        ).getSingleResult();
    }
}
