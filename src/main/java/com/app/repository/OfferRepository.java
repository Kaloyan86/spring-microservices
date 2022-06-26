package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.models.entities.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
