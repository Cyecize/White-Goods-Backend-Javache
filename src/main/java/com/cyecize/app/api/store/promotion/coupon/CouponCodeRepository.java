package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponCodeRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public CouponCode persist(CouponCode couponCode) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(couponCode);
        return couponCode;
    }

    @Transactional
    public CouponCode merge(CouponCode couponCode) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.merge(couponCode);
    }

    @Transactional
    public CouponCode findByCodeEnabled(String code) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select c from CouponCode c where c.code = :code and c.enabled = true",
                        CouponCode.class
                )
                .setParameter("code", code)
                .getSingleResult();
    }

    @Transactional
    public void deleteByEnabledFalse() {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.createQuery("delete from CouponCode where enabled = false ").executeUpdate();
    }

    @Transactional
    public List<String> selectCodeFindByCodeContaining(Collection<String> codes) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select c.code from CouponCode c where c.code in :codes",
                        String.class
                )
                .setParameter("codes", codes)
                .getResultList();
    }
}
