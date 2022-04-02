package com.cyecize.app.api.auth;

public interface AuthenticationService {

    AuthToken login(LoginBindingModel loginBindingModel);

    void logout();
}
