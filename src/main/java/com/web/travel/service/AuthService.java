package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.EUserStatus;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.ResetPasswordRequest;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.payload.response.AuthResponse;
import com.web.travel.payload.response.JwtResponse;
import com.web.travel.payload.response.MessageResponse;
import com.web.travel.repository.RoleRepository;
import com.web.travel.repository.UserRepository;
import com.web.travel.security.jwt.JwtUtils;
import com.web.travel.security.services.UserDetailsImpl;
import com.web.travel.service.email.EmailService;
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

import java.security.SecureRandom;
import java.util.*;
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
    EmailService emailService;
    @Autowired
    AuthenticationManager authenticationManager;
    public boolean userIsExistsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public Map<String, Object> saveUser(SignupRequest signUpRequest){
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
        user.setActive(EUserStatus.STATUS_NOT_ACTIVATED);

        String userFullName = signUpRequest.getFullName();
        String confirmationCode = generateConfirmationCode();
        String token = encodeResetPasswordToken(createConfirmationCodeToken(signUpRequest.getEmail(), confirmationCode));
        MessageResponse messageResponse = emailService.sendConfirmationEmail(signUpRequest.getEmail(), userFullName, token, confirmationCode);
        Map<String, Object> result = new HashMap<>();

        if(messageResponse.isStatus()){
            result.put("user", userRepository.save(user));
            result.put("token", token);
        }else{
            result.put("user", null);
            result.put("token", "");
        }

        return result;
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


    public boolean loginVerify(String token) {
        return jwtUtils.validateJwtToken(token);
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

    public String createConfirmationCodeToken(String email, String confirmationCode){
        return jwtUtils.generateJwtConfirmationToken(email, confirmationCode);
    }

    public String generateConfirmationCode(){
        SecureRandom rd = new SecureRandom();
        int min = 100000,
        max = 999999;
        int randomNumber = rd.nextInt(max - min + 1) + min;
        return String.valueOf(randomNumber);
    }

    public ResDTO confirmationCodeValidate(String confirmCode, String token){
        String[] jwtSubjects = jwtUtils.getTokenSubject(token).split(":");
        String code = jwtSubjects[1];
        String email = jwtSubjects[0];
        if(code.equals(confirmCode)){
            if(resetPasswordTokenIsValid(token)){
                User user = userRepository.findByEmail(email).orElse(null);
                if(user != null){
                    user.setActive(EUserStatus.STATUS_ACTIVATED);
                    userRepository.save(user);
                    return new ResDTO(
                            HttpServletResponse.SC_OK,
                            true,
                            "Confirmation code is valid",
                            null
                    );
                }
                return new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Can't found user with email: " + email,
                        null
                );
            }
            return new ResDTO(
                    HttpServletResponse.SC_BAD_REQUEST,
                    false,
                    "Confirmation code is expired",
                    null
            );
        }
        return new ResDTO(
                HttpServletResponse.SC_BAD_REQUEST,
                false,
                "Confirmation code is not correct",
                null
        );
    }
}
