package com.cyecize.app.api.store.order;

import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.api.user.address.converter.UserAddressIdConverter;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateOrderLoggedInDto {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String sessionId;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @UserAddressIdConverter
    private UserAddress userAddress;
}
