package com.app.error;

public enum Constants {
    ;

    public static final String UNEXPECTED_ERROR = "Exception.unexpected: ";

    public static String carNotFound(Long id) {
        return String.format("Not found car with id %d", id);
    }

}
