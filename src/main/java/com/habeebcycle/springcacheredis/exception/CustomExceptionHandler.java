package com.habeebcycle.springcacheredis.exception;

import com.habeebcycle.springcacheredis.error.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundException(ProductNotFoundException ex, ServerHttpRequest request) {
        String path = request.getPath().pathWithinApplication().value();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> customExceptionHandler(Exception ex, ServerHttpRequest request) {
        String path = request.getPath().pathWithinApplication().value();
        ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(), path);
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
