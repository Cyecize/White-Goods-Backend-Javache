package com.cyecize.app.api.store.order.dto;

import com.cyecize.app.api.store.delivery.DeliveryAddressDto;
import com.cyecize.app.api.store.order.OrderStatus;
import com.cyecize.app.converters.DateTimeConverter;
import com.cyecize.app.converters.GenericEnumConverter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

@Data
public class OrderDto {

    private Long userId;

    private Double deliveryPrice;

    private Double totalDiscounts;

    private Double subtotal;

    private Double totalPrice;

    @GenericEnumConverter
    private OrderStatus status;

    @DateTimeConverter
    private LocalDateTime date;

    private Long addressId;

    private DeliveryAddressDto address;

    private List<OrderItemDto> items;
}
