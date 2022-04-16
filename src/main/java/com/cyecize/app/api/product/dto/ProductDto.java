package com.cyecize.app.api.product.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class ProductDto {

    private Long id;

    private Long categoryId;

    @JsonProperty("name")
    private String productName;

    private Double price;

    private String descriptionBg;

    private String descriptionEn;

    private String imageUrl;

    private Boolean enabled;

    private final List<String> tags = new ArrayList<>();

    public void setTags(List<TagDto> tags) {
        if (tags == null) {
            return;
        }

        this.tags.addAll(tags.stream().map(TagDto::getName).collect(Collectors.toList()));
    }
}
