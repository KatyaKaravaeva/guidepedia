package com.guidepedia.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.SignatureException;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ExceptionMapper extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = TokenRefreshException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorMessage> handleTokenRefreshException(TokenRefreshException ex, WebRequest request) {
        ErrorMessage response = new ErrorMessage(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler({MyEntityNotFoundException.class, EntityNotFoundException.class})
    protected ResponseEntity<ErrorMessage> handleEntityNotFoundEx(MyEntityNotFoundException ex, WebRequest request) {
        ErrorMessage response = new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                "Entity Not Found Exception",
                ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Malformed JSON Request", ex.getMessage());
        System.out.println(HttpStatus.BAD_REQUEST.value());
        System.out.println(ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<Object> handleAuthorizationException(AuthorizationException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Authentification exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(com.guidepedia.exception.ExpiredJwtException.class)
    protected ResponseEntity<Object> handleExpiredJwtException(com.guidepedia.exception.ExpiredJwtException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Authentification exception", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorMessage> handleBusinessException(BusinessException ex) {
        ErrorMessage response = new ErrorMessage(HttpStatus.BAD_REQUEST.value(), "Bad request", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorMessage> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                      HttpStatus status,
                                                                      WebRequest request) {
        ErrorMessage errorMessage = new ErrorMessage();
        errorMessage.setDescription(String.format("The parameter '%s' of value '%s' could not be converted to type '%s'",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName()));
        errorMessage.setMessage(ex.getMessage());
        errorMessage.setStatusCode(status.value());
        return new ResponseEntity<>(errorMessage, status);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorMessage> handleAllExceptions(Exception ex, WebRequest request) {
        ErrorMessage response = new ErrorMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> handleSignatureException(SignatureException ex){
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT signature", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = MalformedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> handleMalformedException(MalformedJwtException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "Invalid JWT token", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> handleExpiredJwtException(ExpiredJwtException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "JWT token is expired", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = UnsupportedJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    protected ResponseEntity<ErrorMessage> handleUnsupportedJwtException(UnsupportedJwtException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request){
        ErrorMessage response = new ErrorMessage(HttpStatus.UNAUTHORIZED.value(), "JWT token is unsupported", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

}