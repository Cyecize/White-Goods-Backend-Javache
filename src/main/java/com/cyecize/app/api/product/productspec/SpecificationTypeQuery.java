package com.cyecize.app.api.product.productspec;

import com.cyecize.app.util.PageQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class SpecificationTypeQuery {

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;

    @NotNull
    private List<Long> categoryIds;
}
