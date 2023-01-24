package com.cyecize.app.api.product;

import com.cyecize.app.util.BetweenQuery;
import com.cyecize.app.util.PageQuery;
import com.cyecize.app.util.SortQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ProductQuery {

    @Valid
    @NotNull(message = "Sort Query Required")
    private SortQuery sort;

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;

    private List<Long> categoryIds;

    private Map<Long, List<Long>> specifications;

    private String search;

    private Boolean showHidden;

    private BetweenQuery<Long> id;
}
