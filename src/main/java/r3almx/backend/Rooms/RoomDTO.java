package r3almx.backend.Rooms;

import java.util.UUID;

public class RoomDTO {
    private UUID id;
    private String roomName;
    private String inviteKey;
    private String roomOwner;

    public RoomDTO(UUID id, String roomName, String inviteKey, String roomOwner) {
        this.id = id;
        this.roomName = roomName;
        this.inviteKey = inviteKey;
        this.roomOwner = roomOwner;
    }

    public UUID getId() {
        return id;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getInviteKey() {
        return inviteKey;
    }

    public String getRoomOwner() {
        return roomOwner;
    }
}
