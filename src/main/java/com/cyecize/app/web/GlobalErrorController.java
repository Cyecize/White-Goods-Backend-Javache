package com.cyecize.app.web;

import com.cyecize.app.api.frontend.index.IndexServingService;
import com.cyecize.app.api.frontend.opengraph.OpenGraphService;
import com.cyecize.app.api.store.cart.ShoppingCartSessionException;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.ErrorResponse;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.summer.areas.routing.exceptions.HttpNotFoundException;
import com.cyecize.summer.areas.security.exceptions.UnauthorizedException;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.exceptions.ObjectBindingException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.ExceptionListener;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class GlobalErrorController {

    private final IndexServingService indexServingService;

    private final OpenGraphService openGraphService;


    /**
     * Logic for forwarding requests to the frontend. If index.html exists in the assets' dir, then
     * the FE is deployed. In that case, then render the HTML content of that file regardless of the
     * URL, FE will handle it. Otherwise, print an error.
     */
    @ExceptionListener(HttpNotFoundException.class)
    public Object handleNotFoundException(HttpSoletRequest req,
            HttpSoletResponse res,
            HttpNotFoundException ex) throws IOException {
        if (this.indexServingService.serveIndexFile(req, res, this.openGraphService.getTags(req))) {
            return null;
        }

        res.setStatusCode(HttpStatus.NOT_FOUND);
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
        log.trace("Constraint errors: {}", bindingResult.getErrors());
        return bindingResult.getErrors();
    }

    @ExceptionListener(value = ApiException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleApiException(ApiException exception,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionListener(value = ObjectBindingException.class, produces = General.APPLICATION_JSON)
    public ErrorResponse handleObjectBingingException(ObjectBindingException ex,
            HttpSoletRequest request,
            HttpSoletResponse response) {
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return this.createErrorResponse(
                request,
                HttpStatus.BAD_REQUEST,
                ex.getCause().getMessage()
        );
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

    private ErrorResponse createErrorResponse(HttpSoletRequest request, HttpStatus status,
            String message) {
        return new ErrorResponse(request.getRequestURI(), status, message);
    }
}
