package r3almx.backend.User;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
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

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy="roomOwner", cascade=CascadeType.ALL)
    private List<Rooms> userRooms;

    protected User() {
    }

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
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

    public List<Rooms> getRooms() {
        return userRooms;
    }

    public void setRooms(List<Rooms> userRooms) {
        this.userRooms = userRooms;
    }
}