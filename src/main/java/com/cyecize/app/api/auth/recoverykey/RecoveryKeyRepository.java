package com.cyecize.app.api.auth.recoverykey;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecoveryKeyRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public RecoveryKey persist(RecoveryKey recoveryKey) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(recoveryKey);
        return recoveryKey;
    }

    @Transactional
    public RecoveryKey findById(String id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.find(RecoveryKey.class, id);
    }

    @Transactional
    public void deleteById(String id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from RecoveryKey where id = :id")
                .setParameter("id", id)
                .executeUpdate();
    }

    @Transactional
    public void deleteByUserId(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from RecoveryKey where userId = :uid")
                .setParameter("uid", userId)
                .executeUpdate();
    }
}
