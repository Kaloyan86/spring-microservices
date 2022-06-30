package com.app.web.controllers;

import com.app.model.dto.CarDto;
import com.app.web.annotations.TrackLatency;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.app.service.CarService;

import java.util.List;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {

    private final CarService carService;

    @Autowired
    public ExportController(CarService carService, Gson gson) {
        this.carService = carService;
    }

    @TrackLatency(value = "JPA query")
    @GetMapping("/cars-by-pictures")
    public ResponseEntity<List<CarDto>> exportCarsByPictures() {

        return ResponseEntity.ok(this.carService.getCarsOrderByPicturesCountThenByMake());
    }

    @TrackLatency(value = "Criteria API query")
    @GetMapping("/cars-by-pictures2")
    public ResponseEntity<List<CarDto>> exportCarsByPicturesUsingCriteriaApi() {

        return ResponseEntity.ok(this.carService.getCarsByPicturesCountThenByMake());
    }
}
