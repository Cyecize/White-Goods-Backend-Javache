package com.cyecize.app.api.user;

import com.cyecize.app.api.user.constraints.UsernameTaken;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.constraints.FieldMatch;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotEmpty;
import com.cyecize.summer.areas.validation.constraints.RegEx;
import lombok.Data;

@Data
public class UserRegisterDto {

    @UsernameTaken
    @NotEmpty(message = ValidationMessages.FIELD_CANNOT_BE_NULL)
    @RegEx(value = General.VALID_EMAIL_PATTERN, message = ValidationMessages.INVALID_VALUE)
    private String email;

    @UsernameTaken
    private String username;

    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    @MinLength(length = General.MIN_PASS_LENGTH, message = ValidationMessages.PASSWORD_TOO_SHORT)
    private String password;

    @FieldMatch(fieldToMatch = "password", message = ValidationMessages.PASSWORDS_DO_NOT_MATCH)
    private String passwordConfirm;
}
