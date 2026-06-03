package org.example.picker.facial_recognition;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "profile_embeddings")
@Data
public class ProfileEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long imageId;
    private Long userId;

    @Lob
    private List<Double> profileEmbedding;
}
