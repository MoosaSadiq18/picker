package org.example.picker.facial_recognition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "image_embeddings")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmbeddingsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    private Long imageGroupId;

    private Long userId;
    private Long roomId;
    private int position;

    @Lob
    private List<Double> imageEmbeddings;

}
