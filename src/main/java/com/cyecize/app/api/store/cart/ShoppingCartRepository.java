package com.cyecize.app.api.store.cart;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShoppingCartRepository {

    private final TransactionContext transactionContext;

    private final SpecificationExecutor specificationExecutor;

    @Transactional
    public ShoppingCart persist(ShoppingCart shoppingCart) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(shoppingCart);
        return shoppingCart;
    }

    @Transactional
    public void merge(ShoppingCart shoppingCart) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(shoppingCart);
    }

    @Transactional
    public ShoppingCart findByUserId(Long userId) {
        return this.specificationExecutor.findOne(
                ShoppingCartSpecifications.userIdEquals(userId),
                ShoppingCart.class,
                EntityGraphs.SHOPPING_CART_WITH_ITEMS
        );
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from ShoppingCart where userId = ?1")
                .setParameter(1, userId)
                .executeUpdate();
    }
}
