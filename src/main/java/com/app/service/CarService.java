package com.app.service;

import com.app.models.entities.Car;

import java.io.IOException;

public interface CarService {

    boolean areImported();

    String readCarsFileContent() throws IOException;

    String importCars() throws IOException;

    String getCarsOrderByPicturesCountThenByMake();

    Car findById(Long car);
}
