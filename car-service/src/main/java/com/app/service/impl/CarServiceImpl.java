package com.app.service.impl;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;
import com.app.model.Car;
import com.app.error.CarNotFoundException;
import com.app.service.util.ValidationUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import com.app.repository.CarRepository;
import com.app.service.CarService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.app.error.Constants.carNotFound;

@Slf4j
@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final Gson gson;
    @PersistenceContext
    private final EntityManager entityManager;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final CarRepository carRepository;
    private final NewTopic topic;
    private final KafkaTemplate<String, CarSeedDto> kafkaTemplate;

    public CarServiceImpl(Gson gson, EntityManager entityManager, ModelMapper modelMapper, ValidationUtil validationUtil, CarRepository carRepository, NewTopic topic, KafkaTemplate<String, CarSeedDto> kafkaTemplate) {
        this.gson = gson;
        this.entityManager = entityManager;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.carRepository = carRepository;
        this.topic = topic;
        this.kafkaTemplate = kafkaTemplate;
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

        return carRepository.findAllCarsOrderByPicturesThenByMake()
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

        return entityManager.createQuery(criteriaQuery)
                            .getResultList()
                            .stream()
                            .map(car -> new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size()))
                            .collect(Collectors.toList());
    }

    @Override
    public Car findById(Long id) {
        return carRepository.findById(id).orElseThrow(() -> new CarNotFoundException(carNotFound(id)));
    }

    public CarDto getCarById(Long id) {
        final Car car = findById(id);
        return new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size());
    }

    @Override
    public List<CarDto> getAllCars() {

        return carRepository.findAll()
                            .stream()
                            .map(car -> new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size()))
                            .collect(Collectors.toList());
    }

    @Override
    public CarSeedDto createCar(CarSeedDto carSeedDto) {
        final Car car = modelMapper.map(carSeedDto, Car.class);
        carRepository.save(car);
        Message<CarSeedDto> message = MessageBuilder.withPayload(carSeedDto)
                                                    .setHeader(KafkaHeaders.TOPIC, topic.name())
                                                    .build();
        log.info(String.format("Create car event => %s", carSeedDto));

        kafkaTemplate.send(message);
        return carSeedDto;
    }

    @Override
    public void deleteCar(Long id) {
        carRepository.delete(findById(id));
    }

    @Override
    public CarDto updateCar(Long id, CarSeedDto carSeedDto) {
        Car car = findById(id);
        car.setModel(carSeedDto.getModel());
        car.setMake(carSeedDto.getMake());
        car.setKilometers(carSeedDto.getKilometers());
        car.setRegisteredOn(modelMapper.map(carSeedDto.getRegisteredOn(), LocalDate.class));

        carRepository.save(car);

        return new CarDto(car.getMake(), car.getModel(), car.getKilometers(), car.getRegisteredOn().toString(), car.getPictures().size());
    }
}
