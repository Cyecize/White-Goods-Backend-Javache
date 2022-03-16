package com.cyecize.app.api.product;

import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductCategoryRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public List<ProductCategory> findALl() {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager
                .createQuery("select pc from ProductCategory pc", ProductCategory.class)
                .setHint(General.HIBERNATE_HINT_ENTITY_GRAPH, entityManager.getEntityGraph("productCategoryAll"))
                .getResultList();
    }
}
