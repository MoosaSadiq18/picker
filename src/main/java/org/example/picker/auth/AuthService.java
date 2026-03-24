package org.example.picker.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(String email, String password){

        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if(existingUser.isPresent()){
            return "User already exists";
        }

        UserEntity user = new UserEntity();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("USER");
        userRepository.save(user);

        return "Registered Successfully";
    }

    public String login(String email, String password){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        return jwtUtil.generateToken(email);
    }
}
