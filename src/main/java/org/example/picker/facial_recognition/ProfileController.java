package org.example.picker.facial_recognition;
import org.example.picker.images.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ProfileController {

    @Autowired
    S3Service s3Service;

    @Autowired
    ProfileService profileService;

    @PostMapping("/sendPfpUrl")
    public ResponseEntity<String> sendPfpUrl(String pfpUrl, Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String receiverUrl = "http://localhost:8000/uploadPfpEmbeddings";

        Map<String, Object> body = new HashMap<>();
        body.put("pfpUrl", pfpUrl);
        body.put("userId", userId);

        ResponseEntity<String> response = restTemplate.postForEntity(receiverUrl, body, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/sendImageUrl")
    public ResponseEntity<String> sendImageUrl(String imageUrl, Long userId) {
        RestTemplate restTemplate = new RestTemplate();
        String receiverUrl = "http://localhost:8000/uploadImageEmbeddings";

        Map<String, Object> body = new HashMap<>();
        body.put("imageUrl", imageUrl);
        body.put("userId", userId);
        body.put("roomId", 1);
        body.put("position", 0);

        ResponseEntity<String> response = restTemplate.postForEntity(receiverUrl, body, String.class);
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("/getPfpEmbeddings")
    public ResponseEntity<String> getPfpEmbeddings(@RequestBody PfpEmbeddingRequest request) {
        if (request.getEmbeddings() == null) {
            throw new NullPointerException("Embeddings are empty");
        }
        boolean saved = profileService.savePfpEmbeddings(request.getEmbeddings(), request.getUserId());

        if (!saved) {
            return ResponseEntity.badRequest().body("Could not save embeddings");
        }
        return ResponseEntity.ok("Pfp embeddings stored");
    }

    @PostMapping("/getImageEmbeddings")
    public ResponseEntity<String> getImageEmbeddings(@RequestBody ImageEmbeddingRequest request) {
        if (request.getEmbeddings() == null) {
            throw new NullPointerException("Embeddings are empty");
        }
        boolean saved = profileService.saveImageEmbeddings(
                request.getEmbeddings(),
                request.getUserId(),
                request.getRoomId(),
                request.getPosition()
                );

        if (!saved) {
            return ResponseEntity.badRequest().body("Could not save embeddings");
        }
        return ResponseEntity.ok("Image embeddings stored");
    }

    @Autowired
    ImageEmbeddingsRepository imageRepository;

    @Autowired
    ProfileEmbeddingRepository pfpRepository;

    private final Double THRESHOLD = 0.6;

    @GetMapping("/getMatches")
    public ResponseEntity<String> getFacialMatches(@RequestParam Long userId,
                                                   @RequestParam Long roomId) {

        List<List<Double>> pfpEmbedddings = pfpRepository.getPfpEmbeddingsByUserId(userId);
        int imagesCount = imageRepository.getImagesCount(roomId);
        int matchedCount = 0;
        int unmatchedCount = 0;

        for(int position=0; position<imagesCount; position++){
            List<List<Double>> imageEmbedddings = imageRepository.getImageEmbeddingsByRoomId(roomId,position);
            Long imageId = imageRepository.getImageId(roomId,position);

            for(int j=0; j<pfpEmbedddings.size(); j++){
                double result = profileService.getEucleadianDistance(pfpEmbedddings, imageEmbedddings);
                if(result < THRESHOLD){
                    matchedCount++;
                    profileService.displayUserImage(userId,roomId,imageId);
                }
                unmatchedCount++;
            }
        }


        for(int i=0;i<pfpEmbedddings.size();i++){
            List<Double> p = pfpEmbedddings.get(i);
            for(int j=0;j<p.size();j++){
                System.out.println("Embeddings are " + p.get(j));
            }
        }


        return ResponseEntity.ok("Images matched: " + matchedCount + " and Images not matched: " + unmatchedCount);

    }
}
