package com.cyecize.app.api.warehouse;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.SortDirection;
import com.cyecize.app.util.Specification;

public class QuantityUpdateSpecifications {

    public static Specification<QuantityUpdate> productIdEquals(Long prodId) {
        if (prodId == null) {
            return Specification.not(null);
        }

        return (root, query, cb) -> cb.equal(root.get(QuantityUpdate_.productId), prodId);
    }

    public static Specification<QuantityUpdate> deliveryIdEquals(Long deliveryId) {
        if (deliveryId == null) {
            return Specification.not(null);
        }

        return (root, query, cb) -> cb.equal(root.get(QuantityUpdate_.deliveryId), deliveryId);
    }

    public static Specification<QuantityUpdate> revisionIdEquals(Long revisionId) {
        if (revisionId == null) {
            return Specification.not(null);
        }

        return (root, query, cb) -> cb.equal(root.get(QuantityUpdate_.revisionId), revisionId);
    }

    public static Specification<QuantityUpdate> sortByIdDesc() {
        return (root, query, criteriaBuilder)
                -> QuerySpecifications.sort(
                        QuantityUpdate.class,
                        root.get(QuantityUpdate_.id),
                        SortDirection.DESC
                )
                .toPredicate(root, query, criteriaBuilder);
    }
}
