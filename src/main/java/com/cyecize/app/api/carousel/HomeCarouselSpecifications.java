package com.cyecize.app.api.carousel;

import com.cyecize.app.util.QuerySpecifications;
import com.cyecize.app.util.SortDirection;
import com.cyecize.app.util.Specification;

public final class HomeCarouselSpecifications {

    public static Specification<HomeCarousel> enabled(Boolean enabled) {
        if (enabled == null) {
            return Specification.where(null);
        }

        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(HomeCarousel_.enabled), enabled);
    }

    public static Specification<HomeCarousel> applyOrder() {
        return (root, query, criteriaBuilder) -> QuerySpecifications.sort(
                HomeCarousel.class,
                root.get(HomeCarousel_.orderNumber),
                SortDirection.ASC
        ).toPredicate(root, query, criteriaBuilder);
    }
}
