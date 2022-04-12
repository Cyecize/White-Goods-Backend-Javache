package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.Specification;

import java.util.Collection;

public final class ProductSpecificationSpecifications {

    public static Specification<ProductSpecification> specificationTypeIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> root.get(ProductSpecification_.specificationTypeId).in(ids);
    }

    public static Specification<ProductSpecification> idIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> root.get(ProductSpecification_.id).in(ids);
    }
}
