package com.cyecize.app.api.auth.recoverykey;

import com.cyecize.app.api.user.User;
import lombok.Data;

@Data
public class PasswordRecoveryEmailDto {

    private final User user;
    private final RecoveryKeyDto recoveryKey;
}
