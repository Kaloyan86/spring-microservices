package softuni.exam.models.entities;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;

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
}
