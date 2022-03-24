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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

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

        return typeQuery.getResultList().stream().findFirst().orElse(null);
    }

    @Transactional
    public <T> Page<T> findAll(Specification<T> specification,
                               PageQuery pageQuery,
                               Class<T> returnType,
                               @Nullable String entityGraph) {
        final Page<Long> page = this.selectIdFindAll(specification, pageQuery, returnType);
        if (page.getElements().isEmpty()) {
            return new Page<>(List.of(), pageQuery);
        }

        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<T> query = criteriaBuilder.createQuery(returnType);
        final Root<T> root = query.from(returnType);

        final CriteriaQuery<T> queryForOrder = criteriaBuilder.createQuery(returnType);
        final List<Order> orderList = queryForOrder
                .where(specification.toPredicate(root, queryForOrder, criteriaBuilder))
                .getOrderList();

        //TODO: use reflection here to get the field that is annotated with javax.persistence.Id
        query.where(root.get("id").in(page.getElements()));
        query.orderBy(orderList);

        final TypedQuery<T> typedQuery = entityManager.createQuery(query);
        if (entityGraph != null) {
            typedQuery.setHint(General.HIBERNATE_HINT_ENTITY_GRAPH, entityManager.getEntityGraph(entityGraph));
        }

        final List<T> results = typedQuery.getResultList();
        return new Page<>(results, page.getItemsCount(), pageQuery);
    }

    private <T> Page<Long> selectIdFindAll(Specification<T> specification, PageQuery pageQuery, Class<T> returnType) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        final Root<T> root = query.from(returnType);

        //TODO: use reflection here to get the field that is annotated with javax.persistence.Id
        query.select(root.get("id"));
        query.where(specification.toPredicate(root, query, criteriaBuilder));

        final TypedQuery<Long> typedQuery = entityManager.createQuery(query)
                .setMaxResults(pageQuery.getSize())
                .setFirstResult(pageQuery.getPage() * pageQuery.getSize());

        final List<Long> resultList = typedQuery.getResultList();

        final Long count;
        if (pageQuery.getPage() > 0 || resultList.size() == pageQuery.getSize()) {
            count = this.countBySpecification(specification, returnType);
        } else {
            count = (long) resultList.size();
        }

        return new Page<>(resultList, count, pageQuery);
    }

    private <T> Long countBySpecification(Specification<T> specification,
                                          Class<T> entityType) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();

        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = criteriaBuilder.createQuery(Long.class);
        final Root<T> root = query.from(entityType);

        query.select(criteriaBuilder.count(root));

        final Predicate predicate = specification.toPredicate(root, query, criteriaBuilder);
        query.where(predicate);

        return entityManager.createQuery(query).getSingleResult();
    }

    @Transactional
    public <T> List<T> findAll(Specification<T> specification, Class<T> returnType, @Nullable String entityGraph) {
        final EntityManager entityManager = this.transactionContext.getEntityManagerForTransaction();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();

        final CriteriaQuery<T> query = criteriaBuilder.createQuery(returnType);
        final Root<T> root = query.from(returnType);

        query.where(specification.toPredicate(root, query, criteriaBuilder));

        final TypedQuery<T> typedQuery = entityManager.createQuery(query);
        if (entityGraph != null) {
            typedQuery.setHint(General.HIBERNATE_HINT_ENTITY_GRAPH, entityManager.getEntityGraph(entityGraph));
        }

        return typedQuery.getResultList();
    }
}
