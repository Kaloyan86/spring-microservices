package com.app.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.app.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    @Query("SELECT c FROM Car c ORDER BY size(c.pictures) DESC , c.make ASC ")
    List<Car> findAllCarsOrderByPicturesThenByMake();

    @Override
    void delete(Car entity);
}
