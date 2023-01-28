package com.cyecize.app.api.warehouse.dto;

import com.cyecize.app.api.warehouse.QuantityUpdateType;
import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class QuantityUpdateDto {

    private Long id;

    private long productId;

    private Long deliveryId;

    private Long orderId;

    private QuantityUpdateType updateType;

    @DateTimeConverter
    private LocalDateTime date;

    private int quantityValue;
}
