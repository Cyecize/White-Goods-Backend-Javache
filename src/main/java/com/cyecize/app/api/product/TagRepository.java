package com.cyecize.app.api.product;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class TagRepository {
    private final TransactionContext transactionContext;

    @Transactional
    public Tag persist(Tag tag) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(tag);
        return tag;
    }
}
