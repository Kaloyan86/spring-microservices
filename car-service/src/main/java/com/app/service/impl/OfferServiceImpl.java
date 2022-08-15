package com.app.service.impl;

import com.app.api.OfferSeedRootDto;
import com.app.model.Offer;
import com.app.repository.OfferRepository;
import com.app.service.CarService;
import com.app.service.SellerService;
import com.app.service.util.ValidationUtil;
import com.app.service.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.service.OfferService;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Service
public class OfferServiceImpl implements OfferService {

    private static final String OFFERS_FILE_PATH = "car-service/src/main/resources/files/xml/offers.xml";

    private final CarService carService;
    private final SellerService sellerService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final OfferRepository offerRepository;

    public OfferServiceImpl(CarService carService, SellerService sellerService, ModelMapper modelMapper, ValidationUtil validationUtil, OfferRepository offerRepository) {
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
