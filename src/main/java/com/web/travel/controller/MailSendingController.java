package com.web.travel.controller;

import com.web.travel.dto.ResDTO;
import com.web.travel.payload.request.MailRequest;
import com.web.travel.service.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
@RestController
@RequestMapping("/api/mail")
public class MailSendingController {
    @Autowired
    private EmailService service;
    @PostMapping("/sendingEmail")
    public ResponseEntity<?> sendWelcomeEmail(@RequestBody MailRequest request) {
        Map<String, Object> model = new HashMap<>();
        model.put("name", request.getName());
        ResDTO response = service.sendWelcomeEmail(request, model);
        boolean isOk = response.isStatus();
        return isOk ? ResponseEntity.ok(response) :
                ResponseEntity.badRequest().body(response);
    }
}
