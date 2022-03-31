package com.cyecize.app.api.product.productspec;

import lombok.Data;

@Data
public class ProductSpecificationDto {

    private Long id;

    private String valueBg;

    private String valueEn;

    private Long specificationTypeId;
}
