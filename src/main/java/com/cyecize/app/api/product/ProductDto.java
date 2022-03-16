package com.cyecize.app.api.product;

import com.cyecize.app.api.product.productspec.ProductSpecificationDto;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDto {

    private Long id;

    private String categoryNameBg;

    private String categoryNameEn;

    private String productName;

    private Double price;

    private String descriptionBg;

    private String descriptionEn;

    private String imageUrl;

    private final List<String> tags = new ArrayList<>();

    public void setTags(List<TagDto> tags) {
        if (tags == null) {
            return;
        }

        this.tags.addAll(tags.stream().map(TagDto::getName).collect(Collectors.toList()));
    }

    private List<ProductSpecificationDto> specifications;

    private final List<String> gallery = new ArrayList<>();

    public void setImages(List<Image> images) {
        if (images != null) {
            this.gallery.addAll(images.stream().map(Image::getImageUrl).collect(Collectors.toList()));
        }
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        this.gallery.add(imageUrl);
    }
}
