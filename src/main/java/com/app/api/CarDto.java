package com.app.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CarDto {

    private String make;
    private String model;
    private Integer kilometers;
    private String registeredOn;
    private Integer pictures;
}
