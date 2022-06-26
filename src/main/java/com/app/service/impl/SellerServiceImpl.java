package com.app.service.impl;

import com.app.models.dto.SellerSeedDto;
import com.app.models.dto.SellerSeedRootDto;
import com.app.models.entities.Picture;
import com.app.models.entities.Seller;
import com.app.repository.SellerRepository;
import com.app.service.CarService;
import com.app.util.ValidationUtil;
import com.app.util.XmlParser;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.service.SellerService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String SELLER_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final Gson gson;
    private final CarService carService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final SellerRepository sellerRepository;

    public SellerServiceImpl(Gson gson, CarService carService, ModelMapper modelMapper, ValidationUtil validationUtil, SellerRepository sellerRepository) {
        this.gson = gson;
        this.carService = carService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return !sellerRepository.findAll().isEmpty();
    }

    @Override
    public String readSellersFromFile() throws IOException {

        return Files.readString(Path.of(SELLER_FILE_PATH));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        XmlParser.fromFile(SELLER_FILE_PATH, SellerSeedRootDto.class)
                .getSellers()
                .stream()
                .peek(s -> sb.append(validationUtil.isValid(s) ? String.format("Successfully imported seller %s - %s%n", s.getFirstName(), s.getEmail()) : String.format("Invalid seller%n")))
                .filter(validationUtil::isValid)
                .map(sellerSeedDto -> modelMapper.map(sellerSeedDto, Seller.class))
                .forEach(sellerRepository::save);

        return sb.toString();
    }

    @Override
    public Seller findById(Long id) {
        return sellerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }
}
