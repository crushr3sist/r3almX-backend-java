package r3almx.backend.ChatService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import io.jsonwebtoken.io.IOException;
import r3almx.backend.Auth.AuthRepository;
import r3almx.backend.Auth.AuthService;
import r3almx.backend.User.User;

public class ChatSocket extends TextWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ChatSocket.class);
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    private final AuthService authService;
    private final AuthRepository authRepository;

    public ChatSocket(AuthService authService, AuthRepository authRepository) {
        this.authService = authService;
        this.authRepository = authRepository;
    }

    @Override
    public void afterConnectionEstablished(@NonNull WebSocketSession session) throws Exception {
        UUID userId = UUID.fromString((String) session.getAttributes().get("userId"));
        System.out.println("user id gotten from the websocket connection: " + userId.toString());
        Optional<User> userInfo = Optional.ofNullable(authRepository.findById(userId).orElseThrow());
        logger.info("Connection established for user: " + userInfo.get().getUsername());
        sessions.put("username", session);
        session.sendMessage(new TextMessage("Connected to chat server, welcome " + userInfo.get().getUsername() + "!"));
    }

    @Override
    public void handleTextMessage(@NonNull WebSocketSession session, @NonNull TextMessage message) throws Exception {
        String username = (String) session.getAttributes().get("username");
        String payload = message.getPayload();
        logger.info("Message received from " + username + ":" + payload);
        broadcastMessage(username + ":" + payload);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) throws Exception {
        String username = (String) session.getAttributes().get("username");
        logger.info("Connection closed for user: " + username + " with status " + status);

    }

    private void broadcastMessage(String message) throws IOException {
        TextMessage textMessage = new TextMessage(message);
        for (WebSocketSession session : sessions.values()) {
            try {
                session.sendMessage(textMessage);
            } catch (java.io.IOException e) {
            }
        }
    }

}
