package com.app.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CarNotFoundException extends RuntimeException {

    private String message;
    private Object[] args;

    public CarNotFoundException(String message) {
        super(message);
        this.message = message;
    }
}
