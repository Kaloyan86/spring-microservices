package com.app;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;

import com.app.error.CarNotFoundException;
import com.app.model.Car;
import com.app.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static com.app.error.Constants.carNotFound;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTests {

    private static final String BASE_URL = "/api/cars/";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;

    private final List<CarDto> cars = new ArrayList<>();

    @BeforeEach
    void setup() {

        CarDto car1 = CarDto.builder()
                            .make("BMW")
                            .model("328i")
                            .kilometers(125000)
                            .registeredOn("31/01/2015")
                            .pictures(2)
                            .build();

        CarDto car2 = CarDto.builder()
                            .make("SKODA")
                            .model("OCTAVIA")
                            .kilometers(25123)
                            .registeredOn("31/01/2019")
                            .pictures(1)
                            .build();

        cars.addAll(List.of(car1, car2));

    }

    @Test
    public void givenCarObject_whenCreateCar_thenReturnSavedCar() throws Exception {

        CarSeedDto carSeedDto = CarSeedDto.builder()
                                          .make("BMW")
                                          .model("328i")
                                          .kilometers(125000)
                                          .registeredOn("31/01/2015")
                                          .build();

        given(carService.createCar(carSeedDto)).willReturn(carSeedDto);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.post(BASE_URL)
                                                                       .contentType(MediaType.APPLICATION_JSON)
                                                                       .content(objectMapper.writeValueAsString(carSeedDto)));

        response.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.make", is(carSeedDto.getMake())))
                .andExpect(jsonPath("$.model", is(carSeedDto.getModel())))
                .andExpect(jsonPath("$.kilometers", is(carSeedDto.getKilometers())))
                .andExpect(jsonPath("$.registeredOn", is(carSeedDto.getRegisteredOn())));

    }

    @Test
    public void getCarById_whenGetMethod() throws Exception {

        Car car = modelMapper.map(cars.get(0), Car.class);
        car.setId(1L);

        given(carService.getCarById(car.getId())).willReturn(modelMapper.map(car, CarDto.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL + car.getId())
                                                                       .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.make", is(car.getMake())))
                .andExpect(jsonPath("$.model", is(car.getModel())))
                .andExpect(jsonPath("$.kilometers", is(car.getKilometers())))
                .andExpect(jsonPath("$.registeredOn", is(car.getRegisteredOn().toString())));
    }

    @Test
    public void givenListOfCars_whenGetAllCars_thenReturnCarsList() throws Exception {

        given(carService.getAllCars()).willReturn(cars);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                                                                       .contentType(MediaType.APPLICATION_JSON));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()", is(cars.size())));

    }

    @Test
    public void givenUpdatedCar_whenUpdateCar_thenReturnUpdatedCarObject() throws Exception {

        CarSeedDto updatedCar = CarSeedDto.builder()
                                          .make("BMW")
                                          .model("530xd")
                                          .kilometers(5000)
                                          .registeredOn("31/01/2009")
                                          .build();

        given(carService.updateCar(1L, updatedCar)).willReturn(modelMapper.map(updatedCar, CarDto.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + 1)
                                                                       .contentType(MediaType.APPLICATION_JSON)
                                                                       .content(objectMapper.writeValueAsString(updatedCar)));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.model", is(updatedCar.getModel())))
                .andExpect(jsonPath("$.kilometers", is(updatedCar.getKilometers())))
                .andExpect(jsonPath("$.registeredOn", is(updatedCar.getRegisteredOn())));
    }

    @Test
    public void should_throw_exception_when_car_doesnt_exist() throws Exception {

        Car car = modelMapper.map(cars.get(0), Car.class);
        car.setId(1L);

        Mockito.doThrow(new CarNotFoundException(carNotFound(car.getId()))).when(carService).updateCar(car.getId(), modelMapper.map(car, CarSeedDto.class));

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.put(BASE_URL + car.getId())
                                                                       .content(objectMapper.writeValueAsString(car))
                                                                       .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNotFound());
    }

    @Test
    public void removeCarById_whenDeleteMethod() throws Exception {
        Car car = modelMapper.map(cars.get(0), Car.class);
        car.setId(1L);

        doNothing().when(carService).deleteCar(car.getId());

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.delete(BASE_URL + car.getId())
                                                                       .contentType(MediaType.APPLICATION_JSON));

        response.andExpect(status().isNoContent())
                .andDo(print())
                .andExpect(content().string("Car with id: 1 has been deleted successfully"));
    }

}
