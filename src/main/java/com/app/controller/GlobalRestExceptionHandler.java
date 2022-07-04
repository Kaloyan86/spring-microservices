package com.app.controller;


import com.app.error.CarNotFoundException;
import com.app.error.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public final ResponseEntity<ErrorStatus> handleCarNotFoundException(CarNotFoundException ex) {

        log.error("Exception occurred: " + ex.getMessage());

        ex.printStackTrace();

        return new ResponseEntity<>(new ErrorStatus(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorStatus> handleExceptions(Exception ex) {

        log.error("Exception occurred: " + ex.getMessage());

        ex.printStackTrace();

        return new ResponseEntity<>(new ErrorStatus(ex.getMessage()),  HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
