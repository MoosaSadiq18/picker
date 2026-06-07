package org.example.picker.facial_recognition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageEmbeddingRequest {

    private Long imageId;
    private Long userId;
    private Long roomId;
    private int position;
    private List<Double> embeddings;
}
