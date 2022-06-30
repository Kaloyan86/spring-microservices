package com.app.service;

import com.app.model.dto.CarDto;
import com.app.model.entities.Car;

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
