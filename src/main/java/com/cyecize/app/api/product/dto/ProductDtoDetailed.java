package com.cyecize.app.api.product.dto;

import com.cyecize.app.api.product.Image;
import com.cyecize.app.api.product.productspec.ProductSpecificationDtoDetailed;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDtoDetailed extends ProductDto {

    @JsonUnwrapped
    private CategoryDto category;

    private List<ProductSpecificationDtoDetailed> specifications;

    private final List<String> imageGallery = new ArrayList<>();

    public void setImages(List<Image> images) {
        if (images != null) {
            this.imageGallery.addAll(
                    images.stream().map(Image::getImageUrl).collect(Collectors.toList()));
        }
    }

    public void setImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
        this.imageGallery.add(imageUrl);
    }

    @Data
    static class CategoryDto {

        @JsonProperty("categoryNameBg")
        private String nameBg;

        @JsonProperty("categoryNameEn")
        private String nameEn;
    }
}
