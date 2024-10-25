package r3almx.backend.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import r3almx.backend.Auth.AuthPojos.TokenResponse;
import r3almx.backend.Auth.AuthService;
import r3almx.backend.User.UserPojos.ChangeUsername;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthService authService;

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @PatchMapping("/change_username")
    public ResponseEntity<?> changeUsername(@RequestBody ChangeUsername body) {

        userService.changeName(body.getNewUsername());
        User user = AuthService.getCurrentUser();
        Map<String, Object> tokenResponse = new HashMap<>();

        TokenResponse access = authService.createToken(user.getEmail());
        tokenResponse.put("status", "200");
        tokenResponse.put("access_token", access.getToken());
        tokenResponse.put("expire_time", access.getExpireTime());
        return ResponseEntity.ok(tokenResponse);
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable UUID id, @RequestBody User user) {
        user.setId(id);
        return userService.updateUser(user);

    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
    }

}
