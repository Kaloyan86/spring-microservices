package com.app.service.impl;

import com.app.api.SellerSeedRootDto;
import com.app.model.Seller;
import com.app.repository.SellerRepository;
import com.app.service.util.ValidationUtil;
import com.app.service.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import com.app.service.SellerService;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.NoSuchElementException;

@Service
public class SellerServiceImpl implements SellerService {

    private static final String SELLER_FILE_PATH = "src/main/resources/files/xml/sellers.xml";

    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final SellerRepository sellerRepository;

    public SellerServiceImpl(ModelMapper modelMapper, ValidationUtil validationUtil, SellerRepository sellerRepository) {
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.sellerRepository = sellerRepository;
    }

    @Override
    public boolean areImported() {
        return !sellerRepository.findAll().isEmpty();
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
