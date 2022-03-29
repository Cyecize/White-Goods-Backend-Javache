package com.cyecize.app.api.product.productspec;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import lombok.Data;

@Data
public class SpecificationDto {

    private Long id;

    private String valueBg;

    private String valueEn;

    @JsonUnwrapped
    private SpecificationTypeDto specificationType;

    @Data
    static class SpecificationTypeDto {
        private String titleBg;

        private String titleEn;
    }
}
