package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;

public interface AuthTokenService {

    void refresh(AuthToken token);

    void remove(AuthToken authToken);

    void removeAllUserTokens(Long userId);

    boolean isAuthTokenExpired(AuthToken token);

    AuthToken createToken(User loggedInUser);

    AuthToken findById(String id);
}