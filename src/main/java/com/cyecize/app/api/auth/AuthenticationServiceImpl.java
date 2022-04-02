package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.areas.security.models.Principal;
import com.cyecize.summer.common.annotations.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final AuthTokenService authTokenService;
    private final Principal principal;

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
}
