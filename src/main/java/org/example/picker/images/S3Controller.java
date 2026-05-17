package org.example.picker.images;

import org.example.picker.auth.UserRepository;
import org.example.picker.room.RoomEntity;
import org.example.picker.room.RoomRepository;
import org.example.picker.room.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.io.IOException;
import java.net.URLConnection;

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


    @GetMapping("/getUserProfileUrl")
    public ResponseEntity<String> getUserProfileUrl(@RequestParam String pfpName, @RequestParam Long userId){
        String url = s3Service.generatePfpUrl(pfpName,userId);
        if(url==null){
            throw new RuntimeException("Upload url not generated");
        }
        else{
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getUploadUrl")
    public ResponseEntity<String> getUploadUrl(@RequestParam String image,
                                               @RequestParam Long roomId){
        String url = s3Service.generateUploadPresignedUrl(image,roomId);
        if(url==null){
            throw new RuntimeException("Upload url not generated");
        }
        else{
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getdownloadUrl/{filename}")
    public ResponseEntity<String> getDownloadUrl(@PathVariable String filename){
        String url = s3Service.generateDownloadPresignedUrl(filename);
        if(url == null){
            throw new RuntimeException("Download url not generated");
        }
        else {
            return ResponseEntity.ok(url);
        }
    }

    @GetMapping("/getImage/{roomId}")
    public ResponseEntity<byte[]> getImageByRoomId(@PathVariable Long roomId) throws IOException{
        RoomEntity existingRoom = roomRepository.findById(roomId).orElseThrow(() -> new RuntimeException("Room doesnot exist"));
        byte[] image = s3Service.getImageByRoomId(roomId);
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

}

