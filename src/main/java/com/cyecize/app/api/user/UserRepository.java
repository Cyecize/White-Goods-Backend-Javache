package com.cyecize.app.api.user;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public User persist(User user) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(user);
        return user;
    }

    @Transactional
    public User findById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager.createQuery("select u from User u where u.id = :id", User.class)
                .setParameter("id", id)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public User merge(User user) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.merge(user);
    }

    @Transactional
    public boolean deleteById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("delete from User where id = :id")
                .setParameter("id", id)
                .executeUpdate() > 0;
    }
}
