package r3almx.backend.Auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import r3almx.backend.Auth.AuthPojos.GoogleTokenRequest;
import r3almx.backend.Auth.AuthPojos.JWTTokenDecode;
import r3almx.backend.Auth.AuthPojos.JWTTokenGenerate;
import r3almx.backend.Auth.AuthPojos.TokenResponse;
import r3almx.backend.User.User;
import r3almx.backend.User.UserRepository;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;
    @Autowired
    private final UserRepository userRepository;

    private static final String CLIENT_ID = "1033716509262-h52etdps8cab2p2ab7gfh8li40u8opsa.apps.googleusercontent.com";

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> postMethodName(@RequestBody GoogleTokenRequest request) {
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
                //check to see if the user record is null
                if (userExists!=null){
                    // if not 
                    // if user does exist then we check to see if they have a google id attached
                    // we check to see if the decoded userid from the payload matches the user record googleId
                    
                    if(userExists.getGoogleId() == null ? googleId != null : !userExists.getGoogleId().equals(googleId)){
                        userRepository.updateGoogleIdById(googleId, userExists.getId());
                    }
                }

                String accessToken = authService.createToken(email);

                return ResponseEntity.ok(new TokenResponse(accessToken, "bearer"));
            } else {
                throw new Exception("Invalid Google Token");
            }
        } catch (Exception e) {
            throw new RuntimeException("google token verification failed", e);
        }
    }

    @PostMapping("/register")
    public String postMethodName(@RequestBody String entity) {
        
        return entity;
    }
    

    @PostMapping("/create/token")
    @ResponseBody
    public ResponseEntity<?> createToken(@RequestBody JWTTokenGenerate request) {
        Map<String, String> tokenResponse = new HashMap<>();
        String accessToken = authService.createToken(request.getEmail());
        tokenResponse.put("status", "200");
        tokenResponse.put("access_token", accessToken);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/decode/token")
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
