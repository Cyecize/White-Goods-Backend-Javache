package com.cyecize.app.api.product;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductCategoryDto {

    private Long id;

    private String nameEn;

    private String nameBg;

    private String imageUrl;

    private final List<String> tags = new ArrayList<>();

    public void setTags(List<TagDto> tags) {
        if (tags == null) {
            return;
        }

        this.tags.addAll(tags.stream().map(TagDto::getName).collect(Collectors.toList()));
    }
}
