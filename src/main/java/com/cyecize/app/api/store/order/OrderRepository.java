package com.cyecize.app.api.store.order;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public Order persist(Order order) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(order);
        return order;
    }

    @Transactional
    public Order merge(Order order) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.merge(order);
    }

    @Transactional
    public Order findById(Long orderId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager
                .createQuery("select o from Order o where o.id = :orderId", Order.class)
                .setParameter("orderId", orderId)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.ORDER_ALL)
                )
                .getResultStream().findFirst().orElse(null);
    }

    public Order findByIdAndUserId(Long orderId, Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager
                .createQuery(
                        "select o from Order o where o.id = :orderId and o.userId = :userId",
                        Order.class
                )
                .setParameter("orderId", orderId)
                .setParameter("userId", userId)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.ORDER_ALL)
                )
                .getResultStream().findFirst().orElse(null);
    }
}
