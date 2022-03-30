package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.ProductCategory;
import com.cyecize.app.api.product.ProductCategory_;
import com.cyecize.app.util.Specification;

import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ListJoin;
import java.util.Collection;

public final class SpecificationTypeSpecifications {

    public static Specification<SpecificationType> categoryContains(Collection<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> {
            final ListJoin<SpecificationType, ProductCategory> categories = root.join(
                    SpecificationType_.categories, JoinType.INNER
            );

            categories.on(categories.get(ProductCategory_.id).in(categoryIds));

            return criteriaBuilder.conjunction();
        };
    }
}
