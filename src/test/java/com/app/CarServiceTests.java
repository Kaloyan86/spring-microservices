package com.app;


import java.util.List;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;
import com.app.error.CarNotFoundException;
import com.app.service.CarService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class CarServiceTests extends AbstractIntegrationTest {

    @Autowired
    private CarService carService;
    @Autowired
    MockMvc mockMvc;

    @Test
    public void shouldGetCarById() {

        CarDto car = carService.getCarById(1L);

        assertThat(car).isNotNull();
    }

    @Test
    public void shouldGetAllCars() {
        List<CarDto> cars = carService.getAllCars();

        Assertions.assertEquals(cars.size(), 189);
    }


    @Test
    public void whenCreateCar_shouldReturnCar() {

        CarSeedDto carSeedDto = CarSeedDto.builder()
                                          .make("BMW")
                                          .model("328i")
                                          .kilometers(125000)
                                          .registeredOn("31/01/2015")
                                          .build();

        CarSeedDto car = carService.createCar(carSeedDto);

        assertThat(car.getMake()).isSameAs(carSeedDto.getMake());
        assertThat(car.getModel()).isSameAs(carSeedDto.getModel());
        assertThat(car.getKilometers()).isSameAs(carSeedDto.getKilometers());
        assertThat(car.getRegisteredOn()).isSameAs(carSeedDto.getRegisteredOn());
    }

    @Test
    public void whenGivenId_shouldUpdateCar_ifFound() {

        CarSeedDto updatedCar = CarSeedDto.builder()
                                          .make("BMW")
                                          .model("530xd")
                                          .kilometers(5000)
                                          .registeredOn("31/01/2009")
                                          .build();

        CarDto car = carService.updateCar(189L, updatedCar);

        assertThat(car.getMake()).isSameAs(updatedCar.getMake());
        assertThat(car.getModel()).isSameAs(updatedCar.getModel());
        assertThat(car.getKilometers()).isSameAs(updatedCar.getKilometers());
    }

    @Test
    public void should_throw_exception_when_car_doesnt_exist() {

        CarNotFoundException carNotFoundException = assertThrows(CarNotFoundException.class, () -> carService.getCarById(200L));

        assertEquals("Not found car with id 200", carNotFoundException.getMessage());

    }

    @Test
    public void whenGivenId_shouldDeleteUser_ifFound(){
        // TODO
    }
}
