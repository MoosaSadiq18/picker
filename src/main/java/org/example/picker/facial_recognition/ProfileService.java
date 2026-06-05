package org.example.picker.facial_recognition;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final ProfileEmbeddingRepository repository;
    private final ImageEmbeddingsRepository imageRepository;

    public boolean savePfpEmbeddings(List<Double> embeddings, Long userId){
        if(embeddings==null){
            return false;
        }
        ProfileEntity profile = new ProfileEntity();
        profile.setProfileEmbedding(embeddings);
        profile.setUserId(userId);
        repository.save(profile);
        return true;
    }

    public boolean saveImageEmbeddings(List<Double> embeddings, Long userId, Long roomId, int position){
        if(embeddings==null){
            return false;
        }
        EmbeddingsEntity image = new EmbeddingsEntity();
        image.setImageEmbeddings(embeddings);
        image.setUserId(userId);
        image.setRoomId(roomId);
        image.setPosition(position);
        imageRepository.save(image);
        return true;
    }


    public double getEucleadianDistance(List<List<Double>> pfpEmbeddings, List<List<Double>> imageEmbeddings){
        double total = 0;
        for(int i=0; i<pfpEmbeddings.size(); i++){
            List<Double> pfp = normalize(pfpEmbeddings.get(i));
            List<Double> img = normalize(imageEmbeddings.get(i));

            for(int j=0; j<pfp.size(); j++){
                double diff = pfp.get(j) - img.get(j);
                total = total + (diff*diff);
            }
        }
        return Math.sqrt(total);
    }

    public List<Double> normalize(List<Double> embedding) {
        double norm = 0.0;
        for(double v : embedding) {
            norm += v * v;
        }

        norm = Math.sqrt(norm);
        List<Double> result = new ArrayList<>(embedding.size());

        for (double v : embedding) {
            result.add(v / norm);
        }
        return result;
    }

    public void displayUserImage(Long userId, Long roomId, Long imageId){

    }

}
