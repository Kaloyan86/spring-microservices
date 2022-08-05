package com.app.error;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorStatus {

    private String message;
    private List<String> messages;

    @Builder(setterPrefix = "with", access = AccessLevel.PUBLIC)
    public ErrorStatus(String message, List<String> messages) {
        this.message = message;
        this.messages = messages;
    }
}
