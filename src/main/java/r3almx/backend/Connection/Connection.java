package r3almx.backend.Connection;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import redis.clients.jedis.Jedis;

public class Connection {
    private static Connection instance;
    private final Jedis redisClient = new Jedis();
    private final Map<String, String> connectionStatusCache = new HashMap<>();
    final Map<String, WebSocketSession> connectionSockets = new HashMap<>();
    private final Set<String> statusOptions = Set.of("online", "offline", "dnd", "idle");

    public static synchronized Connection getInstance() {
        if (instance == null) {
            instance = new Connection();
        }
        return instance;
    }

    public void connect(String userId) {
        connectionStatusCache.put(userId, "online");
        setStatusCache(userId, "online");
    }

    public void disconnect(String userId) {
        if (connectionStatusCache.containsKey(userId)) {
            connectionStatusCache.remove(userId);
        }
        if (connectionSockets.containsKey(userId)) {
            connectionSockets.remove(userId);
        }
        setStatusCache(userId, "offline");
    }

    public Map<String, String> getStatusCache(String userId) {
        Map<String, String> cachedStatus = redisClient.hgetAll("userStatus");
        return cachedStatus.entrySet().stream().filter(entry -> entry.getKey().equals(userId))
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue));
    }

    public Boolean isConnected(String userId) {
        return connectionStatusCache.containsKey(userId) || connectionSockets.containsKey(userId);
    }

    public String getStatus(String userId) {
        return connectionStatusCache.get(userId);
    }

    public void setStatus(String userId, String status) {
        if (statusOptions.contains(status)) {

        }
    }

    public void setStatusCache(String userId, String status) {
        redisClient.hset("userStatus", userId, status);
    }

    public void sendNotification(String userId, WebSocketMessage<?> message) throws IOException {
        WebSocketSession websocket = connectionSockets.get(userId);
        if (websocket != null) {
            websocket.sendMessage(message);
        }
    }
}
