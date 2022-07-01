package com.app.api;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarDto {

    private String make;
    private String model;
    private Integer kilometers;
    private String registeredOn;
    private Integer pictures;
}
