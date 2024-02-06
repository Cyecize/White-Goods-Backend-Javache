package com.cyecize.app.api.store.promotion;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PromotionProductItemRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public PromotionProductItem persist(PromotionProductItem item) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(item);
        return item;
    }

    @Transactional
    public PromotionProductItem merge(PromotionProductItem item) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.merge(item);
    }

    @Transactional
    public void removeAll(Collection<Long> productIds) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from PromotionProductItem where productId in :ids")
                .setParameter("ids", productIds)
                .executeUpdate();
    }
}
