package org.example.picker.images;

import lombok.RequiredArgsConstructor;
import org.example.picker.auth.AuthDetailer;
import org.example.picker.auth.UserRepository;
import org.example.picker.auth.UserService;
import org.example.picker.facial_recognition.ProfileController;
import org.example.picker.room.RoomEntity;
import org.example.picker.room.RoomRepository;
import org.example.picker.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class S3Controller {

    private final RoomService roomService;
    private final S3Service s3Service;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final ImageRepository imageRepository;
    private final ProfileController profileController;
    private final UserService userService;
    private final AuthDetailer authDetailer;

    @GetMapping("/getUploadProfileUrl")
    public ResponseEntity<String> getUserProfileUrl(@RequestParam String pfpName,
                                                    @RequestParam Long userId){
        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }

        String url = s3Service.generatePfpUploadUrl(pfpName,userId);
        if(url==null){
            throw new RuntimeException("Upload url not generated");
        }
        else{
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getUploadImageUrl")
    public ResponseEntity<String> getUserImageUrl(@RequestParam String pfpName,
                                                  @RequestParam Long userId,
                                                  @RequestParam Long roomId){

        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }

        String username = userRepository.getUsernameByUserId(userId);
        if(!authDetailer.getCurrentUsername().matches(username)){
            return ResponseEntity.badRequest().body("Room creator " + username + " doesnot exist and authDetailer is "+ authDetailer.getCurrentUsername());
        }

        String url = s3Service.generateImageUploadUrl(pfpName,userId,roomId);
        if(url==null){
            throw new RuntimeException("Upload url not generated");
        }
        else{
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getDownloadPfpUrl")
    public ResponseEntity<String> getDownloadPfpUrl(@RequestParam String pfpName, @RequestParam Long userId){
        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }

        String url = s3Service.generateDownloadPresignedUrl(pfpName);
        if(url == null){
            throw new RuntimeException("Download url not generated");
        }
        else {
            profileController.sendPfpUrl(url,userId);
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getDownloadImageUrl")
    public ResponseEntity<String> getDownloadImageUrl(@RequestParam Long userId,
                                                      @RequestParam Long roomId){
        if(!userService.isUserAllowed(userId)){
            return ResponseEntity.badRequest().body("User not authorized");
        }

        List<String> imageName = imageRepository.getAllImageName(roomId);
        System.out.println("Got imageName");
        for(String imgName: imageName) {
            System.out.println(imgName);
        }

        for(String imgName: imageName){
            String url = s3Service.generateDownloadPresignedUrl(imgName);
            if(url == null){
                return ResponseEntity.badRequest().body("Download url not generated");
            }
            else {
                System.out.println("Sending image: " + imgName + " userId: " + userId);
                profileController.sendImageUrl(url,userId,roomId);
                System.out.println("Sent image: " + imgName + " userId: " + userId);
            }
        }

        return ResponseEntity.ok("Images download urls sent succesfully");
    }

}

