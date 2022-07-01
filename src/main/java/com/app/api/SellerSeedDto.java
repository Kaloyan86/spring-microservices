package com.app.api;

import com.app.data.model.Rating;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerSeedDto {

    @Size(min = 2, max = 20)
    @XmlElement(name = "first-name")
    private String firstName;
    @Size(min = 2, max = 20)
    @XmlElement(name = "last-name")
    private String lastName;
    @Email
    @XmlElement(name = "email")
    private String email;
    @NotNull
    @XmlElement(name = "rating")
    private Rating rating;
    @NotBlank
    @XmlElement(name = "town")
    private String town;
}
