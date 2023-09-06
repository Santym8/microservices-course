package com.santym.orderservice.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreateOrderException.class)
    public ResponseEntity<ErrorDetails> handleCreateOrderException(CreateOrderException createOrderException) {
        return ResponseEntity.badRequest().body(
                ErrorDetails.builder()
                        .message(createOrderException.getMessage())
                        .details(createOrderException.getBody())
                        .error("CreateOrderException")
                        .path("/api/v1/orders")
                        .timestamp(new Date())
                        .build()
        );
    }
}
