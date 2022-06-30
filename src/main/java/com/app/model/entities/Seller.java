package com.app.model.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.app.model.entities.enums.Rating;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "sellers")
public class Seller extends BaseEntity {

    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Rating rating;
    @Column(nullable = false)
    private String town;
}
