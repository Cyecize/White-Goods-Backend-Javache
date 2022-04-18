package com.cyecize.app.api.product.productspec;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSpecificationDtoDetailed extends ProductSpecificationDto {
    @JsonUnwrapped
    private SpecificationTypeDto specificationType;

    @Data
    static class SpecificationTypeDto {
        private String specificationType;
        private String titleBg;
        private String titleEn;
    }
}
