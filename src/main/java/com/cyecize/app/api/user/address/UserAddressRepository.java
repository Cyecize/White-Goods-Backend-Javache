package com.cyecize.app.api.user.address;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
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

}
