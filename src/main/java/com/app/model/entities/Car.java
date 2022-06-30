package com.app.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cars")
public class Car extends BaseEntity {

    @Column(length = 20)
    private String make;

    @Column(length = 20)
    private String model;

    private Integer kilometers;

    @Column(name = "registered_on")
    private LocalDate registeredOn;

    @OneToMany(mappedBy = "car", fetch = FetchType.EAGER)
    private Set<Picture> pictures;

    @Override
    public String toString() {
        return String.format("Car make - %s, model - %s\n" +
                        "\tKilometers - %d\n" +
                        "\tRegistered on - %s\n" +
                        "\tNumber of pictures - %d\n",
                this.make,
                this.model,
                this.kilometers,
                this.registeredOn,
                this.pictures.size()
        );
    }
}
