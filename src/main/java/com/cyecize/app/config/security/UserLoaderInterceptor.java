package com.cyecize.app.config.security;

import com.cyecize.app.api.auth.AuthToken;
import com.cyecize.app.api.auth.AuthTokenService;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.areas.security.UserDetailsContextHolder;
import com.cyecize.summer.areas.startup.services.DependencyContainer;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.extensions.InterceptorAdapter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserLoaderInterceptor implements InterceptorAdapter {

    @Configuration("user.interceptor.order")
    private final int order;

    @Configuration("token.header.name")
    private final String headerName;

    private final AuthTokenService authTokenService;

    @Override
    public boolean preHandle(HttpSoletRequest request, HttpSoletResponse response, Object handler) throws Exception {
        //No need to load user for each handler, just the first time.
        if (!(handler instanceof DependencyContainer)) {
            return true;
        }

        UserDetailsContextHolder.setUserDetails(null);

        if (!request.getHeaders().containsKey(this.headerName)) {
            return true;
        }

        final String tokenId = request.getHeader(this.headerName);
        final AuthToken token = this.authTokenService.findById(tokenId);

        if (token == null) {
            return true;
        }

        if (this.authTokenService.isAuthTokenExpired(token)) {
            this.authTokenService.remove(token);
            return true;
        }

        this.authTokenService.refresh(token);
        UserDetailsContextHolder.setUserDetails(token.getUser());

        return true;
    }

    @Override
    public int getOrder() {
        return this.order;
    }
}
