package r3almx.backend.Rooms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import r3almx.backend.Auth.AuthService;
import r3almx.backend.Channels.ChannelService;
import r3almx.backend.Message.MessagesService;

@Service
public class RoomsService {

    @Autowired
    private final AuthService authService;

    @Autowired
    private final RoomsRepository roomsRepository;

    @Autowired
    private MessagesService messageService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomsService(RoomsRepository roomsRepository, AuthService authService) {
        this.authService = authService;
        this.roomsRepository = roomsRepository;
    }

    public Rooms createRoom(String roomName) {
        Rooms room = new Rooms();
        System.out.println(authService.getCurrentUser().toString());
        room.setRoomName(roomName);
        Rooms savedRoom = roomsRepository.save(room);

        createRoomTables(savedRoom.getId().toString());

        return savedRoom;
    }

    private void createRoomTables(String roomId) {
        // SQL for channels table creation
        String createChannelsTable = "CREATE TABLE IF NOT EXISTS channels_" + roomId + " (" +
                "id UUID PRIMARY KEY, " + // UUID primary key for channel ID
                "channel_name VARCHAR(255), " + // Channel name field
                "channel_description VARCHAR(255), " + // Channel description field
                "author UUID, " + // Author UUID
                "time_created TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"; // Time created with default timestamp

        // SQL for messages table creation
        String createMessagesTable = "CREATE TABLE IF NOT EXISTS messages_" + roomId + " (" +
                "id UUID PRIMARY KEY, " + // UUID primary key for message ID
                "channel_id UUID REFERENCES channels_" + roomId + "(id), " + // Foreign key referencing the channels
                                                                             // table
                "sender_id UUID REFERENCES users(id), " + // Foreign key referencing users table
                "message TEXT, " + // Message content
                "timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP)"; // Timestamp with default value

        // Execute the SQL statements
        jdbcTemplate.execute(createChannelsTable);
        jdbcTemplate.execute(createMessagesTable);
    }

}
