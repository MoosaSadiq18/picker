package org.example.picker.images;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

/*@Service
public class S3Service {

        @Value("${aws.bucket.name}")
        private String bucketName;

        @Autowired
        S3Client s3Client;

        public void uploadFileToS3(MultipartFile file) throws IOException {
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(bucketName)
                            .key(file.getOriginalFilename())
                            .build(),
                    RequestBody.fromBytes(file.getBytes()));
        }
}
*/