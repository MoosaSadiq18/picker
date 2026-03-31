package org.example.picker.images;

import org.example.picker.room.RoomEntity;
import org.example.picker.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Service
public class S3Service {

    @Value("${aws.bucket.name}")
    private String bucketName;

    @Autowired
    S3Client s3Client;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ImageRepository imageRepository;


    public void uploadFileToS3(MultipartFile file, Long userId, Long roomId) throws IOException {
        s3Client.putObject(PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(file.getOriginalFilename())
                        .build(),
                RequestBody.fromBytes(file.getBytes()));

        RoomEntity room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));
        ImageEntity image = new ImageEntity();

        image.setImageName(file.getOriginalFilename());
        image.setUserId(userId);
        room.getImages().add(image);
        roomRepository.save(room);
    }

    public byte[] getImageByRoomId(Long roomId) {
        ImageEntity image = imageRepository.findByRoomId(roomId);

        String filename = image.getImageName();
        if(filename==null){
            throw new NullPointerException("Image " + image.getImageId() + " is empty");
        }
        else{
            ResponseBytes<GetObjectResponse> objectAsBytes =
                    s3Client.getObjectAsBytes(GetObjectRequest.builder()
                            .bucket(bucketName)
                            .key(filename)
                            .build());

            return objectAsBytes.asByteArray();
        }
    }

}