package com.app.service.impl;

import com.app.models.dto.OfferSeedRootDto;
import com.app.models.entities.Offer;
import com.app.repository.OfferRepository;
import com.app.service.CarService;
import com.app.service.SellerService;
import com.app.util.ValidationUtil;
import com.app.util.XmlParser;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.service.OfferService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "src/main/resources/files/xml/offers.xml";

    private final Gson gson;
    private final CarService carService;
    private final SellerService sellerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final OfferRepository offerRepository;

    public OfferServiceImpl(Gson gson, CarService carService, SellerService sellerService, ModelMapper modelMapper, ValidationUtil validationUtil, OfferRepository offerRepository) {
        this.gson = gson;
        this.carService = carService;
        this.sellerService = sellerService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.offerRepository = offerRepository;
    }

    @Override
    public boolean areImported() {
        return !offerRepository.findAll().isEmpty();
    }

    @Override
    public String readOffersFileContent() throws IOException {
        return Files.readString(Path.of(OFFERS_FILE_PATH));
    }

    @Override
    public String importOffers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();

        XmlParser.fromFile(OFFERS_FILE_PATH, OfferSeedRootDto.class)
                .getOffers()
                .stream()
                .peek(o -> sb.append(validationUtil.isValid(o) ? String.format("Successfully imported seller %s - %b%n", o.getAddedOn(), o.getHasGoldStatus()) : String.format("Invalid offer%n")))
                .filter(validationUtil::isValid)
                .map(offerSeedDto -> {
                    Offer offer = modelMapper.map(offerSeedDto, Offer.class);
                    offer.setSeller(sellerService.findById(offerSeedDto.getSeller().getId()));
                    offer.setCar(carService.findById(offerSeedDto.getCar().getId()));

                    return offer;
                })
                .forEach(offerRepository::save);

        return sb.toString();
    }
}
