package com.cyecize.app.api.warehouse;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuantityUpdateRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public QuantityUpdate persist(QuantityUpdate quantityUpdate) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(quantityUpdate);
        return quantityUpdate;
    }

    @Transactional
    public void merge(QuantityUpdate quantityUpdate) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(quantityUpdate);
    }
}
