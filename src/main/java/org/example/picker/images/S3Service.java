package org.example.picker.images;

import jakarta.persistence.AccessType;
import org.example.picker.room.RoomEntity;
import org.example.picker.room.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.SdkHttpMethod;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.io.IOException;
import java.net.URLConnection;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Autowired
    private S3Presigner s3Presigner;

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

    public ResponseEntity<byte[]> displayImagesOnJoin(Long roomId){
        RoomEntity existingRoom = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room does not exist"));
        byte[] image = getImageByRoomId(roomId);
        String filename = imageRepository.findByRoomId(roomId).getImageName();
        String contentType = URLConnection.guessContentTypeFromName(filename);

        if(filename==null){
            contentType = "application/octet-stream";
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        ContentDisposition.attachment().filename(filename).build().toString())
                .body(image);
    }

    public String generatePfpUrl(String filename, Long userId){
        String key = String.format(
                "user/%d/%s/%s",
                userId,
                UUID.randomUUID(),
                filename
        );

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(request)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

        return presignedPutObjectRequest.url().toString();
    }

    public String generateUploadPresignedUrl(String filename, Long roomId){
        RoomEntity room = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room not found"));

        String key = String.format(
                "room/%d/%s/%s",
                roomId,
                UUID.randomUUID(),
                filename
        );

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .putObjectRequest(request)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest = s3Presigner.presignPutObject(presignRequest);

        ImageEntity image = new ImageEntity();
        image.setImageName(filename);
        image.setUserId(roomRepository.getCreatorIdByRoomId(roomId));
        room.getImages().add(image);
        roomRepository.save(room);

        return presignedPutObjectRequest.url().toString();
    }

    public String generateDownloadPresignedUrl(String filename){
        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(filename)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(60))
                .getObjectRequest(request)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(presignRequest);

        return presignedGetObjectRequest.url().toString();
    }



}