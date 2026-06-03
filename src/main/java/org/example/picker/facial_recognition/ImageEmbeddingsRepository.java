package org.example.picker.facial_recognition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageEmbeddingsRepository extends JpaRepository<EmbeddingsEntity,Long> {

    @Query("select i.imageEmbeddings from EmbeddingsEntity i where i.imageId =:imageId")
    List<List<Double>> getImageEmbeddingsById(@Param("imageId") Long imageId);
}
