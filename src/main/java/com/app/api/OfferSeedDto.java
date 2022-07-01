package com.app.api;

import lombok.Data;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import java.math.BigDecimal;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class OfferSeedDto {

    @Size(min = 5)
    @XmlElement(name = "description")
    private String description;
    @Positive
    @XmlElement(name = "price")
    private BigDecimal price;
    @XmlElement(name = "added-on")
    private String addedOn;
    @XmlElement(name = "has-gold-status")
    private Boolean hasGoldStatus;
    @XmlElement(name = "car")
    private CarIdDto car;
    @XmlElement(name = "seller")
    private SellerIdDto seller;
}
