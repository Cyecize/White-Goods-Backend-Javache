package com.cyecize.app.api.store.order.dto;

import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.converters.DateTimeConverter;
import com.cyecize.app.converters.GenericEnumConverter;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class OrderDtoSimple {

    private Long id;

    private Long userId;

    @GenericEnumConverter
    private OrderStatus status;

    @DateTimeConverter
    private LocalDateTime date;
}
