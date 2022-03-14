package com.cyecize.app.util;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;

class SpecificationComposition {

    interface Combiner extends Serializable {
        Predicate combine(CriteriaBuilder builder, Predicate lhs, Predicate rhs);
    }

    static <T> Specification<T> composed(Specification<T> lhs, Specification<T> rhs,
                                         Combiner combiner) {

        return (root, query, builder) -> {

            Predicate thisPredicate = toPredicate(lhs, root, query, builder);
            Predicate otherPredicate = toPredicate(rhs, root, query, builder);

            if (thisPredicate == null) {
                return otherPredicate;
            }

            return otherPredicate == null ? thisPredicate : combiner.combine(builder, thisPredicate, otherPredicate);
        };
    }

    private static <T> Predicate toPredicate(Specification<T> specification, Root<T> root, CriteriaQuery<?> query,
                                             CriteriaBuilder builder) {
        return specification == null ? null : specification.toPredicate(root, query, builder);
    }
}