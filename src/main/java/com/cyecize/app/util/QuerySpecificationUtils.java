package com.cyecize.app.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.From;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import org.apache.commons.lang3.BooleanUtils;

public final class QuerySpecificationUtils {

    public static <T extends Comparable<? super T>> Predicate getLesserPredicate(T value,
            CriteriaBuilder criteriaBuilder,
            Expression<T> field,
            boolean inclusive) {
        if (value == null) {
            return criteriaBuilder.conjunction();
        }

        if (inclusive) {
            return criteriaBuilder.lessThanOrEqualTo(field, value);
        }

        return criteriaBuilder.lessThan(field, value);
    }

    public static <T extends Comparable<? super T>> Predicate getGreaterPredicate(T value,
            CriteriaBuilder criteriaBuilder,
            Expression<T> field,
            boolean inclusive) {
        if (value == null) {
            return criteriaBuilder.conjunction();
        }

        if (inclusive) {
            return criteriaBuilder.greaterThanOrEqualTo(field, value);
        }

        return criteriaBuilder.greaterThan(field, value);
    }

    /**
     * @param attr  - Field to compare
     * @param query -
     * @param cb    -
     * @param root  - Path from which the field can be accessed. For joins pass {@link Join} and for
     *              sub queries pass subroot.
     * @param <T>   - Entity type
     * @param <V>   - Field value type
     * @return predicate
     */
    public static <T, V extends Comparable<? super V>> Predicate betweenPredicate(
            SingularAttribute<T, V> attr,
            BetweenQuery<V> query,
            CriteriaBuilder cb,
            Path<T> root,
            boolean inclusive) {
        if (query == null) {
            return cb.conjunction();
        }

        boolean notBetween = BooleanUtils.isTrue(query.getNotBetween());

        if (query.getMin() != null && query.getMax() != null && inclusive && !notBetween) {
            return cb.between(root.get(attr), query.getMin(), query.getMax());
        }

        if (notBetween) {
            return cb.or(
                    getGreaterPredicate(query.getMax(), cb, root.get(attr), inclusive),
                    getLesserPredicate(query.getMin(), cb, root.get(attr), inclusive)
            );
        }

        return cb.and(
                getGreaterPredicate(query.getMin(), cb, root.get(attr), inclusive),
                getLesserPredicate(query.getMax(), cb, root.get(attr), inclusive)
        );
    }

    /**
     * Using this method ensures that only one join will be made across all executed
     * specifications.
     */
    @SuppressWarnings("unchecked")
    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, ?> from, String attribute,
            boolean fetch) {
        Join<?, ?> join = from.getJoins().stream()
                .filter(j -> j.getAttribute().getName().equals(attribute))
                .findFirst().orElse(null);

        if (join == null) {
            join = (Join<?, ?>) from.getFetches().stream()
                    .filter(j -> j.getAttribute().getName().equals(attribute))
                    .findFirst().orElse(null);
        }

        if (join == null) {
            if (fetch) {
                join = (Join<?, ?>) from.fetch(attribute);
            } else {
                join = from.join(attribute, JoinType.LEFT);
            }
        }

        return (Join<X, Y>) join;
    }

    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, ?> from,
            ListAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), false);
    }

    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, ?> from, SetAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), false);
    }

    public static <X, Y> Join<X, Y> getOrCreateJoin(From<?, ?> from,
            SingularAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), false);
    }

    public static <X, Y> Join<X, Y> getOrCreateFetch(From<?, ?> from,
            ListAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), true);
    }

    public static <X, Y> Join<X, Y> getOrCreateFetch(From<?, ?> from,
            SetAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), true);
    }

    public static <X, Y> Join<X, Y> getOrCreateFetch(From<?, ?> from,
            SingularAttribute<X, Y> attribute) {
        return getOrCreateJoin(from, attribute.getName(), true);
    }
}
