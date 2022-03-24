package com.cyecize.app.config;

import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.common.annotations.Component;
import com.cyecize.summer.common.extensions.InterceptorAdapter;

@Component
public class CorsInterceptor implements InterceptorAdapter {
    @Override
    public boolean preHandle(HttpSoletRequest request, HttpSoletResponse response, Object handler) throws Exception {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, PATCH, DELETE, OPTIONS");
        response.addHeader("Access-Control-Allow-Headers", "*");
        response.addHeader("Access-Control-Allow-Credentials", Boolean.TRUE.toString());
        response.addHeader("Access-Control-Max-Age", Integer.valueOf(180).toString());

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatusCode(HttpStatus.OK);
            return false;
        }

        return true;
    }
}
