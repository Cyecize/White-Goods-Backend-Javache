package com.cyecize.app.api.carousel;

import com.cyecize.app.api.user.User;

import java.util.List;

public interface HomeCarouselService {
    List<HomeCarousel> getHomeCarousel(boolean showHidden, User currentUser);

    HomeCarousel createCarouselItem(CreateCarouselDto dto);

    HomeCarousel editCarouselItem(Long id, EditCarouselDto dto);

    HomeCarousel getCarouselItem(Long id);
}
