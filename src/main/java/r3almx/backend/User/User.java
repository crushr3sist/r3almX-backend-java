package r3almx.backend.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import r3almx.backend.Rooms.Rooms;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = true)
    private String googleId;

    @Column(unique = false, nullable = true)
    private String profilePic;

    @Column(nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(name = "user_rooms", // Join table name
            joinColumns = @JoinColumn(name = "user_id"), // User column in join table
            inverseJoinColumns = @JoinColumn(name = "room_id") // Room column in join table
    )
    @JsonBackReference
    private List<Rooms> userRooms;

    protected User() {
    }

    public User(String email, String username, String password, Optional<String> googleId,
            Optional<String> profilePic) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.googleId = googleId.orElse(null);
        this.profilePic = profilePic.orElse(null);
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getProfilePic() {
        return profilePic;
    }

    @Override
    public String toString() {
        return String.format("User[id='%s', username='%s', email='%s']", id, username, email);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<Rooms> getUserRooms() {
        return userRooms;
    }

    public void setUserRooms(List<Rooms> userRooms) {
        this.userRooms = userRooms;
    }

    public void joinRoom(Rooms room) {
        this.userRooms.add(room);
    }

    public void leaveRoom(Rooms room) {
        this.userRooms.remove(room);
    }

}