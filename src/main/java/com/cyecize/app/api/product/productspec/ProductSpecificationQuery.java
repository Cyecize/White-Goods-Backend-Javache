package com.cyecize.app.api.product.productspec;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class ProductSpecificationQuery {

    @MinLength(length = 1, message = "At least one specification type required")
    private List<Long> specificationTypeIds;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private Boolean onlyAssignedValues;
}
