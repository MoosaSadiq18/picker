package org.example.picker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private AuthDetailer authDetailer;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.register(request.getEmail(),request.getPassword()));
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request.getEmail(),request.getPassword()));
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("Hello " + authDetailer.getCurrentUserEmail());
    }

}
