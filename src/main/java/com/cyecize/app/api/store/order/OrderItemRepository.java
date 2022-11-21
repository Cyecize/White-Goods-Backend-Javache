package com.cyecize.app.api.store.order;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderItemRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public void persistAll(Collection<OrderItem> orderItems) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        for (OrderItem orderItem : orderItems) {
            entityManager.persist(orderItem);
        }
    }
}
