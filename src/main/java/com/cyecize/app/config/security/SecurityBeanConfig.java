package com.cyecize.app.config.security;

import com.cyecize.app.api.auth.AuthenticationService;
import com.cyecize.app.constants.Endpoints;
import com.cyecize.summer.areas.security.confighandlers.RelaxedSecurityConfigHandler;
import com.cyecize.summer.areas.security.confighandlers.ThrowUnauthorizedExceptionHandler;
import com.cyecize.summer.areas.security.models.SecurityConfig;
import com.cyecize.summer.common.annotations.Bean;
import com.cyecize.summer.common.annotations.BeanConfig;
import com.cyecize.summer.common.annotations.Configuration;
import lombok.RequiredArgsConstructor;

@BeanConfig
@RequiredArgsConstructor
public class SecurityBeanConfig {

    @Configuration("summer.security.interceptor.order")
    private final int securityInterceptorOrder;

    private final AuthenticationService authenticationService;

    @Bean
    public SecurityConfig securityConfig() {
        return new SecurityConfig()
                .setSecurityInterceptorOrder(this.securityInterceptorOrder)
                .setNotLoggedInHandler(new ThrowUnauthorizedExceptionHandler())
                .setBeforeLogoutHandler((req, res, conf) -> this.authenticationService.logout())
                .setLogoutCompletedHandler(new RelaxedSecurityConfigHandler())
                .setLogoutURL(Endpoints.LOGOUT);
    }
}
