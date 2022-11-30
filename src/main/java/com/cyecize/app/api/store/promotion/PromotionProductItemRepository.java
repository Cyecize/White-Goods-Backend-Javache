package com.cyecize.app.api.store.promotion;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
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

}
