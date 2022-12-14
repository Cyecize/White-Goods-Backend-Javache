package com.cyecize.app.api.store.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OrderStatus {
    WAITING("order.accepted"),
    IN_PROGRESS("order.in.progress"),
    SENT("order.sent"),
    COMPLETED("order.completed");

    @Getter
    private final String message;
}
