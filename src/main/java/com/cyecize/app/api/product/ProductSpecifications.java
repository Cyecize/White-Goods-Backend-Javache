package com.cyecize.app.api.product;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.criteria.JoinType;
import java.util.List;

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

    public static Specification<Product> sort(SortQuery sortQuery) {
        if (!ReflectionUtils.fieldExists(Product.class, sortQuery.getField())) {
            sortQuery.setField(Product_.ID);
        }

        return (root, query, criteriaBuilder)
                -> QuerySpecifications.sort(Product.class, root.get(sortQuery.getField()), sortQuery.getDirection())
                .toPredicate(root, query, criteriaBuilder);
    }

    public static Specification<Product> categoryIdContains(List<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> root.get(Product_.categoryId).in(categoryIds);
    }

    public static Specification<Product> containsText(String text) {
        if (StringUtils.trimToNull(text) == null) {
            return Specification.where(null);
        }

        return QuerySpecifications.contains(Product_.productName, text)
                .or(QuerySpecifications.contains(Product_.descriptionBg, text))
                .or(QuerySpecifications.contains(Product_.descriptionEn, text));
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
