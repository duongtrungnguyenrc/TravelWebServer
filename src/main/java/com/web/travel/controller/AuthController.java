package com.web.travel.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.web.travel.model.ERole;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.JwtResponse;
import com.web.travel.payload.response.MessageResponse;
import com.web.travel.security.services.UserDetailsImpl;
import com.web.travel.service.AuthService;
import jakarta.validation.Valid;
import com.web.travel.repository.UserRepository;
import com.web.travel.repository.RoleRepository;
import com.web.travel.security.jwt.JwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
        JwtResponse jwtResponse = authService.signIn(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (authService.userIsExistsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User savedUser = authService.saveUser(signUpRequest);
        return savedUser != null ? ResponseEntity.ok(new MessageResponse("User registered successfully!")) :
                ResponseEntity.badRequest().body(new MessageResponse("User registered unsuccessful!"));
    }
}
