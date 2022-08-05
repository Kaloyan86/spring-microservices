package com.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.model.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

}
