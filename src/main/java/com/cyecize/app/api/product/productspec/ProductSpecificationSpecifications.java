package com.cyecize.app.api.product.productspec;

import com.cyecize.app.api.product.Product;
import com.cyecize.app.api.product.Product_;
import com.cyecize.app.util.QuerySpecificationUtils;
import com.cyecize.app.util.Specification;
import java.util.Collection;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

public final class ProductSpecificationSpecifications {

    public static Specification<ProductSpecification> specificationTypeIdIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Specification.not(null);
        }

        return (root, query, cb) -> root.get(ProductSpecification_.specificationTypeId).in(ids);
    }

    public static Specification<ProductSpecification> assignedToProduct() {
        return (root, query, cb) -> {
            final Subquery<Long> subQuery = query.subquery(Long.class);
            final Root<Product> subRoot = subQuery.from(Product.class);

            subQuery.select(cb.count(subRoot.get(Product_.id)));
            final var specs = QuerySpecificationUtils.getOrCreateJoin(
                    subRoot,
                    Product_.specifications
            );

            subQuery.where(cb.equal(
                    root.get(ProductSpecification_.id),
                    specs.get(ProductSpecification_.id)
            ));

            return cb.greaterThan(subQuery, 0L);
        };
    }

    public static Specification<ProductSpecification> idIn(Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return Specification.not(null);
        }

        return (root, query, criteriaBuilder) -> root.get(ProductSpecification_.id).in(ids);
    }
}
