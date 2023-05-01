package com.cyecize.app.config;

import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.extensions.InterceptorAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VisitorLoggerInterceptor implements InterceptorAdapter {

    @Override
    public boolean preHandle(HttpSoletRequest request, HttpSoletResponse response, Object handler)
            throws Exception {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        final String addr;
        // Well known header for delivery real ip to server when using proxy servers.
        if (request.getHeaders().containsKey("X-Forwarded-For")) {
            addr = request.getHeader("X-Forwarded-For");
        } else {
            addr = request.getRemoteAddress();
        }

        log.info(addr);
        return true;
    }
}
