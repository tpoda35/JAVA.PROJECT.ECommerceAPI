package com.eCommerce.product.controller;

import com.eCommerce.product.model.CustomErrorResponse;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<CustomErrorResponse> entityNotFoundHandler(EntityNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new CustomErrorResponse(
                new Date(), HttpStatus.NOT_FOUND.value(), e.getMessage())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> globalExceptionHandler(Exception e){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new CustomErrorResponse(
                new Date(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage())
        );
    }

}
