package org.example.picker.images;

import org.example.picker.auth.UserRepository;
import org.example.picker.room.RoomRepository;
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
    RoomService roomService;

    @Autowired
    S3Service s3Service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository;

    @PostMapping("/uploadImages")
    public ResponseEntity<String> uploadImages(@RequestParam("image")MultipartFile image
                                                ,@RequestParam String username
                                                ,@RequestParam String roomName) throws IOException {

        if(image.isEmpty()){
            return ResponseEntity.badRequest().body("Image is empty " + image.getOriginalFilename());
        }

        Long userId = userRepository.getUserIdByUsername(username);
        Long roomId = roomRepository.getRoomIdByRoomName(roomName);
        s3Service.uploadFileToS3(image,userId,roomId);

        return ResponseEntity.ok("Image " + image.getOriginalFilename() + " uploaded successfully");
    }
}
