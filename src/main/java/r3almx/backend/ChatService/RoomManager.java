package r3almx.backend.ChatService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import r3almx.backend.User.User;
import redis.clients.jedis.Jedis;

public class RoomManager {
    private static RoomManager instance;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final Map<String, Set<WebSocketSession>> rooms = new HashMap<>();
    private final Map<String, Channel> rabbitChannels = new HashMap<>();
    private final Map<String, String> rabbitQueues = new HashMap<>();
    private final Map<String, Object> broadcastTasks = new HashMap<>();
    private final Jedis redisClient = new Jedis();

    public static synchronized RoomManager getInstance() {
        if (instance == null) {
            instance = new RoomManager();
        }
        return instance;
    }

    public Connection getRabbitConnection() {
        try {

            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://rabbitmq:rabbitmq@localhost:5672/");
            Connection conn = null;
            return conn;
        } catch (URISyntaxException | KeyManagementException | NoSuchAlgorithmException e) {
            System.out.println("connection caused exception: " + e.toString() + "\n");
        }
        return null;
    }

    public void setupConsumer(String roomId) throws IOException {
        Channel channel = rabbitChannels.get(roomId);
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            try {
                String messageString = new String(delivery.getBody(), "UTF-8");

                MessageDataOut messageData = objectMapper.readValue(messageString, MessageDataOut.class);

                broadcastMessageToRoom(roomId, messageData);

                // saveMessageToCache(roomId, messageData);

            } catch (Exception e) {
                System.out.println("Error processing message: " + e.toString());
            }
        };

        String queueName = rabbitQueues.get(roomId);
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });
    }

    public void broadcastMessageToRoom(String roomId, MessageDataOut messageData) throws JsonProcessingException {
        Set<WebSocketSession> roomSockets = rooms.get(roomId);
        if (roomSockets != null) {
            try {

                String jsonMessage = objectMapper.writeValueAsString(messageData);

                TextMessage textMessage = new TextMessage(jsonMessage);

                for (WebSocketSession socket : roomSockets) {
                    if (socket.isOpen()) {
                        try {
                            socket.sendMessage(textMessage);
                        } catch (Exception e) {
                            System.out.println("Failed to send to socket: " + e.toString() + "\n");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Failed to broadcast message: " + e.toString() + "\n");
            }
        }
    }

    public void connectUser(String roomId, WebSocketSession websocket) throws IOException {

        Set<WebSocketSession> room = rooms.get(roomId);

        if (room == null) {
            rooms.put(roomId, new HashSet<>());
        }
        Connection connection = getRabbitConnection();
        Channel channel = connection.createChannel();
        String queueName = channel.queueDeclare(roomId, false, false, true, null).getQueue();
        rabbitChannels.put(roomId, channel);
        rabbitQueues.put(roomId, queueName);
        rooms.get(roomId).add(websocket);
    }

    public void disconnectUser(String roomId, WebSocketSession websocket) {
        Set<WebSocketSession> room = rooms.get(roomId);

        if (room != null) {
            room.remove(websocket);
            String queue = rabbitQueues.get(roomId);
            try {
                Channel channeToDelete = rabbitChannels.get(roomId);
                channeToDelete.queueDelete(queue);
            } catch (Exception e) {
                System.out.println("failed to delete queue");
            }
            rabbitChannels.remove(roomId);
            rabbitQueues.remove(roomId);
        }
    }

    public void addMessageToQueue(String roomId, MessageDataIn message, User user, String mid) throws IOException {
        Channel channel = rabbitChannels.get(roomId);
        MessageDataOut messageData = new MessageDataOut(
                message.getMessage(),
                user.getUsername(),
                user.getId().toString(),
                message.getTimeStamp(),
                mid,
                roomId,
                message.getChannelId());
        if (channel != null) {
            String jsonMessage = objectMapper.writeValueAsString(messageData);

            channel.basicPublish("", rabbitQueues.get(roomId), null, jsonMessage.getBytes());
        }
    }

    public String generateMessageId() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

}
