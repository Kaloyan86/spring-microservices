package com.app.controller;


import com.app.error.CarNotFoundException;
import com.app.error.ErrorStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import static com.app.error.Constants.UNEXPECTED_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalRestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CarNotFoundException.class)
    public final ResponseEntity<ErrorStatus> handleCarNotFoundException(CarNotFoundException ex) {

        log.error("Exception occurred: " + ex.getMessage());

        ex.printStackTrace();

        return new ResponseEntity<>(ErrorStatus.builder().withMessage(ex.getMessage()).build(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ErrorStatus> handleExceptions(Exception ex) {

        log.error(UNEXPECTED_ERROR + ex.getMessage());

        ex.printStackTrace();

        return new ResponseEntity<>(ErrorStatus.builder().withMessage(ex.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> errorMessages = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        log.error(errorMessages.toString());

        return new ResponseEntity<>(ErrorStatus.builder().withMessages(errorMessages).build(), HttpStatus.BAD_REQUEST);
    }
}
