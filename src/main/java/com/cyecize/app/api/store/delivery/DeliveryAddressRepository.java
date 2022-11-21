package com.cyecize.app.api.store.delivery;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeliveryAddressRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public DeliveryAddress persist(DeliveryAddress address) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(address);
        return address;
    }

}
