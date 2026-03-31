package org.example.picker.auth;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthDetailer {

    public String getCurrentUsername(){
        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }
}
