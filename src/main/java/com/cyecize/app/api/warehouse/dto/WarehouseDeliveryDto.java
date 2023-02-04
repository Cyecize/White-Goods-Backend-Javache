package com.cyecize.app.api.warehouse.dto;

import com.cyecize.app.converters.DateTimeConverter;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WarehouseDeliveryDto {

    private Long id;

    @DateTimeConverter
    private LocalDateTime date;

    private List<QuantityUpdateDto> items;
}
