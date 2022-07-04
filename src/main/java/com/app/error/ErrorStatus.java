package com.app.error;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ErrorStatus {

    private String message;

    public ErrorStatus(String message) {
        this.message = message;
    }

}
