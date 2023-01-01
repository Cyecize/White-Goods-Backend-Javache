package com.cyecize.app.api.user.address;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAddressRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public UserAddress findByUserIdAndAddressId(Long userId, Long addressId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select ua from UserAddress ua where ua.userId = :userId and ua.id = :id",
                        UserAddress.class
                )
                .setParameter("userId", userId)
                .setParameter("id", addressId)
                .getResultStream().findFirst().orElse(null);
    }

    @Transactional
    public UserAddress persist(UserAddress userAddress) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(userAddress);
        return userAddress;
    }

    @Transactional
    public void setPreferredFalseWhereUserIdEquals(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery(
                        "update UserAddress set preferredAddress = false where userId = :uid"
                ).setParameter("uid", userId)
                .executeUpdate();
    }

    @Transactional
    public List<UserAddress> findByUserId(Long userId) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select ua from UserAddress ua where ua.userId = :uid",
                        UserAddress.class
                ).setParameter("uid", userId)
                .getResultList();
    }
}
