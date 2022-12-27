package com.cyecize.app.api.user;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public List<String> selectEmailWhereRoleEquals(RoleType role) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        return entityManager.createQuery(
                        "select u.email from User u join u.roles r where r.role = ?1",
                        String.class
                )
                .setParameter(1, role)
                .getResultList();
    }

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
}
