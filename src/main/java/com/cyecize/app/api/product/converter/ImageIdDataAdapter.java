package com.cyecize.app.api.product.converter;

import com.cyecize.app.api.product.Image;
import com.cyecize.app.api.product.ImageService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.summer.areas.validation.interfaces.DataAdapter;
import com.cyecize.summer.common.annotations.Component;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

@Component
@RequiredArgsConstructor
public class ImageIdDataAdapter implements DataAdapter<Image> {
    private final ImageService imageService;

    @Override
    public Image resolve(String paramName, HttpSoletRequest request) {
        if (StringUtils.trimToNull(paramName) == null) {
            return null;
        }

        final long imageId = NumberUtils.toLong(request.getBodyParam(paramName), Long.MIN_VALUE);
        if (imageId == Long.MIN_VALUE) {
            return null;
        }

        return this.imageService.findById(imageId);
    }
}
