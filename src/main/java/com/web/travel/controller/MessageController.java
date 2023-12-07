package com.web.travel.controller;

import com.web.travel.model.Message;
import com.web.travel.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @CrossOrigin(origins = "*")
    @GetMapping("/{room}")
    public ResponseEntity<List<Message>> getMessages(@PathVariable Long room) {
        return ResponseEntity.ok(messageService.getMessages(room));
    }
}
