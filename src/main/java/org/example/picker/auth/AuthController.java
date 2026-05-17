package org.example.picker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.s3.model.Bucket;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthDetailer authDetailer;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authService.register(request.getUsername(), request.getEmail(),request.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        if()
        return ResponseEntity.ok(authService.login(request.getUsername(),request.getPassword()));
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello " + authDetailer.getCurrentUsername());
    }

}
