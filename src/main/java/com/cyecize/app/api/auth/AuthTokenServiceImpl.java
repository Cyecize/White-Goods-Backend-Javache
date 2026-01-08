package com.cyecize.app.api.auth;

import com.cyecize.app.api.user.User;
import com.cyecize.app.integration.transaction.Transactional;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthTokenServiceImpl implements AuthTokenService {

    private final AuthTokenRepository authTokenRepository;

    @Configuration("user.token.max.inactivity.minutes")
    private final int maxUserTokenInactivityMin;

    @Override
    public void refresh(AuthToken token) {
        this.authTokenRepository.updateLastAccessTimeById(token.getId(), LocalDateTime.now());
    }

    @Override
    public void remove(AuthToken authToken) {
        this.authTokenRepository.delete(authToken.getId());
    }

    @Override
    @Transactional
    public void removeAllUserTokens(Long userId) {
        this.authTokenRepository.removeAllByUser(userId);
    }

    @Override
    public boolean isAuthTokenExpired(AuthToken token) {
        final LocalDateTime now = LocalDateTime.now();
        final LocalDateTime lastAccessTime = token.getLastAccessDate();

        return Math.abs(ChronoUnit.MINUTES.between(now, lastAccessTime))
                > maxUserTokenInactivityMin;
    }

    @Override
    public AuthToken createToken(User loggedInUser) {
        final AuthToken token = new AuthToken();

        token.setUserId(loggedInUser.getId());
        token.setLastAccessDate(LocalDateTime.now());

        this.authTokenRepository.persist(token);

        return token;
    }

    @Override
    public AuthToken findById(String id) {
        return this.authTokenRepository.findAuthTokenById(id).orElse(null);
    }
}
