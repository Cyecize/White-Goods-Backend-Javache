package com.cyecize.app.api.product.productspec;

import com.cyecize.summer.areas.validation.constraints.MinLength;
import lombok.Data;

import java.util.List;

@Data
public class ProductSpecificationQuery {

    @MinLength(length = 1, message = "At least one specification type required")
    private List<Long> specificationTypeIds;
}
