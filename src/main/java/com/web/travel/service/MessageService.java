package com.web.travel.service;

import com.web.travel.model.Message;
import com.web.travel.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;

    public List<Message> getMessages(Long room){
        return messageRepository.findAllByRoom(room);
    }
    public Message saveMessage(Message message){
        return messageRepository.save(message);
    }
}
