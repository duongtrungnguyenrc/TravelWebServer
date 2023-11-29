package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.ConfirmCodeRequest;
import com.web.travel.payload.request.MailResetPasswordRequest;
import com.web.travel.service.AuthService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/confirm")
@CrossOrigin(origins = "*")
public class ConfirmationController {
    @Autowired
    AuthService authService;

    @PostMapping("/validate")
    public ResponseEntity<ResDTO> validateConfirmCode(@RequestBody ConfirmCodeRequest request){
        String decodedToken = authService.decodeResetPasswordToken(request.getToken());
        ResDTO response = authService.confirmationCodeValidate(request.getConfirmCode(), decodedToken);
        return response.isStatus() ?
            ResponseEntity.ok(
                    response
            ) :
            ResponseEntity.badRequest().body(
                    response
            );
    }
}
