package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.response.UserResDTO;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.payload.request.LoginRequest;
import com.web.travel.payload.request.LoginVerifyRequest;
import com.web.travel.payload.request.SignupRequest;
import com.web.travel.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthService authService;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        ResDTO authResponse = authService.signIn(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (authService.userIsExistsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResDTO(HttpServletResponse.SC_BAD_REQUEST, false, "Email đã tồn tại!", null));
        }

        Map<String, Object> result = authService.saveUser(signUpRequest);
        User savedUser = (User) result.get("user");
        String token = (String) result.get("token");

        if(savedUser == null){
            return ResponseEntity.ok(
                    new ResDTO(HttpServletResponse.SC_OK
                            , true, "Đăng ký tài khoản thất bại, hãy thử lại sau!"
                            , null)
            );
        }

        Map<String, Object> response = new HashMap<>();
        List<ERole> eRoleList = savedUser.getRoles().stream().map(Role::getName).toList();
        response.put("user", new UserResDTO(savedUser.getId(), savedUser.getEmail(), eRoleList));
        response.put("confirmToken", token);
        return ResponseEntity.ok(
                    new ResDTO(HttpServletResponse.SC_OK,
                            true,
                            "Đăng ký tài khoản thành công!",
                            response
                    )
                );
    }

    @PostMapping("/verify")
    public ResponseEntity<?> loginVerify(@Valid @RequestBody LoginVerifyRequest loginVerifyRequest) {
        if (!authService.loginVerify(loginVerifyRequest.getToken())) {
            return ResponseEntity
                    .badRequest()
                    .body(new ResDTO(HttpServletResponse.SC_BAD_REQUEST, false, "Xác thực đăng nhập thất bại!", null));
        }
        return ResponseEntity
                .ok()
                .body(new ResDTO(HttpServletResponse.SC_OK, true, "Xác thực đăng nhập thành công!", null));
    }
}
