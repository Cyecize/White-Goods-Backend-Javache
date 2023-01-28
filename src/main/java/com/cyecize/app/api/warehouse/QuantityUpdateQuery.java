package com.cyecize.app.api.warehouse;

import com.cyecize.app.util.PageQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuantityUpdateQuery {

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;
}
