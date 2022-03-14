package com.cyecize.app.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

public interface Specification<T> extends Serializable {

    long serialVersionUID = 1L;

    /**
     * Negates the given {@link Specification}.
     *
     * @param <T>  the type of the {@link Root} the resulting {@literal Specification} operates on.
     * @param spec can be {@literal null}.
     * @return guaranteed to be not {@literal null}.
     * @since 2.0
     */
    static <T> Specification<T> not(Specification<T> spec) {

        return spec == null //
                ? (root, query, builder) -> null //
                : (root, query, builder) -> builder.not(spec.toPredicate(root, query, builder));
    }

    /**
     * Simple static factory method to add some syntactic sugar around a {@link Specification}.
     *
     * @param <T>  the type of the {@link Root} the resulting {@literal Specification} operates on.
     * @param spec can be {@literal null}.
     * @return guaranteed to be not {@literal null}.
     * @since 2.0
     */
    static <T> Specification<T> where(Specification<T> spec) {
        return spec == null ? (root, query, builder) -> null : spec;
    }

    /**
     * ANDs the given {@link Specification} to the current one.
     *
     * @param other can be {@literal null}.
     * @return The conjunction of the specifications
     * @since 2.0
     */
    default Specification<T> and(Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder::and);
    }

    /**
     * ORs the given specification to the current one.
     *
     * @param other can be {@literal null}.
     * @return The disjunction of the specifications
     * @since 2.0
     */
    default Specification<T> or(Specification<T> other) {
        return SpecificationComposition.composed(this, other, CriteriaBuilder::or);
    }

    /**
     * Creates a WHERE clause for a query of the referenced entity in form of a {@link Predicate} for the given
     * {@link Root} and {@link CriteriaQuery}.
     *
     * @param root            must not be {@literal null}.
     * @param query           must not be {@literal null}.
     * @param criteriaBuilder must not be {@literal null}.
     * @return a {@link Predicate}, may be {@literal null}.
     */
    Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder);
}