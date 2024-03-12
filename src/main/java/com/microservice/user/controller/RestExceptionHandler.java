package com.microservice.user.controller;


import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.microservice.user.error.ErrorDetail;
import com.microservice.user.error.ErrorResponse;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(value= {MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> validationError(MethodArgumentNotValidException e) {
        ErrorDetail errorDetail = new ErrorDetail();
        errorDetail.setCode(HttpStatus.BAD_REQUEST.value());
        errorDetail.setMessage("Required data is missing");

        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            errorDetail.getDetails().put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(new ErrorResponse(errorDetail));
    }
}
