package com.cyecize.app.api.carousel;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class HomeCarouselIdDataAdapter implements DataAdapter<HomeCarousel> {
    private final HomeCarouselService homeCarouselService;

    @Override
    public HomeCarousel resolve(String paramName, HttpSoletRequest request) {
        if (StringUtils.trimToNull(paramName) == null) {
            return null;
        }

        long itemId = NumberUtils.toLong(request.getBodyParam(paramName), Long.MIN_VALUE);
        if (itemId == Long.MIN_VALUE) {
            return null;
        }

        return this.homeCarouselService.getCarouselItem(itemId);
    }
}
