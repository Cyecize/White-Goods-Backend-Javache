package com.cyecize.app.api.store.order;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;
import java.util.List;

public class OrderSpecifications {

    public static Specification<Order> sort(SortQuery sortQuery) {
        if (!ReflectionUtils.fieldExists(Order.class, sortQuery.getField())) {
            sortQuery.setField(Order_.ID);
        }

        return (root, query, criteriaBuilder)
                -> QuerySpecifications.sort(Order.class, root.get(sortQuery.getField()),
                        sortQuery.getDirection())
                .toPredicate(root, query, criteriaBuilder);
    }

    public static Specification<Order> userIdEquals(Long userId) {
        if (userId == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(
                root.get(Order_.userId),
                userId
        );
    }

    public static Specification<Order> statusContains(List<OrderStatus> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, cb) -> root.get(Order_.status).in(statuses);
    }
}
