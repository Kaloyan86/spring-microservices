package com.app.web.controllers;

import com.app.web.annotations.TrackLatency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.app.service.CarService;

@Controller
@RequestMapping("/export")
public class ExportController extends BaseController {


    private final CarService carService;


    @Autowired
    public ExportController(CarService carService) {
        this.carService = carService;

    }


    @TrackLatency(value = "JPA query")
    @GetMapping("/cars-by-pictures")
    public ModelAndView exportCarsByPictures() {
        String carsByPictures = this.carService
                .getCarsOrderByPicturesCountThenByMake();

        return super.view("export/export-cars-by-pictures.html", "carsByPictures", carsByPictures);
    }

    @TrackLatency(value = "Criteria API query")
    @GetMapping("/cars-by-pictures2")
    public ModelAndView exportCarsByPicturesUsingCriteriaApi() {
        String carsByPictures = this.carService
                .getCarsByPicturesCountThenByMake();

        return super.view("export/export-cars-by-pictures.html", "carsByPictures", carsByPictures);
    }
}
