package com.cyecize.app.api.user;

import com.cyecize.app.api.auth.constraints.ValidPassword;
import com.cyecize.app.api.auth.constraints.ValidPasswordDto;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.FieldMatch;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChangePasswordDto implements ValidPasswordDto {

    @NotNull
    @ConvertedBy(CurrentUserAdapter.class)
    private User user;

    @ValidPassword
    private String password;

    @MinLength(length = General.MIN_PASS_LENGTH, message = ValidationMessages.PASSWORD_TOO_SHORT)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String newPassword;

    @FieldMatch(fieldToMatch = "newPassword", message = ValidationMessages.PASSWORDS_DO_NOT_MATCH)
    private String newPasswordConfirm;
}
