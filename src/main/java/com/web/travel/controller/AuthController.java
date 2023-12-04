package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.response.UserResDTO;
import com.web.travel.mapper.response.UserDetailResMapper;
import com.web.travel.model.Role;
import com.web.travel.model.User;
import com.web.travel.model.enumeration.ERole;
import com.web.travel.payload.request.*;
import com.web.travel.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.Principal;
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
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> authenticateUser(HttpServletRequest request, @Valid @RequestBody LoginRequest loginRequest) {
        ResDTO authResponse = authService.signIn(request, loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/signup")
    @CrossOrigin(origins = "*")
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
            return ResponseEntity.badRequest().body(
                    new ResDTO(HttpServletResponse.SC_BAD_REQUEST
                    , false
                    , "Đăng ký tài khoản thất bại, hãy thử lại sau!"
                    , null));
        }

        Map<String, Object> response = new HashMap<>();
        List<ERole> eRoleList = savedUser.getRoles().stream().map(Role::getName).toList();
        UserDetailResMapper userMapper = new UserDetailResMapper();
        UserResDTO dto = (UserResDTO) userMapper.mapToDTO(savedUser);
        response.put("user", dto);
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
    @CrossOrigin(origins = "*")
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

    @GetMapping("/sign-with-google")
    @CrossOrigin(origins = "*")
    public void googleAuthorize(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/oauth2/authorization/google");
    }

    @GetMapping("/sign-with-github")
    @CrossOrigin(origins = "*")
    public void githubAuthorize(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/oauth2/authorization/github");
    }

    @GetMapping("/sign-with-facebook")
    @CrossOrigin(origins = "*")
    public void facebookAuthorize(HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.sendRedirect("/oauth2/authorization/facebook");
    }

    @PostMapping("/activate")
    @CrossOrigin(origins = "*")
    public ResponseEntity<ResDTO> validateConfirmCode(@RequestBody ConfirmCodeRequest request){
        String decodedToken = authService.decodeResetPasswordToken(request.getToken());
        ResDTO response = authService.confirmationCodeValidate(request.getActivateCode(), decodedToken);
        return response.isStatus() ?
                ResponseEntity.ok(
                        response
                ) :
                ResponseEntity.badRequest().body(
                        response
                );
    }

    @PostMapping("/change-password")
    @CrossOrigin(origins = "*")
    public ResponseEntity<?> changePassword(Principal principal, @RequestBody ChangePasswordRequest request){
        ResDTO response = authService.changePassword(principal, request);
        return response.isStatus() ? ResponseEntity.ok(response) : ResponseEntity.badRequest().body(response);
    }
}
