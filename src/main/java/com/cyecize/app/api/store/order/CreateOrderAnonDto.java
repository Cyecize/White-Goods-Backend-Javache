package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.delivery.CreateAddressDto;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.Valid;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderAnonDto {

    @Valid
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private CreateAddressDto address;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String sessionId;
}
