package org.example.picker.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Long> {

    @Query("select count(imageName) from ImageEntity where roomId =:roomId")
    int getImagesCount(@Param("roomId") Long roomId);

    @Query("select imageName from ImageEntity where roomId =:roomId")
    List<String> getImageName(@Param("roomId") Long roomId);
}
