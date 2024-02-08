package com.cyecize.app.api.store.promotion;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;
import java.util.Collection;
import org.apache.commons.lang3.StringUtils;

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

    public static Specification<Promotion> idEquals(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Promotion_.id), id);
    }

    public static Specification<Promotion> promotionTypeIn(Collection<PromotionType> types) {
        if (types == null || types.isEmpty()) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> root.get(Promotion_.promotionType).in(types);
    }

    public static Specification<Promotion> nameContains(String name) {
        name = StringUtils.trimToNull(name);
        if (name == null) {
            return Specification.where(null);
        }

        return QuerySpecifications.contains(Promotion_.nameEn, name)
                .or(QuerySpecifications.contains(Promotion_.nameBg, name));
    }
}
