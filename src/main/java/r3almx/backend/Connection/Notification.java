package r3almx.backend.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.socket.WebSocketMessage;

public class Notification {
    private final Map<String, String> notificationTypes = new HashMap<>();
    private final Connection connections = Connection.getInstance();

    public void sendNotificationToUser(String userId, WebSocketMessage<?> message) throws IOException {
        connections.sendNotification(userId, message);
    }
}
