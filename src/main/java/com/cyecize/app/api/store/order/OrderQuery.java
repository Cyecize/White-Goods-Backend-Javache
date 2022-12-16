package com.cyecize.app.api.store.order;

import com.cyecize.app.util.PageQuery;
import com.cyecize.app.util.SortQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class OrderQuery {
    @Valid
    @NotNull(message = "Sort Query Required")
    private SortQuery sort;

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;

    private List<OrderStatus> statuses;
}
