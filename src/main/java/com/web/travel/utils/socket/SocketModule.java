package com.web.travel.utils.socket;

import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.corundumstudio.socketio.listener.DataListener;
import com.corundumstudio.socketio.listener.DisconnectListener;
import com.web.travel.model.Message;
import com.web.travel.service.SocketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@Slf4j
public class SocketModule {
    private final SocketIOServer socketIOServer;
    private final SocketService socketService;

    public SocketModule(SocketIOServer socketIOServer, SocketService socketService) {
        this.socketIOServer = socketIOServer;
        this.socketService = socketService;
        socketIOServer.addConnectListener(onConnected());
        socketIOServer.addEventListener("send", Message.class, onChatReceived());
        socketIOServer.addEventListener("change", Message.class, onChangeReceived());
        socketIOServer.addEventListener("stop-change", Message.class, onStopChangeReceived());
    }

    private DataListener<Message> onStopChangeReceived() {
        return (senderClient, data, ackSender) -> {
            log.info("On stop change...");

            Long room = Long.valueOf(senderClient.getHandshakeData().getUrlParams().get("room").stream().collect(Collectors.joining("")));
            socketService.sendStopChangeEvent(senderClient, data, room);
        };
    }

    private DataListener<Message> onChangeReceived() {
        return (senderClient, data, ackSender) -> {
            log.info("On change...");

            Long room = Long.valueOf(senderClient.getHandshakeData().getUrlParams().get("room").stream().collect(Collectors.joining("")));
            socketService.sendChangeEvent(senderClient, data, room);
        };
    }

    private DataListener<Message> onChatReceived() {
        return (senderClient, data, ackSender) -> {
            log.info(data.toString());
            socketService.saveMessage(senderClient, data);
        };
    }

    private ConnectListener onConnected() {
        return (client) -> {
            var params = client.getHandshakeData().getUrlParams();
            String room = String.join("", params.get("room"));
            client.joinRoom(room);
            socketService.sendConnectedMessage(client, Long.valueOf(room), params.get("admin") != null);

            log.info("Socket ID[{}] - room[{}] Connected to chat module through", client.getSessionId().toString(), room);
        };
    }
}
