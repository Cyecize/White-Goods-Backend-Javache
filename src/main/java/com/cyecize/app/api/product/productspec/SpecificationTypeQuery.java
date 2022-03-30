package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.PageQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class SpecificationTypeQuery {

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;

    @MinLength(length = 1, message = "Expecting at least one category")
    private List<Long> categoryIds;
}
