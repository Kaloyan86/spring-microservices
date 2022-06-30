package com.app.service.impl;

import com.app.model.dto.CarDto;
import com.app.model.dto.CarSeedDto;
import com.app.model.entities.Car;
import com.app.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.repository.CarRepository;
import com.app.service.CarService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final Gson gson;
    @PersistenceContext
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;

    public CarServiceImpl(Gson gson, EntityManager entityManager, ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository) {
        this.gson = gson;
        this.entityManager = entityManager;
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
    public List<CarDto> getCarsOrderByPicturesCountThenByMake() {

        return carRepository
                .findAllCarsOrderByPicturesThenByMake()
                .stream()
                .map(car -> new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size()))
                .collect(Collectors.toList());
    }

    @Override
    public List<CarDto> getCarsByPicturesCountThenByMake() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Car> criteriaQuery = criteriaBuilder.createQuery(Car.class);
        Root<Car> carRoot = criteriaQuery.from(Car.class);
        criteriaQuery.select(carRoot)
                .orderBy(criteriaBuilder.desc(criteriaBuilder.size(carRoot.get("pictures"))), criteriaBuilder.asc(carRoot.get("make")));

        return entityManager
                .createQuery(criteriaQuery)
                .getResultList()
                .stream()
                .map(car -> new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size()))
                .collect(Collectors.toList());
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
