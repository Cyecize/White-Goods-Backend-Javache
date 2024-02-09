package com.cyecize.app.api.store.order;

import com.cyecize.app.api.store.order.validator.ValidConfirmedPrice;
import com.cyecize.app.api.user.address.UserAddress;
import com.cyecize.app.api.user.address.converter.UserAddressIdConverter;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderLoggedInDto {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String sessionId;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @UserAddressIdConverter
    private UserAddress userAddress;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @ValidConfirmedPrice
    private Double userAgreedPrice;
}
