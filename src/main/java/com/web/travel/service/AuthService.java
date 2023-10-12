package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.ResetPasswordRequest;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.AuthResponse;
import com.web.travel.payload.response.JwtResponse;
import com.web.travel.repository.RoleRepository;
import com.web.travel.repository.UserRepository;
import com.web.travel.security.jwt.JwtUtils;
import com.web.travel.security.services.UserDetailsImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    public boolean userIsExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public User saveUser(SignupRequest signUpRequest){
        // Create new user's account
        User user = new User(signUpRequest.getFullName(),
                signUpRequest.getAddress(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getPhone());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "tour":
                        Role tourRole = roleRepository.findByName(ERole.ROLE_TOUR_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(tourRole);

                        break;
                    case "customer_care":
                        Role customerCareRole = roleRepository.findByName(ERole.ROLE_CUSTOMER_CARE)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(customerCareRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        return userRepository.save(user);
    }

    public ResDTO signIn(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        JwtResponse jwtResponse = new JwtResponse(
                jwt,
                userDetails.getId(),
                userDetails.getEmail(),
                roles
        );

        return new ResDTO(HttpServletResponse.SC_OK, true, "Đăng nhập thành công", jwtResponse);
    }

    public ResDTO resetPassword(String password, String token){
        String userEmail = getEmailFromTokenForReset(token);
        User user = userRepository.findByEmail(userEmail).orElse(null);
        if(user != null){
            user.setPassword(encoder.encode(password));
            userRepository.save(user);
            return new ResDTO(HttpServletResponse.SC_OK,
                    true,
                    "Changed password successfully",
                    "");
        }
        return new ResDTO(HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Changed password unsuccessfully",
                "");
    }

    public String getEmailFromToken(String token){
        token = parseToken(token);
        return jwtUtils.getEmailFromJwtToken(token);
    }

    public String getEmailFromTokenForReset(String token){
        return jwtUtils.getEmailFromJwtToken(token);
    }

    private String parseToken(String token){
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    public String createResetPasswordToken(String email){
        return jwtUtils.generateJwtMailToken(email);
    }

    public boolean resetPasswordTokenIsValid(String token){
        return jwtUtils.validateJwtToken(token);
    }
    public String encodeResetPasswordToken(String token){
        Base64.Encoder encoder = Base64.getEncoder().withoutPadding();
        return encoder.encodeToString(token.getBytes());
    }

    public String decodeResetPasswordToken(String encodedToken){
        Base64.Decoder decoder = Base64.getDecoder();
        byte[] tokenBytes = decoder.decode(encodedToken);
        return new String(tokenBytes);
    }

    public String getUserFullNameFromEmail(String email){
        User user = userRepository.findByEmail(email).orElse(null);
        if(user != null){
            return user.getFullName();
        }
        return "";
    }
}
