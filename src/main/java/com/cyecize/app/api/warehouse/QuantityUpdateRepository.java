package com.cyecize.app.api.warehouse;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
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
    public void persistAll(List<QuantityUpdate> quantityUpdatesToSave) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        for (QuantityUpdate quantityUpdate : quantityUpdatesToSave) {
            entityManager.persist(quantityUpdate);
        }
    }

    @Transactional
    public void merge(QuantityUpdate quantityUpdate) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(quantityUpdate);
    }
}
