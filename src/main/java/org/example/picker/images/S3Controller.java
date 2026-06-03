package org.example.picker.images;

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
public class S3Controller {

    @Autowired
    RoomService roomService;

    @Autowired
    S3Service s3Service;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    ProfileController profileController;

    @Autowired
    UserService userService;

    @GetMapping("/getUploadProfileUrl")
    public ResponseEntity<String> getUserProfileUrl(@RequestParam String pfpName,
                                                    @RequestParam Long userId){
       /* if(!userService.isUserAllowed(userId)){
            throw new AuthorizationDeniedException("User not authorized");
        }*/

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
       /* if(!userService.isUserAllowed(userId)){
            throw new AuthorizationDeniedException("User not authorized");
        }*/

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
      /*  if(!userService.isUserAllowed(userId)){
            throw new AuthorizationDeniedException("User not authorized");
        }*/

        String url = s3Service.generateDownloadPresignedUrl(pfpName);
        if(url == null){
            throw new RuntimeException("Download url not generated");
        }
        else {
            profileController.sendPfpUrl(url,userId);
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getdownloadImageUrl")
    public ResponseEntity<String> getDownloadImageUrl(@RequestParam String imageName, @RequestParam Long userId){

        String url = s3Service.generateDownloadPresignedUrl(imageName);
        if(url == null){
            throw new RuntimeException("Download url not generated");
        }
        else {
            profileController.sendImageUrl(url,userId);
            return ResponseEntity.ok(url);
        }
    }

}

