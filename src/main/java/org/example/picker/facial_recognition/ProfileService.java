package org.example.picker.facial_recognition;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileService {

    @Autowired
    ProfileEmbeddingRepository repository;

    @Autowired
    ImageEmbeddingsRepository imageRepository;

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

    public boolean saveImageEmbeddings(List<Double> embeddings, Long userId, Long roomId){
        if(embeddings==null){
            return false;
        }
        EmbeddingsEntity image = new EmbeddingsEntity();
        image.setImageEmbeddings(embeddings);
        image.setUserId(userId);
        image.setRoomId(roomId);
        imageRepository.save(image);
        return true;
    }


    public double getEucleadianDistance(List<List<Double>> pfpEmbeddings, List<List<Double>> imageEmbeddings){
        double total = 0;
        for(int i=0; i<pfpEmbeddings.size(); i++){
            List<Double> pfp = pfpEmbeddings.get(i);
            List<Double> img = imageEmbeddings.get(i);

            for(int j=0; j<pfp.size(); j++){
                double diff = pfp.get(j) - img.get(j);
                total = total + (diff*diff);
            }
        }
        return Math.sqrt(total);
    }

}
