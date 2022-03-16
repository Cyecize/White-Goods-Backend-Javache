package com.cyecize.app.web;

import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.ErrorResponse;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.ExceptionListener;

@Controller
public class GlobalErrorController {

    @ExceptionListener(value = ApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleApiException(ApiException exception,
                                            HttpSoletRequest request,
                                            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionListener(value = NotFoundApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleNotFoundApiException(NotFoundApiException exception,
                                                    HttpSoletRequest request,
                                                    HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.NOT_FOUND);
        return this.createErrorResponse(request, HttpStatus.NOT_FOUND, exception.getMessage());
    }

    private ErrorResponse createErrorResponse(HttpSoletRequest request, HttpStatus status, String message) {
        return new ErrorResponse(request.getRequestURI(), status, message);
    }
}
