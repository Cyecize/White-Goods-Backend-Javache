package com.cyecize.app.api.carousel;

import java.util.List;

public interface HomeCarouselService {
    List<HomeCarousel> getHomeCarousel();

    HomeCarousel createCarouselItem(CreateCarouselDto dto);
}
