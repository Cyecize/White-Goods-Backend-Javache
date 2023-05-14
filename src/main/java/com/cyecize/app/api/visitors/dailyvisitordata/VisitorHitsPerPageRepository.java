package com.cyecize.app.api.visitors.dailyvisitordata;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VisitorHitsPerPageRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public VisitorHitsPerPage persist(VisitorHitsPerPage entity) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(entity);
        return entity;
    }
}
