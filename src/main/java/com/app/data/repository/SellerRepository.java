package com.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.data.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
