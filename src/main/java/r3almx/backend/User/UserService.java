package r3almx.backend.User;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import r3almx.backend.Auth.AuthService;

@Service
@Transactional
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository UserRepository) {
        this.userRepository = UserRepository;
    }

    // modifiers / editors
    public void changeName(String newUsername) {
        UUID user = AuthService.getCurrentUser().getId();
        System.out.println(user.toString());
        userRepository.updateUsernameById(newUsername, user);
    }

    public User changeProfilePic(User user) {
        return user;
    }

    public void addGoogleId(User user, String googleId) {
        UUID userId = user.getId();
        userRepository.updateGoogleIdById(googleId, userId);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        return userRepository.findUserByEmail(email);
    }

    public User getUserById(UUID Id) {
        return userRepository.findById(Id).orElse(null);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User updateUser(User user) {
        return userRepository.save(user);

    }

    public void deleteUser(UUID Id) {
        userRepository.deleteById(Id);
    }

}
