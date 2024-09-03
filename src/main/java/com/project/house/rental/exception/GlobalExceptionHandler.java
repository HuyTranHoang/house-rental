package com.project.house.rental.exception;

import com.project.house.rental.common.HttpResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAccountLockedException.class)
    public ResponseEntity<Map<String, Object>> handleUserAccountLockedException(UserAccountLockedException ex) {
        Map<String, Object> errorResponse = Map.of(
                "timeStamp", LocalDateTime.now().toString(),
                "statusCode", HttpStatus.FORBIDDEN.value(),
                "httpStatus", HttpStatus.FORBIDDEN,
                "reason", "Forbidden",
                "message", ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> handleAllExceptions(Exception ex) {
        HttpResponse httpResponse = new HttpResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase().toUpperCase(),
                ex.getMessage()
        );
        return new ResponseEntity<>(httpResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}