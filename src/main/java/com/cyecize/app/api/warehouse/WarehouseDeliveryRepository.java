package com.cyecize.app.api.warehouse;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WarehouseDeliveryRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public WarehouseDelivery persist(WarehouseDelivery warehouseDelivery) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(warehouseDelivery);
        return warehouseDelivery;
    }

    @Transactional
    public void merge(WarehouseDelivery warehouseDelivery) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(warehouseDelivery);
    }

    @Transactional
    public WarehouseDelivery find(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select wd from WarehouseDelivery wd where wd.id = :id",
                        WarehouseDelivery.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }
}
