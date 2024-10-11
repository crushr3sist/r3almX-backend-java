package r3almx.backend.Rooms;
import java.security.SecureRandom;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import r3almx.backend.User.User;

@Entity
@Table(name="rooms")
public class Rooms {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique=true, nullable=false)
    private String roomName;

    @Column(unique=true, nullable=false)
    private String inviteKey;

    @ManyToOne
    @JoinColumn(name="room_owner")
    private User roomOwner;

    protected Rooms() {}

    public Rooms(String roomName,String inviteKey , User roomOwner) {
        this.roomName = roomName; // Initialize roomName
        this.inviteKey = inviteKey;
        this.roomOwner = roomOwner; // Initialize roomOwner
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private  String generateInviteKey() {
        StringBuilder key = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = RANDOM.nextInt(CHARACTERS.length());
            key.append(CHARACTERS.charAt(index));
        }
        return key.toString();
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getInviteKey() {
        return inviteKey;
    }

    public void setInviteKey(String inviteKey) {
        this.inviteKey = inviteKey;
    }

    public User getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(User roomOwner) {
        this.roomOwner = roomOwner;
    }
}
