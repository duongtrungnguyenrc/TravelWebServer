package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.MailRequest;
import com.web.travel.payload.request.MailResetPasswordRequest;
import com.web.travel.payload.request.ResetPasswordRequest;
import com.web.travel.service.AuthService;
import com.web.travel.service.email.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reset-password")
public class ResetPasswordController {
    @Autowired
    EmailService emailService;
    @Autowired
    AuthService authService;

    @GetMapping("/check")
    public ResponseEntity<?> validateToken(@RequestParam(value = "token", required = true) String token){
        if(authService.resetPasswordTokenIsValid(token)){
            return ResponseEntity.ok(
                    new ResDTO(
                            HttpServletResponse.SC_BAD_REQUEST,
                            false,
                            "Token is not valid",
                            ""
                    )
            );
        }
        return ResponseEntity.ok(
                new ResDTO(
                        HttpServletResponse.SC_OK,
                        true,
                        "Token is valid",
                        ""
                )
        );
    }

    @PostMapping("/change")
    public ResponseEntity<?> changePassword(@RequestBody ResetPasswordRequest request){
        if(authService.resetPasswordTokenIsValid(request.getToken())){
            return ResponseEntity.ok(
                    authService.resetPassword(request)
            );
        }
        return ResponseEntity.badRequest().body(
                new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Token is not valid",
                        ""
                )
        );
    }
    @PostMapping("/send-mail")
    public ResponseEntity<?> mailSending(@RequestBody MailResetPasswordRequest mail){
        String url = "http://localhost:8080/api/reset-password?token=";
        String token = authService.createResetPasswordToken(mail.getMail());
        url += token;
        MailRequest mailRequest = new MailRequest();
        mailRequest.setSubject("RESET PASSWORD");
        mailRequest.setTo(mail.getMail());
        mailRequest.setFrom("travel-vn");
        Map<String, Object> model = new HashMap<>();
        model.put("url", url);
        model.put("name", "Viet");
        if(emailService.sendResetPasswordEmail(mailRequest, model).isStatus()){
            return ResponseEntity.ok(
                    new ResDTO(
                            HttpServletResponse.SC_OK,
                            true,
                            "Mail sent successfully",
                            null
                    )
            );
        }
        return ResponseEntity.badRequest().body(
                new ResDTO(
                        HttpServletResponse.SC_BAD_REQUEST,
                        false,
                        "Mail sent failure",
                        null
                )
        );
    }
}
