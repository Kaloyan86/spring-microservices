package com.app.api;

import com.google.gson.annotations.Expose;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
@Builder
public class CarSeedDto {

    @Expose
    @Size(min = 2, max = 20, message = "Size must be between 2 and 20")
    private String make;
    @Expose
    @Size(min = 2, max = 20, message = "Size must be between 2 and 20")
    private String model;
    @Expose
    @Positive
    private Integer kilometers;
    @Expose
    private String registeredOn;

    @Builder
    public CarSeedDto(String make, String model, Integer kilometers, String registeredOn) {
        this.make = make;
        this.model = model;
        this.kilometers = kilometers;
        this.registeredOn = registeredOn;
    }
}
