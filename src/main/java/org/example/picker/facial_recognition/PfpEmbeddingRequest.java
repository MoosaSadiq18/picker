package org.example.picker.facial_recognition;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PfpEmbeddingRequest {

    private Long userId;
    private List<Double> embeddings;
}
