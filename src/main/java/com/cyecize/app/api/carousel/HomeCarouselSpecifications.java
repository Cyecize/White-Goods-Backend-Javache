package com.cyecize.app.api.carousel;

import com.cyecize.app.api.user.User;
import com.cyecize.app.util.AuthUtils;
import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.SortDirection;
import com.cyecize.app.util.Specification;
import org.apache.commons.lang3.BooleanUtils;

public final class HomeCarouselSpecifications {

    public static Specification<HomeCarousel> showHidden(Boolean showHidden, User currentUser) {
        if (!BooleanUtils.toBoolean(showHidden) || !AuthUtils.hasAdminRole(currentUser)) {
            return (root, query, criteriaBuilder) -> criteriaBuilder.isTrue(
                    root.get(HomeCarousel_.enabled));
        }

        return Specification.where(null);
    }

    public static Specification<HomeCarousel> applyOrder() {
        return (root, query, criteriaBuilder) -> QuerySpecifications.sort(
                HomeCarousel.class,
                root.get(HomeCarousel_.orderNumber),
                SortDirection.ASC
        ).toPredicate(root, query, criteriaBuilder);
    }

    public static Specification<HomeCarousel> idEquals(Long id) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HomeCarousel_.id),
                id);
    }
}
