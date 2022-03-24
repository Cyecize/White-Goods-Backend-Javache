package com.cyecize.app.api.question;

import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.RegEx;
import lombok.Data;

@Data
public class QuestionQuery {

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = 50, message = ValidationMessages.INVALID_VALUE)
    private String fullName;

    @MaxLength(length = 50, message = ValidationMessages.INVALID_VALUE)
    @RegEx(value = General.VALID_EMAIL_PATTERN, message = ValidationMessages.INVALID_VALUE)
    private String email;

    @RegEx(value = General.VALID_PHONE_PATTERN, message = ValidationMessages.INVALID_VALUE)
    private String phoneNumber;

    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @MaxLength(length = 5000, message = ValidationMessages.INVALID_VALUE)
    private String message;
}
