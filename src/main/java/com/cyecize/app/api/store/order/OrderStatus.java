package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.order.orderstatus.OrderCancelledStatusApplier;
import com.cyecize.app.api.store.order.orderstatus.OrderCompletedStatusApplier;
import com.cyecize.app.api.store.order.orderstatus.OrderSentStatusApplier;
import com.cyecize.app.api.store.order.orderstatus.OrderStatusApplier;
import com.cyecize.app.api.store.order.validator.orderstatus.CancelledOrderStatusValidator;
import com.cyecize.app.api.store.order.validator.orderstatus.CompletedOrderStatusValidator;
import com.cyecize.app.api.store.order.validator.orderstatus.InProgressOrderStatusValidator;
import com.cyecize.app.api.store.order.validator.orderstatus.OrderStatusValidator;
import com.cyecize.app.api.store.order.validator.orderstatus.SentOrderStatusValidator;
import com.cyecize.app.api.store.order.validator.orderstatus.SimpleOrderStatusValidator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    WAITING(
            "order.accepted",
            new SimpleOrderStatusValidator(false),
            (order, dto, orderService) -> {
            }
    ),
    IN_PROGRESS(
            "order.in.progress",
            new InProgressOrderStatusValidator(),
            (order, dto, orderService) -> order.setStatus(dto.getOrderStatus())
    ),
    SENT(
            "order.sent",
            new SentOrderStatusValidator(),
            new OrderSentStatusApplier()
    ),
    COMPLETED(
            "order.completed",
            new CompletedOrderStatusValidator(),
            new OrderCompletedStatusApplier()
    ),
    CANCELLED(
            "order.cancelled",
            new CancelledOrderStatusValidator(),
            new OrderCancelledStatusApplier()
    );

    @Getter
    private final String message;

    @Getter
    private final OrderStatusValidator validator;

    @Getter
    private final OrderStatusApplier statusApplier;
}
