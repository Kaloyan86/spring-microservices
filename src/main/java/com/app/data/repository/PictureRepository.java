package com.app.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.data.model.Picture;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

}
