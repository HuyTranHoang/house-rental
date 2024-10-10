package com.project.house.rental.exception;

import com.project.house.rental.common.HttpResponse;
import jakarta.persistence.NoResultException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionHandling {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<HttpResponse> NoHandlerFoundException(NoHandlerFoundException e) {
        return createResponseEntity(HttpStatus.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity<HttpResponse> noResultException(NoResultException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException(AccessDeniedException e) {
        return createResponseEntity(HttpStatus.FORBIDDEN, "You do not have permission to access this page");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<HttpResponse> noSuchElementException(NoSuchElementException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<HttpResponse> illegalArgumentException(IllegalArgumentException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<HttpResponse> customRuntimeException(CustomRuntimeException e) {
        return createResponseEntity(HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<HttpResponse> handleConflictException(ConflictException e) {
        return createResponseEntity(HttpStatus.CONFLICT, e.getMessage());
    }

    private ResponseEntity<HttpResponse> createResponseEntity(HttpStatus status, String message) {
        HttpResponse httpResponse = new HttpResponse(status.value(), status, status.getReasonPhrase(), message);
        return new ResponseEntity<>(httpResponse, status);
    }
}