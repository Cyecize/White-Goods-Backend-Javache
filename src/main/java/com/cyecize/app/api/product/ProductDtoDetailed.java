package com.cyecize.app.api.product;

import com.cyecize.app.api.product.productspec.ProductSpecificationDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDtoDetailed extends ProductDto {
    private String categoryNameBg;

    private String categoryNameEn;

    private List<ProductSpecificationDto> specifications;

    private final List<String> gallery = new ArrayList<>();

    public void setImages(List<Image> images) {
        if (images != null) {
            this.gallery.addAll(images.stream().map(Image::getImageUrl).collect(Collectors.toList()));
        }
    }

    public void setImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
        this.gallery.add(imageUrl);
    }
}
