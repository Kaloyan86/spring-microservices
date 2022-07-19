package com.app;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;

import com.app.service.CarService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTests {

    private static final String BASE_URL = "/api/cars";

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CarService carService;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    ModelMapper modelMapper;

    private List<CarDto> cars = new ArrayList<>();

    @BeforeEach
    void setup() {

        CarDto car1 = CarDto.builder().make("BMW")
                .model("328i")
                .kilometers(125000)
                .registeredOn("31/01/2015")
                .pictures(2)
                .build();

        CarDto car2 = CarDto.builder().make("SKODA")
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
    public void givenListOfCars_whenGetAllCars_thenReturnCarsList() throws Exception {

        given(carService.getAllCars()).willReturn(cars);

        ResultActions response = mockMvc.perform(MockMvcRequestBuilders.get(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(cars.size())));

    }
}
