package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileBindingModel;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface ImageService {
    void removeImage(Image image);

    void persistAll(Collection<Image> images);

    Set<Image> createImages(Collection<Base64FileBindingModel> imageDtos);

    List<Image> findByProductId(Long productId);

    Image findById(long imageId);
}
