package com.cyecize.app.api.store.order.dto;

import lombok.Data;

@Data
public class OrderCouponCodeStatisticsDto {

    private final Double subtotal;
    private final Double total;
    private final Double totalDiscounts;
    private final Double totalDelivery;
}
