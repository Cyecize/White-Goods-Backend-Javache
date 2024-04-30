package com.cyecize.app.api.store.promotion.coupon;

import static com.cyecize.app.constants.ValidationMessages.FIELD_CANNOT_BE_NULL;

import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.util.BetweenQueryDate;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class CouponCodeStatisticQuery {

    @NotEmpty(message = FIELD_CANNOT_BE_NULL)
    private String code;

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    private BetweenQueryDate date;

    @NotNull(message = FIELD_CANNOT_BE_NULL)
    private List<OrderStatus> statuses;
}
