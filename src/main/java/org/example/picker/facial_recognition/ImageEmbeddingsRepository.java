package org.example.picker.facial_recognition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageEmbeddingsRepository extends JpaRepository<EmbeddingsEntity,Long> {

    @Query("select i.imageEmbeddings from EmbeddingsEntity i where i.roomId =:roomId and i.position =:position")
    List<List<Double>> getImageEmbeddingsByRoomId(@Param("imageId") Long imageId, @Param("roomId") Long roomId, @Param("position") int position);

    @Query("select count(imageEmbeddings) from EmbeddingsEntity where roomId =:roomId")
    int getImagesCount(@Param("roomId") Long roomId);

    @Query("select i.imageId from EmbeddingsEntity i where i.roomId =:roomId and i.position =:position")
    Long getImageId(@Param("roomId") Long roomId, @Param("position") int position);
}
