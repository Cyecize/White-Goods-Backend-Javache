package com.cyecize.app.api.store.promotion;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;

public class PromotionSpecifications {

    public static Specification<Promotion> sort(SortQuery sortQuery) {
        if (!ReflectionUtils.fieldExists(Promotion.class, sortQuery.getField())) {
            sortQuery.setField(Promotion_.ID);
        }

        return (root, query, criteriaBuilder)
                -> QuerySpecifications.sort(
                        Promotion.class,
                        root.get(sortQuery.getField()),
                        sortQuery.getDirection()
                )
                .toPredicate(root, query, criteriaBuilder);
    }
}
