package com.cyecize.app.api.store.promotion.coupon;


import com.cyecize.app.api.store.order.dto.OrderCouponCodeStatisticsDto;
import lombok.Data;

@Data
public class CouponCodeStatisticDto {

    private final OrderCouponCodeStatisticsDto orderStatistics;
}
