package com.app.controller;

import com.app.api.CarDto;
import com.app.api.CarSeedDto;
import com.app.config.annotations.TrackLatency;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.app.service.CarService;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/cars")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @TrackLatency(value = "JPA query")
    @GetMapping("/by-pictures")
    public ResponseEntity<List<CarDto>> exportCarsByPictures() {

        return ResponseEntity.ok(this.carService.getCarsOrderByPicturesCountThenByMake());
    }

    @TrackLatency(value = "Criteria API query")
    @GetMapping("/by-pictures2")
    public ResponseEntity<List<CarDto>> exportCarsByPicturesUsingCriteriaApi() {

        return ResponseEntity.ok(this.carService.getCarsByPicturesCountThenByMake());
    }


    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCarById(@PathVariable Long id) {

        return ResponseEntity.ok(this.carService.getCarById(id));
    }

    @GetMapping()
    public ResponseEntity<List<CarDto>> getCars() {

        return ResponseEntity.ok(this.carService.getAllCars());
    }

    @PostMapping()
    public ResponseEntity<CarSeedDto> createEmployee(@Valid @RequestBody CarSeedDto car) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(carService.createCar(car));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarDto> updateCar(@PathVariable Long id, @Valid @RequestBody CarSeedDto car) {

        return ResponseEntity.ok(carService.updateCar(id, car));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {

        carService.deleteCar(id);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(String.format("Car with id: %d has been deleted successfully", id));
    }

}
