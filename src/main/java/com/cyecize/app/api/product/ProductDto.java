package com.cyecize.app.api.product;

import lombok.Data;

import java.util.List;

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

    private List<TagDto> tags;

//    private $imageGallery;
//    private $specifications;
}
