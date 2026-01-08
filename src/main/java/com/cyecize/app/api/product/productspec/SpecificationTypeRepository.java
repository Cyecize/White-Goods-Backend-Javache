package com.cyecize.app.api.product.productspec;

import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Service;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SpecificationTypeRepository {

    private final TransactionContext transactionContext;

    @Transactional
    public void merge(SpecificationType specificationType) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.merge(specificationType);
    }

    @Transactional
    public SpecificationType persist(SpecificationType specificationType) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        entityManager.persist(specificationType);
        return specificationType;
    }

    @Transactional
    public boolean existsById(Long id) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select case when (count(st) > 0)  then true else false end from SpecificationType st where st.id = ?1",
                        Boolean.class
                ).setParameter(1, id)
                .getSingleResult();
    }

    @Transactional
    public boolean existsBySpecificationType(String name) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        return entityManager.createQuery(
                        "select case when (count(st) > 0)  then true else false end from SpecificationType st "
                                + "where st.specificationType = ?1",
                        Boolean.class
                ).setParameter(1, name)
                .getSingleResult();
    }
}
