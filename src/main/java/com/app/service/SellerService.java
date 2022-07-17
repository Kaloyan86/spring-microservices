package com.app.service;

import com.app.model.Seller;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface SellerService {
    
    boolean areImported();

    String importSellers() throws IOException, JAXBException;

    Seller findById(Long id);
}
