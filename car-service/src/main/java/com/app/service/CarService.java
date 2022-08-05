package com.app.service;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;
import com.app.model.Car;

import java.io.IOException;
import java.util.List;

public interface CarService {

    boolean areImported();

    String readCarsFileContent() throws IOException;

    String importCars() throws IOException;

    List<CarDto> getCarsOrderByPicturesCountThenByMake();

    List<CarDto> getCarsByPicturesCountThenByMake();

    Car findById(Long car);

    CarDto getCarById(Long id);

    List<CarDto> getAllCars();

    CarSeedDto createCar(CarSeedDto car);

    void deleteCar(Long id);

    CarDto updateCar(Long id, CarSeedDto carSeedDto);
}
