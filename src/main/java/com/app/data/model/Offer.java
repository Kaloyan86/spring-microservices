package com.app.data.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "offers")
public class Offer extends BaseEntity {

    private BigDecimal price;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Boolean hasGoldStatus;
    private LocalDateTime addedOn;
    @ManyToOne
    private Car car;
    @ManyToOne
    private Seller seller;
    @ManyToMany
    private Set<Picture> pictures;
}
