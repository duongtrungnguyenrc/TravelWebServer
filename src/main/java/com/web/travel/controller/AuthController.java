package com.web.travel.controller;

import com.web.travel.model.User;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.AuthResponse;
import com.web.travel.payload.response.MessageResponse;
import com.web.travel.service.AuthService;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = authService.signIn(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (authService.userIsExistsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(0,"Error: Email is already in use!"));
        }

        User savedUser = authService.saveUser(signUpRequest);
        return savedUser != null ? ResponseEntity.ok(new MessageResponse(1, "User registered successfully!")) :
                ResponseEntity.badRequest().body(new MessageResponse(0,"User registered unsuccessful!"));
    }
}
