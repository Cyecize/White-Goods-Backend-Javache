package com.cyecize.app.api.product;

import com.cyecize.app.util.Specification;

import javax.persistence.criteria.JoinType;

public class ProductSpecifications {

    public static Specification<Product> idEquals(Long id) {
        if (id == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.id), id);
    }

    public static Specification<Product> enabled(Boolean enabled) {
        if (enabled == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.enabled), enabled);
    }

    public static Specification<Product> fetchTags() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final Class<?> clazz = criteriaQuery.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                return null;
            }

            root.fetch(Product_.tags, JoinType.LEFT);

            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Product> fetchCategory() {
        return (root, criteriaQuery, criteriaBuilder) -> {
            final Class<?> clazz = criteriaQuery.getResultType();
            if (clazz.equals(Long.class) || clazz.equals(long.class)) {
                return null;
            }

            root.fetch(Product_.category, JoinType.LEFT);

            return criteriaBuilder.conjunction();
        };
    }
}
