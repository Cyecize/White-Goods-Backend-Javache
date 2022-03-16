package com.cyecize.app.api.product;

import lombok.Data;

import java.util.List;

@Data
public class ProductCategoryDto {

    private Long id;

    private String nameEn;

    private String nameBg;

    private String imageUrl;

    private List<TagDto> tags;
}
