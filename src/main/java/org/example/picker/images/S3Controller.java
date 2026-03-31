package org.example.picker.images;

import org.example.picker.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class S3Controller {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    RoomService roomService;

    @Autowired
    S3Service s3Service;

    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImages(@RequestParam("image")MultipartFile image
                                                ,@RequestParam Long userId
                                                ,@RequestParam Long roomId) throws IOException {

        if(image.isEmpty()){
            return ResponseEntity.badRequest().body("Image is empty " + image.getOriginalFilename());
        }

        s3Service.uploadFileToS3(image);
        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImageName(image.getName());
        imageEntity.setUserId(userId);
        imageEntity.setRoomId(roomId);
        imageEntity.setImageName(imageEntity.getImageName());
        imageRepository.save(imageEntity);

        return ResponseEntity.ok("Image " + image.getOriginalFilename() + " uploaded successfully");
    }
}
