package com.app.service.impl;

import com.app.model.dto.PictureSeedDto;
import com.app.model.entities.Picture;
import com.app.repository.PictureRepository;
import com.app.service.CarService;
import com.app.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.service.PictureService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class PictureServiceImpl implements PictureService {

    private static final String PICTURE_FILE_PATH = "src/main/resources/files/json/pictures.json";

    private final Gson gson;
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final PictureRepository pictureRepository;

    public PictureServiceImpl(Gson gson, CarService carService, ModelMapper modelMapper, ValidationUtil validationUtil, PictureRepository pictureRepository) {
        this.gson = gson;
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.pictureRepository = pictureRepository;
    }

    @Override
    public boolean areImported() {
        return !pictureRepository.findAll().isEmpty();
    }

    @Override
    public String readPicturesFromFile() throws IOException {
        return Files.readString(Path.of(PICTURE_FILE_PATH));
    }

    @Override
    public String importPictures() throws IOException {
        StringBuilder sb = new StringBuilder();

        Arrays.stream(gson.fromJson(readPicturesFromFile(), PictureSeedDto[].class))
                .peek(p -> sb.append(validationUtil.isValid(p) ? String.format("Successfully imported picture - %s%n", p.getName()) : String.format("Invalid picture%n")))
                .filter(validationUtil::isValid)
                .map(pictureSeedDto -> {
                    Picture picture = modelMapper.map(pictureSeedDto, Picture.class);
                    picture.setCar(carService.findById(pictureSeedDto.getCar()));
                    return picture;
                })
                .forEach(pictureRepository::save);

        return sb.toString();
    }
}
