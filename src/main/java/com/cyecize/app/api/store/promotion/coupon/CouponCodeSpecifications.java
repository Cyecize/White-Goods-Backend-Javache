package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.util.BetweenQuery;
import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.ReflectionUtils;
import com.cyecize.app.util.SortQuery;
import com.cyecize.app.util.Specification;
import java.time.LocalDateTime;
import org.apache.commons.lang3.StringUtils;

public class CouponCodeSpecifications {

    public static Specification<CouponCode> sort(SortQuery sortQuery) {
        if (!ReflectionUtils.fieldExists(CouponCode.class, sortQuery.getField())) {
            sortQuery.setField(CouponCode_.ID);
        }

        return (root, query, criteriaBuilder)
                -> QuerySpecifications.sort(
                        CouponCode.class,
                        root.get(sortQuery.getField()),
                        sortQuery.getDirection()
                )
                .toPredicate(root, query, criteriaBuilder);
    }

    public static Specification<CouponCode> expiryDateBetween(BetweenQuery<LocalDateTime> query) {
        return QuerySpecifications.between(CouponCode_.expiryDate, query);
    }

    public static Specification<CouponCode> containsCode(String code) {
        code = StringUtils.trimToNull(code);
        if (code == null) {
            return Specification.where(null);
        }
        return QuerySpecifications.contains(CouponCode_.code, code);
    }

    public static Specification<CouponCode> promotionIdEquals(Long promoId) {
        if (promoId == null) {
            return Specification.where(null);
        }
        return QuerySpecifications.equal(CouponCode_.promotionId, promoId);
    }

    public static Specification<CouponCode> enabled(Boolean enabled) {
        if (enabled == null) {
            return Specification.where(null);
        }

        return QuerySpecifications.equal(CouponCode_.enabled, enabled);
    }
}
