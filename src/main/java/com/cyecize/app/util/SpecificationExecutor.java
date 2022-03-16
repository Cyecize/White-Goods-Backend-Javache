package com.cyecize.app.util;

import com.cyecize.app.constants.General;
import com.cyecize.app.integration.transaction.TransactionContext;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.ioc.annotations.Nullable;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Component
@RequiredArgsConstructor
public class SpecificationExecutor {

    private final TransactionContext transactionContext;

    @Transactional
    public <T> T findOne(Specification<T> specification, Class<T> returnType, @Nullable String entityGraph) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<T> query = criteriaBuilder.createQuery(returnType);
        final Root<T> root = query.from(returnType);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        final TypedQuery<T> typeQuery = entityManager.createQuery(query);
        if (entityGraph != null) {
            typeQuery.setHint(General.HIBERNATE_HINT_ENTITY_GRAPH, entityManager.getEntityGraph(entityGraph));
        }

        typeQuery.setMaxResults(1);

        return typeQuery.getResultList().stream().findFirst().orElse(null);
    }
}
