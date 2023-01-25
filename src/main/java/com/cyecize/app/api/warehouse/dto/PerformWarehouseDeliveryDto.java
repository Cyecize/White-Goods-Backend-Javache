package com.cyecize.app.api.warehouse.dto;

import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import java.util.List;
import lombok.Data;

@Data
public class PerformWarehouseDeliveryDto {

    @Valid
    @MinLength(length = 1, message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private List<CreateQuantityUpdateDto> items;
}
