package com.cyecize.app.api.store.promotion.coupon;

import com.cyecize.app.util.BetweenQueryDate;
import com.cyecize.app.util.PageQuery;
import com.cyecize.app.util.SortQuery;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CouponCodeQuery {

    @Valid
    @NotNull(message = "Sort Query Required")
    private SortQuery sort;

    @Valid
    @NotNull(message = "Page Query Required")
    private PageQuery page;

    private String code;

    private Long promotionId;

    private BetweenQueryDate expiryDate;

    private Boolean enabled;
}
