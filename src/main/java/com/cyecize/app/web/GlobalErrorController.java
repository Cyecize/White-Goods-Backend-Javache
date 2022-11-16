package com.cyecize.app.web;

import com.cyecize.app.api.store.ShoppingCartSessionException;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.ErrorResponse;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.solet.SoletConfig;
import com.cyecize.solet.SoletConstants;
import com.cyecize.summer.areas.routing.exceptions.HttpNotFoundException;
import com.cyecize.summer.areas.security.exceptions.UnauthorizedException;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.ExceptionListener;
import com.cyecize.summer.common.models.ModelAndView;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GlobalErrorController {

    private final SoletConfig soletConfig;

    /**
     * Logic for forwarding requests to the frontend. If index.html exists in the assets' dir, then
     * the FE is deployed. In that case, then render the HTML content of that file regardless of the
     * URL, FE will handle it. Otherwise, print an error.
     */
    @ExceptionListener(HttpNotFoundException.class)
    public Object handleNotFoundException(HttpSoletResponse httpSoletResponse,
            HttpNotFoundException ex) throws IOException {
        final String assetsDir = this.soletConfig.getAttribute(SoletConstants.SOLET_CONFIG_ASSETS_DIR) + "";
        if (Files.exists(Path.of(assetsDir + "/index.html"))) {
            httpSoletResponse.setStatusCode(HttpStatus.OK);
            final String fileContents = new String(
                    Files.readAllBytes(Path.of(assetsDir + "/index.html")));

            return new ModelAndView("index.html.twig", Map.of("data", fileContents));
        }

        httpSoletResponse.setStatusCode(HttpStatus.NOT_FOUND);
        ex.printStackTrace();
        return "Page not found!";
    }

    @ExceptionListener(UnauthorizedException.class)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException exception,
                                                     HttpSoletRequest request,
                                                     HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return this.createErrorResponse(request, HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionListener(ConstraintValidationException.class)
    public List<FieldError> constraintErrs(ConstraintValidationException ex,
                                           BindingResult bindingResult,
                                           HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        return bindingResult.getErrors();
    }

    @ExceptionListener(value = ApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleApiException(ApiException exception,
                                            HttpSoletRequest request,
                                            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionListener(value = ShoppingCartSessionException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleShoppingCartSessionException(ShoppingCartSessionException ex,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        return this.createErrorResponse(request, HttpStatus.NOT_ACCEPTABLE, ex.getMessage());
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
