package r3almx.backend.Connection;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import r3almx.backend.Auth.AuthService;
import r3almx.backend.User.User;

public class ConnectionSocket extends TextWebSocketHandler {

    private final Connection connectionManager = Connection.getInstance();

    @Autowired
    AuthService authService;

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession websocket) throws Exception {
        User user = AuthService.getCurrentUser();
        String userId = user.getId().toString();
        connectionManager.connect(userId);
        connectionManager.connectionSockets.put(userId, websocket);
        String initialStatus = connectionManager.getStatus(userId);
        Map<String, String> initialHandshakeMessage = new HashMap<>();

        initialHandshakeMessage.put("type", "STATUS_UPDATE");
        initialHandshakeMessage.put("status", initialStatus);
        websocket.sendMessage((WebSocketMessage<?>) initialHandshakeMessage);
    }
}
