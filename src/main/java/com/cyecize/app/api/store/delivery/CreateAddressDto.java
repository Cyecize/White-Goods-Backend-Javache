package com.cyecize.app.api.store.delivery;

import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import com.cyecize.summer.areas.validation.constraints.RegEx;
import lombok.Data;

@Data
public class CreateAddressDto {

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String fullName;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @RegEx(value = General.VALID_EMAIL_PATTERN, message = ValidationMessages.INVALID_VALUE)
    private String email;

    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @RegEx(value = General.VALID_PHONE_PATTERN, message = ValidationMessages.INVALID_VALUE)
    private String phoneNumber;

    @MaxLength(length = General.MAX_NAME, message = ValidationMessages.INVALID_VALUE)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String country;

    @MaxLength(length = General.MAX_NAME, message = ValidationMessages.INVALID_VALUE)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String city;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    @NotNull(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    private String addressLine;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String notes;
}
