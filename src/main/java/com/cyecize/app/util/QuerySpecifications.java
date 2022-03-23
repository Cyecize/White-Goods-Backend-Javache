package com.cyecize.app.util;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Selection;
import javax.persistence.metamodel.SingularAttribute;

import static com.cyecize.app.util.QuerySpecificationUtils.betweenPredicate;
import static com.cyecize.app.util.QuerySpecificationUtils.getOrCreateJoin;

public final class QuerySpecifications {

    public static <T, V> Specification<T> equal(SingularAttribute<T, V> attribute, V value) {
        return (root, query, criteriaBuilder) -> equal(root.get(attribute), value, criteriaBuilder);
    }

    public static <T> Predicate equal(Expression<T> field, T value, CriteriaBuilder cb) {
        return cb.equal(field, value);
    }

    public static <T> Specification<T> contains(SingularAttribute<T, String> attribute, String text) {
        return (root, query, criteriaBuilder)
                -> contains(attribute.getDeclaringType().getJavaType(), root.get(attribute), text)
                .toPredicate(root, query, criteriaBuilder);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static <T> Specification<T> contains(Class<T> type, Expression expression, String text) {
        return (root, query, criteriaBuilder)
                -> criteriaBuilder.like(
                criteriaBuilder.lower(expression),
                "%" + StringUtils.lowerCase(text) + "%",
                '\\'
        );
    }

    public static <T, V extends Comparable<? super V>> Specification<T> between(SingularAttribute<T, V> attribute,
                                                                                BetweenQuery<V> query,
                                                                                boolean inclusive) {
        return (root, criteriaQuery, criteriaBuilder) -> betweenPredicate(
                attribute,
                query,
                criteriaBuilder,
                root,
                inclusive
        );
    }

    public static <T, V extends Comparable<? super V>> Specification<T> between(SingularAttribute<T, V> attribute,
                                                                                BetweenQuery<V> query) {
        return between(attribute, query, true);
    }

    /**
     * Creates a simple left join and applies between query on a field from the joined entity.
     *
     * @param join  - Field to join
     * @param attr  - Field from the joined entity to compare
     * @param query -
     * @param <T>   - Entity type
     * @param <J>   - Joined entity type
     * @param <V>   - Field value type
     * @return specification
     */
    public static <T, J, V extends Comparable<? super V>> Specification<T> betweenJoin(SingularAttribute<T, J> join,
                                                                                       SingularAttribute<J, V> attr,
                                                                                       BetweenQuery<V> query,
                                                                                       boolean inclusive) {
        return (root, criteriaQuery, criteriaBuilder) -> betweenPredicate(
                attr,
                query,
                criteriaBuilder,
                getOrCreateJoin(root, join),
                inclusive
        );
    }

    public static <T, J, V extends Comparable<? super V>> Specification<T> betweenJoin(SingularAttribute<T, J> join,
                                                                                       SingularAttribute<J, V> attr,
                                                                                       BetweenQuery<V> query) {
        return betweenJoin(join, attr, query, true);
    }

    public static <T> Specification<T> sort(Class<T> entity, Expression<?> field, SortDirection direction) {
        return (root, query, criteriaBuilder) -> {
            final Selection<?> selection = query.getSelection();
            if ((selection instanceof AggregationFunction)
                    && AggregationFunction.COUNT.class.isAssignableFrom(selection.getClass())) {
                return criteriaBuilder.conjunction();
            }

            Order order = criteriaBuilder.asc(field);
            if (direction == SortDirection.DESC) {
                order = criteriaBuilder.desc(field);
            }

            query.orderBy(order);

            return criteriaBuilder.conjunction();
        };
    }
}
