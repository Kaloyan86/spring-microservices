package com.app.service;

import com.app.api.CarDto;
import com.app.data.model.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    boolean areImported();

    String readCarsFileContent() throws IOException;

    String importCars() throws IOException;

    List<CarDto> getCarsOrderByPicturesCountThenByMake();

    List<CarDto> getCarsByPicturesCountThenByMake();

    Car findById(Long car);
}
