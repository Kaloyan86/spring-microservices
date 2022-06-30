package com.app.service;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public interface OfferService {

    boolean areImported();

	String importOffers() throws IOException, JAXBException;
}
