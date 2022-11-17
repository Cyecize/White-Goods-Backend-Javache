package com.cyecize.app.api.store;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingCartItemRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public void persist(Collection<ShoppingCartItem> items) {
        final EntityManager em = this.transactionContext.getEntityManagerForTransaction();
        for (ShoppingCartItem item : items) {
            em.persist(item);
        }
    }

    @Transactional
    public void remove(Collection<ShoppingCartItem> items) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        for (ShoppingCartItem item : items) {
            entityManager.remove(item);
        }
    }
}
