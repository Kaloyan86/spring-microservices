package com.app.init;


import com.app.service.CarService;
import com.app.service.OfferService;
import com.app.service.PictureService;
import com.app.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@ConditionalOnProperty(
prefix = "command.line.runner",
value = "enabled",
havingValue = "true",
matchIfMissing = true)
@Component
public class DataInit implements CommandLineRunner {

    CarService carService;
    PictureService pictureService;
    OfferService offerService;
    SellerService sellerService;

    @Autowired
    public DataInit(CarService carService, PictureService pictureService, OfferService offerService, SellerService sellerService) {
        this.carService = carService;
        this.pictureService = pictureService;
        this.offerService = offerService;
        this.sellerService = sellerService;
    }

    @Override
    public void run(String... args) throws Exception {
        String carsInfo = carService.areImported() ? "Cars have already been imported!\n" : carService.importCars();
        String picturesInfo = pictureService.areImported() ? "Pictures have already been imported!\n" : pictureService.importPictures();
        String sellersInfo = sellerService.areImported() ? "Sellers have already been imported!\n" : sellerService.importSellers();
        String offersInfo = offerService.areImported() ? "Offers have already been imported!\n" : offerService.importOffers();

        System.out.println(carsInfo + picturesInfo + sellersInfo + offersInfo);
    }
}
