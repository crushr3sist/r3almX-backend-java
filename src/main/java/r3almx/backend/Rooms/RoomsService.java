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

@Service
@Transactional
public class RoomsService {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final RoomsRepository roomsRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public RoomsService(RoomsRepository roomsRepository, AuthService authService) {
        this.authService = authService;
        this.roomsRepository = roomsRepository;
    }

    public Rooms createRoom(String roomName) {
        User currentUser = authService.getCurrentUser();
        List<String> initialMembers = new ArrayList<>();
        initialMembers.add(currentUser.getId().toString());

        Rooms room = new Rooms(roomName, currentUser, initialMembers);

        Rooms savedRoom = roomsRepository.save(room);

        createRoomTables(savedRoom.getId().toString());

        return savedRoom;
    }

    private void createRoomTables(String _roomId) {
        // SQL for channels table creation
        String roomId = _roomId.replace("-", "_");
        
        String createChannelsTable = "CREATE TABLE channels_" + roomId + " (" +
                "id uuid PRIMARY KEY, " +
                "channel_name VARCHAR(255), " +
                "channel_description VARCHAR(255), " +
                "author uuid, " +
                "time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ");";

        String createMessagesTable = "CREATE TABLE messages_" + roomId + " (" +
                "id uuid PRIMARY KEY, " +
                "channel_id uuid, " +
                "sender_id uuid, " +
                "message TEXT, " +
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "FOREIGN KEY (channel_id) REFERENCES channels_" + roomId + "(id) ON DELETE CASCADE, " +
                "FOREIGN KEY (sender_id) REFERENCES users(id) ON DELETE CASCADE" +
                ");";

        try {
            // Execute the SQL statements
            jdbcTemplate.execute(createChannelsTable);
            jdbcTemplate.execute(createMessagesTable);
        } catch (DataAccessException e) {
            // Log the exception for debugging purposes
            System.err.println("Error executing SQL statements: " + e.getMessage());
        }
    }

    public void addMemberToRoom(UUID roomId, String memberName) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.addMember(memberName);
        roomsRepository.save(room);
    }

    public void removeMemberFromRoom(UUID roomId, String memberName) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        room.removeMember(memberName);
        roomsRepository.save(room);
    }

    public List<String> getRoomMembers(UUID roomId) {
        Rooms room = roomsRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Room not found"));
        return room.getAllMembers();
    }

}
