package org.example.picker.images;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<ImageEntity,Long> {

    @Query("select i.imageName from ImageEntity i where i.roomId =:roomId")
    List<String> getAllImageName(@Param("roomId") Long roomId);

    @Query("select i.imageName from ImageEntity i where i.imageId =:imageId")
    String getFilenameByImageId(@Param("imageId") Long imageId);

    @Query("select i.imageId from ImageEntity i where i.imageName =:imageName")
    Long getImageIdByFilename(@Param("imageName") String imageName);
}
