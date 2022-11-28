package com.cyecize.app.web;

import com.cyecize.app.api.store.cart.ShoppingCartSessionException;
import com.cyecize.app.constants.General;
import com.cyecize.app.error.ApiException;
import com.cyecize.app.error.ErrorResponse;
import com.cyecize.app.error.NotFoundApiException;
import com.cyecize.http.HttpStatus;
import com.cyecize.solet.HttpSoletRequest;
import com.cyecize.solet.HttpSoletResponse;
import com.cyecize.solet.SoletConstants;
import com.cyecize.solet.SoletOutputStream;
import com.cyecize.summer.areas.routing.exceptions.HttpNotFoundException;
import com.cyecize.summer.areas.security.exceptions.UnauthorizedException;
import com.cyecize.summer.areas.validation.exceptions.ConstraintValidationException;
import com.cyecize.summer.areas.validation.exceptions.ObjectBindingException;
import com.cyecize.summer.areas.validation.interfaces.BindingResult;
import com.cyecize.summer.areas.validation.models.FieldError;
import com.cyecize.summer.common.annotations.Configuration;
import com.cyecize.summer.common.annotations.Controller;
import com.cyecize.summer.common.annotations.routing.ExceptionListener;
import com.cyecize.summer.utils.PathUtils;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class GlobalErrorController {

    @Configuration(SoletConstants.SOLET_CONFIG_ASSETS_DIR)
    private final String assetsDir;

    /**
     * Logic for forwarding requests to the frontend. If index.html exists in the assets' dir, then
     * the FE is deployed. In that case, then render the HTML content of that file regardless of the
     * URL, FE will handle it. Otherwise, print an error.
     */
    @ExceptionListener(HttpNotFoundException.class)
    public Object handleNotFoundException(HttpSoletResponse httpSoletResponse,
                                          HttpNotFoundException ex) throws IOException {
        final Path indexPath = Path.of(PathUtils.appendPath(this.assetsDir, "/index.html"));
        if (Files.exists(indexPath)) {
            httpSoletResponse.setStatusCode(HttpStatus.OK);
            // Old way of serving files. Not recommended since it loads the whole file in memory
            // final String fileContents = new String(Files.readAllBytes(indexPath));
            //  return new ModelAndView("index.html.twig", Map.of("data", fileContents));
            this.downloadHtmlFile(indexPath, httpSoletResponse);
            return null;
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

    private ErrorResponse createErrorResponse(HttpSoletRequest request, HttpStatus status, String message) {
        return new ErrorResponse(request.getRequestURI(), status, message);
    }

    private void downloadHtmlFile(Path filePath, HttpSoletResponse response) {
        final File file = filePath.toFile();

        try {
            response.addHeader("Content-Type", "text/html");
            response.addHeader("Content-Disposition", "inline;");
            response.addHeader("Content-Length", file.length() + "");

            final SoletOutputStream outputStream = response.getOutputStream();
            final byte[] buff = new byte[2048];
            try (final FileInputStream fileInputStream = new FileInputStream(file)) {
                while (fileInputStream.available() > 0) {
                    final int read = fileInputStream.read(buff);
                    outputStream.write(buff, 0, read);
                }
            }
        } catch (IOException ex) {
            throw new ApiException("Could not download file!");
        }
    }
}
