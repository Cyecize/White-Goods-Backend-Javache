package com.cyecize.app.api.auth;

import com.cyecize.app.api.auth.recoverykey.RecoveryKey;
import com.cyecize.app.api.auth.recoverykey.converter.RecoveryKeyIdConverter;
import com.cyecize.app.constants.General;
import com.cyecize.app.constants.ValidationMessages;
import com.cyecize.summer.areas.validation.annotations.ConvertedBy;
import com.cyecize.summer.areas.validation.constraints.FieldMatch;
import com.cyecize.summer.areas.validation.constraints.MaxLength;
import com.cyecize.summer.areas.validation.constraints.MinLength;
import com.cyecize.summer.areas.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDto {
    @ConvertedBy(RecoveryKeyIdConverter.class)
    @NotNull(message = ValidationMessages.INVALID_RECOVERY_KEY)
    private RecoveryKey recoveryKey;

    @MinLength(length = General.MIN_PASS_LENGTH, message = ValidationMessages.PASSWORD_TOO_SHORT)
    @MaxLength(length = General.MAX_VARCHAR, message = ValidationMessages.INVALID_VALUE)
    private String newPassword;

    @FieldMatch(fieldToMatch = "newPassword", message = ValidationMessages.PASSWORDS_DO_NOT_MATCH)
    private String newPasswordConfirm;
}
