package r3almx.backend.Auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.SignatureException;
import r3almx.backend.Auth.AuthPojos.GoogleTokenRequest;
import r3almx.backend.Auth.AuthPojos.JWTTokenDecode;
import r3almx.backend.Auth.AuthPojos.JWTTokenGenerate;
import r3almx.backend.Auth.AuthPojos.TokenResponse;
import r3almx.backend.User.User;
import r3almx.backend.User.UserRepository;
import r3almx.backend.User.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final UserService userService;
    private static final String CLIENT_ID = "1033716509262-h52etdps8cab2p2ab7gfh8li40u8opsa.apps.googleusercontent.com";

    public AuthController(AuthService authService, UserRepository userRepository, UserService userService) {
        this.authService = authService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> googleCallback(@RequestBody GoogleTokenRequest request) {
        String googleToken = request.getCode();
        System.out.println(googleToken);
        try {

            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken != null) {

                GoogleIdToken.Payload payload = idToken.getPayload();

                payload.entrySet().forEach(e -> System.out.println(e.toString()));

                String email = payload.getEmail();
                String googleId = payload.getSubject();
                String pictureUrl = (String) payload.get("picture");
                String name = (String) payload.get("name");

                System.out.println("Google User Email: " + email);
                System.out.println("Google User Id: " + googleId);
                // check to see if user exists
                // we get the user record from the email verified by google
                User userExists = userRepository.findUserByEmail(email);
                // check to see if the user record is null
                Boolean usernameSet;
                if (userExists == null) {
                    // Create new user
                    User newUser = new User(email, name, name, Optional.of(googleId), Optional.of(pictureUrl));
                    userService.createUser(newUser); // Save user
                    usernameSet = false;
                } else {
                    // Update existing user with Google ID if necessary
                    if (userExists.getGoogleId() == null || !userExists.getGoogleId().equals(googleId)) {
                        userRepository.updateGoogleIdById(googleId, userExists.getId());
                    }
                    if (userExists.getUsername().equals(email)
                            || userExists.getUsername().equals(userExists.getEmail())) {
                        usernameSet = false;
                    } else {
                        usernameSet = true;
                    }
                }

                TokenResponse access = authService.createToken(email);

                Map<String, Object> responseJson = new HashMap<>();
                responseJson.put("access_token", access.getToken());
                responseJson.put("token_type", "bearer");
                responseJson.put("expire_time", access.getExpireTime());
                responseJson.put("username_set", usernameSet);

                return ResponseEntity.ok(responseJson);
            } else {
                throw new Exception("Invalid Google Token");
            }
        } catch (Exception e) {
            throw new RuntimeException("google token verification failed", e);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        if (authService.userExists(user.getEmail())) {
            return new ResponseEntity<>("user exists", HttpStatus.CONFLICT);
        } else {
            Map<String, String> responseJson = new HashMap<>();
            userService.createUser(user);
            responseJson.put("status", "200");
            return ResponseEntity.ok(responseJson);
        }
    }

    @GetMapping("/token")
    public String _getToken() {
        return AuthService.getToken();
    }

    @PostMapping("/token/create")
    @ResponseBody
    public ResponseEntity<?> createToken(@RequestBody JWTTokenGenerate request) {
        Map<String, Object> tokenResponse = new HashMap<>();

        TokenResponse access = authService.createToken(request.getEmail());
        tokenResponse.put("status", "200");
        tokenResponse.put("access_token", access.getToken());
        tokenResponse.put("expire_time", access.getExpireTime());

        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/token/verify")
    @ResponseBody
    public ResponseEntity<?> verifyToken() {
        Map<String, String> responseJson = new HashMap<>();
        try {
            Boolean isValid = authService.verifyTokenByExpire();
            System.out.println("Token Verify: " + isValid);
            if (isValid) {
                responseJson.put("status", "200");
                responseJson.put("message", "token is valid");
                return ResponseEntity.ok(responseJson);
            }
        } catch (SignatureException e) {

            responseJson.put("status", "403");
            responseJson.put("message", "token expired");
        }
        return ResponseEntity.badRequest().body(responseJson);
    }

    @PostMapping("/token/decode")
    @ResponseBody
    public ResponseEntity<?> decodeToken(@RequestBody JWTTokenDecode request) {
        Map<String, String> tokenResponse = new HashMap<>();
        Claims accessToken = authService.decodeToken(request.getToken());

        tokenResponse.put("email", accessToken.get("email").toString());
        tokenResponse.put("id", accessToken.getSubject());

        tokenResponse.put("expire", accessToken.getExpiration().toString());
        return ResponseEntity.ok(tokenResponse);
    }

}
