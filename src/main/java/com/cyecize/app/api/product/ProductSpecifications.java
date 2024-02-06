package com.cyecize.app.api.product;

import com.cyecize.app.api.product.productspec.ProductSpecification;
import com.cyecize.app.api.product.productspec.ProductSpecification_;
import com.cyecize.app.api.user.User;
import com.cyecize.app.util.AuthUtils;
import com.cyecize.app.util.BetweenQuery;
import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.SetJoin;
import javax.persistence.criteria.Subquery;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

public class ProductSpecifications {

    public static Specification<Product> idEquals(Long id) {
        if (id == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.id), id);
    }

    public static Specification<Product> idContains(Collection<Long> ids) {
        return idContains(ids, true);
    }

    public static Specification<Product> idContains(Collection<Long> ids, boolean fail) {
        if (ids == null || ids.isEmpty()) {
            return fail ? Specification.not(null) : Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> root.get(Product_.id).in(ids);
    }

    public static Specification<Product> enabled(Boolean enabled) {
        if (enabled == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Product_.enabled), enabled);
    }

    public static Specification<Product> showHidden(Boolean showHidden, User currentUser) {
        if (!BooleanUtils.toBoolean(showHidden) || !AuthUtils.hasAdminRole(currentUser)) {
            return (root, query, cb) -> cb.isTrue(root.get(Product_.enabled));
        }

        return Specification.where(null);
    }

    public static Specification<Product> betweenId(BetweenQuery<Long> id) {
        return QuerySpecifications.between(Product_.id, id);
    }

    public static Specification<Product> sort(SortQuery sortQuery) {
        if (!ReflectionUtils.fieldExists(Product.class, sortQuery.getField())) {
            sortQuery.setField(Product_.DEFAULT_DISPLAY_ORDER);
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

    public static Specification<Product> includesAllSpecifications(Map<Long, List<Long>> specifications) {
        if (specifications == null || specifications.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> {
            final Subquery<Long> subquery = query.subquery(Long.class);
            final Root<Product> subRoot = subquery.from(Product.class);

            final SetJoin<Product, ProductSpecification> specs = subRoot.join(Product_.specifications, JoinType.INNER);

            subquery.select(specs.get(ProductSpecification_.id));
            subquery.where(criteriaBuilder.equal(root, subRoot));

            Predicate finalPredicate = criteriaBuilder.conjunction();
            for (Map.Entry<Long, List<Long>> specsEntry : specifications.entrySet()) {
                if (specsEntry.getValue().isEmpty()) {
                    continue;
                }

                Predicate specTypePredicate = criteriaBuilder.disjunction();
                for (Long specId : specsEntry.getValue()) {
                    specTypePredicate = criteriaBuilder.or(
                            specTypePredicate,
                            criteriaBuilder.literal(specId).in(subquery)
                    );
                }

                finalPredicate = criteriaBuilder.and(finalPredicate, specTypePredicate);
            }

            return finalPredicate;
        };
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
