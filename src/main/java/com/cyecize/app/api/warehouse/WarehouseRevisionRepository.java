package com.cyecize.app.api.warehouse;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseRevisionRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public WarehouseRevision persist(WarehouseRevision warehouseRevision) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(warehouseRevision);
        return warehouseRevision;
    }

    @Transactional
    public void merge(WarehouseRevision warehouseRevision) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(warehouseRevision);
    }

    @Transactional
    public WarehouseRevision find(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select wr from WarehouseRevision wr where wr.id = :id",
                        WarehouseRevision.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }
}
