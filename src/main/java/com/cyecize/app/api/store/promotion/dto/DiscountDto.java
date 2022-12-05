package com.cyecize.app.api.store.promotion.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class DiscountDto {

    private String nameBg;

    private String nameEn;

    private Double value;

    @JsonIgnore
    private final boolean freeDelivery;

    private List<DiscountProductItemDto> productItems;
}
