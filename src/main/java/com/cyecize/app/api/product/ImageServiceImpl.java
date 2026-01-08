package com.cyecize.app.api.product;

import com.cyecize.app.api.base64.Base64FileBindingModel;
import com.cyecize.app.api.base64.Base64FileService;
import com.cyecize.summer.common.annotations.Service;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;
    private final Base64FileService base64FileService;

    @Override
    public void removeImage(Image image) {
        this.base64FileService.removeFile(image.getImageUrl());
        this.imageRepository.delete(image);
    }

    @Override
    public void persistAll(Collection<Image> images) {
        this.imageRepository.persistAll(images);
    }

    @Override
    public Set<Image> createImages(Collection<Base64FileBindingModel> imageDtos) {
        final Set<Image> images = new HashSet<>();
        if (imageDtos == null) {
            return images;
        }

        for (Base64FileBindingModel imageDto : imageDtos) {
            final Image image = new Image();
            image.setImageUrl(this.base64FileService.saveFile(imageDto));
            images.add(image);
        }

        return images;
    }

    @Override
    public List<Image> findByProductId(Long productId) {
        return this.imageRepository.findByProductId(productId);
    }

    @Override
    public Image findById(long imageId) {
        return this.imageRepository.findById(imageId);
    }
}
