package com.cyecize.app.api.auth;

import com.cyecize.app.api.auth.recoverykey.PasswordRecoveryEmailDto;
import com.cyecize.app.api.auth.recoverykey.RecoveryKey;
import com.cyecize.app.api.auth.recoverykey.RecoveryKeyDto;
import com.cyecize.app.api.auth.recoverykey.RecoveryKeyService;
import com.cyecize.app.api.mail.EmailTemplate;
import com.cyecize.app.api.mail.MailService;
import com.cyecize.app.api.user.User;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthTokenService authTokenService;
    private final Principal principal;
    private final RecoveryKeyService recoveryKeyService;
    private final MailService mailService;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public AuthToken login(LoginBindingModel loginBindingModel) {
        this.authTokenService.removeAllUserTokens(loginBindingModel.getUser().getId());
        this.principal.setUser(loginBindingModel.getUser());
        return this.authTokenService.createToken(loginBindingModel.getUser());
    }

    @Override
    public void logout() {
        if (this.principal.getUser() == null) {
            throw new RuntimeException("Cannot log out because session is annonymous!");
        }

        final User user = (User) this.principal.getUser();
        this.principal.setUser(null);
        this.authTokenService.removeAllUserTokens(user.getId());
    }

    @Override
    public void sendRecoveryEmail(User user) {
        final RecoveryKey recoveryKey = this.recoveryKeyService.createRecoveryKey(user.getId());
        final RecoveryKeyDto dto = this.modelMapper.map(recoveryKey, RecoveryKeyDto.class);
        this.mailService.sendEmail(
                EmailTemplate.PASSWORD_RECOVERY_KEY,
                new PasswordRecoveryEmailDto(user, dto),
                List.of(user.getEmail())
        );
    }
}
