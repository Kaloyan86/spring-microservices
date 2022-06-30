package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.model.entities.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
