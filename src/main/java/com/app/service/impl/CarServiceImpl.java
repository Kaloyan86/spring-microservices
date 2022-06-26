package com.app.service.impl;

import com.app.models.dto.CarSeedDto;
import com.app.models.entities.Car;
import com.app.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.repository.CarRepository;
import com.app.service.CarService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.NoSuchElementException;


@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;

    public CarServiceImpl(Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository) {
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
    }

    @Override
    public boolean areImported() {
        return !carRepository.findAll().isEmpty();
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readCarsFileContent(), CarSeedDto[].class))
                .peek(c -> sb.append(validationUtil.isValid(c) ? String.format("Successfully imported car - %s - %s%n", c.getMake(), c.getModel()) : String.format("Invalid car%n")))
                .filter(validationUtil::isValid)
                .map(carSeedDto -> modelMapper.map(carSeedDto, Car.class))
                .forEach(carRepository::save);

        return sb.toString();
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {

        StringBuilder sb = new StringBuilder();

        carRepository.findAllCarsOrderByPicturesThenByMake()
                .forEach(car -> sb.append(car.toString()).append(System.lineSeparator()));

        return sb.toString();
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
