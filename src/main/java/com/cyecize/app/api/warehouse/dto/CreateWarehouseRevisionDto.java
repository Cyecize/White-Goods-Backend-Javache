package com.cyecize.app.api.warehouse.dto;

import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import java.util.List;
import lombok.Data;

@Data
public class CreateWarehouseRevisionDto {

    @Valid
    @MinLength(length = 1, message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = General.MAX_ALLOWED_BULK_UPDATE_ITEMS, message = "Too many items!")
    private List<CreateQuantityUpdateDto> items;
}
