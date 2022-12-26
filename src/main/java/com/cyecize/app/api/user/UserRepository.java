package com.cyecize.app.api.user;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public List<String> selectEmailWhereRoleEquals(String role) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager.createQuery("select u.email from User u join u.roles r where r.role = ?1", String.class)
                .setParameter(1, role)
                .getResultList();
    }

    @Transactional
    public User persist(User user) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(user);
        return user;
    }
}
