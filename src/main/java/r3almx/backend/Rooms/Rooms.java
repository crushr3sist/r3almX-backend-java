package r3almx.backend.Rooms;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import r3almx.backend.Channels.Channels;
import r3almx.backend.User.User;

@Entity
@Table(name = "rooms")
public class Rooms {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_";
    private static final SecureRandom RANDOM = new SecureRandom();

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "room_owner")
    private User roomOwner;

    @Column(unique = true, nullable = false)
    private String roomName;

    @Column(unique = true, nullable = false)
    private String inviteKey;

    @Column(name = "members", columnDefinition = "text[]")
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> members;

    protected Rooms() {
    }

    public Rooms(String roomName, User roomOwner, List<String> members) {
        this.roomName = roomName; // Initialize roomName
        this.inviteKey = generateInviteKey();
        this.roomOwner = roomOwner; // Initialize roomOwner
        this.members = members != null ? new ArrayList<>(members) : new ArrayList<>();
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    private String generateInviteKey() {
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

    public void setInviteKey() {
        this.inviteKey = generateInviteKey();
    }

    public User getRoomOwner() {
        return roomOwner;
    }

    public void setRoomOwner(User roomOwner) {
        this.roomOwner = roomOwner;
    }

    public void changeRoomName(String newName) {
    }

    public void changeRoomDescription(String newDescription) {
    }

    public void addMember(String member) {
        if (this.members == null) {
            this.members = new ArrayList<>();
        }
        this.members.add(member);
    }

    public void removeMember(String member) {
        if (this.members != null) {
            this.members.remove(member);
        }
    }

    public boolean hasMember(String member) {
        return this.members != null && this.members.contains(member);
    }

    public List<String> getAllMembers() {
        return this.members != null ? new ArrayList<>(this.members) : new ArrayList<>();
    }

}
