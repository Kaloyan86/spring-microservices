package com.app.api;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CarDto {

    private String make;
    private String model;
    private Integer kilometers;
    private String registeredOn;
    private Integer pictures;
}
