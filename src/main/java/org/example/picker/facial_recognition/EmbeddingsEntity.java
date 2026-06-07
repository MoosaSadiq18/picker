package org.example.picker.facial_recognition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.picker.images.ImageEntity;

import java.util.List;

@Entity
@Table(name = "image_embeddings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingsEntity {

    @Id
    private Long imageId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "image_id")
    private ImageEntity image;

    private Long userId;
    private Long roomId;
    private int position;

    @Lob
    private List<Double> imageEmbeddings;

}
