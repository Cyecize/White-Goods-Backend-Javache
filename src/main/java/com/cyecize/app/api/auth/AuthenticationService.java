package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;

public interface AuthenticationService {

    AuthToken login(LoginBindingModel loginBindingModel);

    void logout();

    void sendRecoveryEmail(User user);
}
