package com.web.travel.service;

import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.web.travel.model.Message;
import com.web.travel.model.User;
import com.web.travel.utils.DateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SocketService {
    public final MessageService messageService;
    public final UserService userService;

    public void sendSocketMessage(SocketIOClient senderClient, Object message, Long room, boolean isOnConnected){
        senderClient
                .getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(client -> {
                    boolean isAdmin = client.getHandshakeData().getUrlParams().get("admin") != null;
                    if((isOnConnected && !isAdmin) || !client.getSessionId().equals(senderClient.getSessionId())){
                        client.sendEvent("receive", message);
                    }
                });
    }

    public void sendChangeEvent(SocketIOClient senderClient, Object message, Long room){
        senderClient.getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(socketIOClient -> {
                    if(!socketIOClient.getSessionId().equals(senderClient.getSessionId()))
                        socketIOClient.sendEvent("change", message);
                });
    }

    public void sendStopChangeEvent(SocketIOClient senderClient, Object message, Long room){
        senderClient.getNamespace()
                .getRoomOperations(String.valueOf(room))
                .getClients()
                .forEach(socketIOClient -> {
                    if(!socketIOClient.getSessionId().equals(senderClient.getSessionId()))
                        socketIOClient.sendEvent("stop-change", message);
                });
    }

    public void saveMessage(SocketIOClient senderClient, Message message){
        User user = userService.getUserObjectById(message.getUid());
        String avatar = "";
        if(user != null)
            avatar = user.getAvatar() != null ? user.getAvatar() : "";
        Message savedMessage = messageService.saveMessage(
                Message.builder()
                        .message(message.getMessage())
                        .room(message.getRoom())
                        .time(message.getTime())
                        .uid(message.getUid())
                        .role(message.getRole().toUpperCase())
                        .avatar(avatar)
                        .name(
                                (user != null && user.getFullName() != null)
                                        ? user.getFullName()
                                        : "Unknown"
                        )
                        .build()
        );

        sendSocketMessage(senderClient, savedMessage, message.getRoom(), false);
    }

    public void saveInfoMessage(SocketIOClient senderClient, String message, Long room, boolean isAdmin) {

        Message newMessage = new Message();
        newMessage.setRole("ADMIN");
        newMessage.setMessage(message);
        newMessage.setTime(DateHandler.getCurrentDateTime());
        newMessage.setName("Hệ thống");
        newMessage.setAvatar("");
        newMessage.setRoom(room);

        messageService.saveMessage(newMessage);
        List<Message> messages = messageService.getMessages(room);
        if(!isAdmin)
            sendSocketMessage(senderClient, messages, room, true);
    }
}
