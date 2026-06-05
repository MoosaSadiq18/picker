package org.example.picker.facial_recognition;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Repository
public interface ProfileEmbeddingRepository extends JpaRepository<ProfileEntity,Long> {

    @Query("select i.profileEmbedding from ProfileEntity i where i.userId =:userId")
    List<List<Double>> getPfpEmbeddingsByUserId(@Param("userId") Long userId);

}
