package com.cyecize.app.api.carousel;

import com.cyecize.app.util.Specification;
import com.cyecize.app.util.SpecificationExecutor;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomeCarouselServiceImpl implements HomeCarouselService {

    private final SpecificationExecutor specificationExecutor;

    @Override
    public List<HomeCarousel> getHomeCarousel() {
        final Specification<HomeCarousel> specification = HomeCarouselSpecifications.enabled(true)
                .and(HomeCarouselSpecifications.applyOrder());

        return this.specificationExecutor.findAll(specification, HomeCarousel.class, null);
    }
}
