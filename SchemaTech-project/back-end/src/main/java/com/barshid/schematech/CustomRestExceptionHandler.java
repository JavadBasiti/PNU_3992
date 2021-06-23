package com.barshid.schematech;

import com.barshid.schematech.controller.dto.ApiError;
//import com.amazonaws.services.kms.model.ExpiredImportTokenException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedUserException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {
//    @Override
//    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
//            HttpRequestMethodNotSupportedException ex,
//            HttpHeaders headers,
//            HttpStatus status,
//            WebRequest request) {
//        StringBuilder builder = new StringBuilder();
//        builder.append(ex.getMethod());
//        builder.append(
//                " method is not supported for this request. Supported methods are ");
//        ex.getSupportedHttpMethods().forEach(t -> builder.append(t + " "));
//
//        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
//                ex.getLocalizedMessage(), builder.toString());
//        return new ResponseEntity<Object>(
//                apiError, new HttpHeaders(), apiError.getStatus());
//    }

    @ExceptionHandler({ ResponseStatusException.class })
    public ResponseEntity<Object> handleResponseStatus(ResponseStatusException ex, WebRequest request) {
        ApiError apiError = new ApiError( ex.getStatus(), ex.getReason(),request.getContextPath());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getError());
    }

//    @ExceptionHandler({ExpiredImportTokenException.class })
//    public ResponseEntity<Object> handleTokenExpire(ExpiredImportTokenException ex, WebRequest request) {
//        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getErrorMessage(),request.getContextPath());
//        return new ResponseEntity<Object>(
//                apiError, new HttpHeaders(), apiError.getError());
//    }

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class })
    public ResponseEntity<Object> handleUnAuthorized(HttpClientErrorException.Unauthorized ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(),request.getContextPath());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getError());
    }

    @ExceptionHandler({UnauthorizedClientException.class })
    public ResponseEntity<Object> handleUnAuthorized(UnauthorizedClientException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(),request.getContextPath());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getError());
    }

    @ExceptionHandler({UnauthorizedUserException.class })
    public ResponseEntity<Object> handleUnAuthorized(UnauthorizedUserException ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage(),request.getContextPath());
        return new ResponseEntity<Object>(
                apiError, new HttpHeaders(), apiError.getError());
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        logger.error(ex.getLocalizedMessage(),ex);
        ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), request.getContextPath());
        return new ResponseEntity<Object>(apiError, new HttpHeaders(), apiError.getError());
    }


}