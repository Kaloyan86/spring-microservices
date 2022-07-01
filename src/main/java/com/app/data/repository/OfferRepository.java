package com.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.data.model.Offer;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

}
