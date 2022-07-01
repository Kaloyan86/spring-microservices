package com.app.api;

import com.google.gson.annotations.Expose;
import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

@Data
public class CarSeedDto {

    @Expose
    @Size(min = 2, max = 20,message = "Size must be")
    private String make;
    @Expose
    @Size(min = 2, max = 20)
    private String model;
    @Expose
    @Positive
    private Integer kilometers;
    @Expose
    private String registeredOn;

}
