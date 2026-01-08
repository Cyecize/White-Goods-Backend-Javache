package com.cyecize.app.api.auth;

import com.cyecize.app.constants.EntityGraphs;
import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthTokenRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public void removeAllByUser(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from AuthToken where userId = ?1")
                .setParameter(1, userId)
                .executeUpdate();
    }

    @Transactional
    public void updateLastAccessTimeById(String tokenId, LocalDateTime lastAccessTime) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("update AuthToken at set at.lastAccessDate = ?2 WHERE at.id = ?1")
                .setParameter(1, tokenId)
                .setParameter(2, lastAccessTime)
                .executeUpdate();
    }

    @Transactional
    public List<AuthToken> findByUser(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select at from AuthToken at where at.userId = ?1",
                        AuthToken.class)
                .setParameter(1, userId)
                .getResultList();
    }

    @Transactional
    public Optional<AuthToken> findAuthTokenById(String id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select at from AuthToken at where at.id = ?1",
                        AuthToken.class)
                .setParameter(1, id)
                .setHint(
                        General.HIBERNATE_HINT_ENTITY_GRAPH,
                        entityManager.getEntityGraph(EntityGraphs.AUTH_TOKEN_USER_ROLES)
                )
                .getResultStream().findFirst();
    }

    @Transactional
    public boolean existsById(String tokenId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery("select count (at) from AuthToken at where at.id = ?1",
                        Long.class)
                .setParameter(1, tokenId)
                .getSingleResult() > 0;
    }

    @Transactional
    public AuthToken persist(AuthToken token) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(token);

        return token;
    }

    @Transactional
    public AuthToken update(AuthToken token) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(token);

        return token;
    }

    @Transactional
    public void delete(String tokenId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from AuthToken where id = ?1")
                .setParameter(1, tokenId)
                .executeUpdate();
    }
}
