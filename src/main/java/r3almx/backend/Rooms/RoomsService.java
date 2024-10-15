package r3almx.backend.Rooms;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import r3almx.backend.Auth.AuthService;
import r3almx.backend.User.User;
import r3almx.backend.User.UserRepository;

@Service
@Transactional
public class RoomsService {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoomsRepository roomsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public RoomsService(RoomsRepository roomsRepository, AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.roomsRepository = roomsRepository;
        this.userRepository = userRepository;
    }

    public Rooms createRoom(String roomName) {

        if (roomsRepository.findByRoomName(roomName).isPresent()) {
            throw new RuntimeException("Room name '" + roomName + "' already exists");
        }

        User currentUser = authService.getCurrentUser();
        List<User> initialMembers = new ArrayList<>();

        initialMembers.add(currentUser);
        Rooms room = new Rooms(roomName, currentUser, initialMembers);
        Rooms savedRoom = roomsRepository.save(room);
        currentUser.joinRoom(room);

        try {
            createRoomTables(savedRoom.getId().toString());
        } catch (InterruptedException e) {
            // Log the error and consider throwing a custom exception
        }

        return savedRoom;
    }

    @Transactional
    public void joinRoom(UUID userId, UUID roomId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));

        // Add the user to the room and the room to the user's list of rooms
        room.addMember(user);

        // Save the updates
        userRepository.save(user);
        roomsRepository.save(room);
    }

    private void createRoomTables(String _roomId) throws InterruptedException {
        String roomId = _roomId.replace("-", "_");

        String createChannelsTable = "CREATE TABLE IF NOT EXISTS channels_" + roomId + " (" +
                "id uuid PRIMARY KEY, " +
                "channel_name VARCHAR(255), " +
                "channel_description VARCHAR(255), " +
                "author uuid, " +
                "time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages_" + roomId + " (" +
                "id uuid PRIMARY KEY, " +
                "channel_id uuid, " +
                "sender_id VARCHAR(255), " +
                "message TEXT, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (channel_id) REFERENCES channels_" + roomId + "(id) ON DELETE CASCADE" +
                ");";

        try {
            // Start SQL execution
            jdbcTemplate.execute(createChannelsTable);
            jdbcTemplate.execute(createMessagesTable);
        } catch (DataAccessException e) {
            // Rollback transaction on failure
            System.err.println("Error executing SQL statements: " + e.getMessage());
            throw e; // Rethrow exception to ensure proper transaction rollback
        }
    }

    public void addMemberToRoom(UUID roomId, User member) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.addMember(member);
        roomsRepository.save(room);
    }

    public void removeMemberFromRoom(UUID roomId, User member) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.removeMember(member);
        roomsRepository.save(room);
    }

    public List<User> getRoomMembers(UUID roomId) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return room.getMembers();
    }

    public List<Rooms> getUserRooms() {
        User currentUser = authService.getCurrentUser();
        return currentUser.getUserRooms();
    }

    public List<Rooms> getUserRoomsById(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return user.getUserRooms();
    }
}
