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

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private final AuthService authService;

    private static final String CLIENT_ID = "1033716509262-h52etdps8cab2p2ab7gfh8li40u8opsa.apps.googleusercontent.com";

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/google/callback")
    public ResponseEntity<?> postMethodName(@RequestBody GoogleTokenRequest request) {
        String googleToken = request.getGoogleToken();
        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();
            GoogleIdToken idToken = verifier.verify(googleToken);

            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String userId = payload.getSubject();
                String pictureUrl = (String) payload.get("picture");
                String name = (String) payload.get("name");

                System.out.println("Google User Email: " + email);
                System.out.println("Google User Id: " + userId);

                String accessToken = authService.createToken(email);

                return ResponseEntity.ok(new TokenResponse(accessToken, "bearer"));
            } else {
                throw new Exception("Invalid Google Token");
            }
        } catch (Exception e) {
            throw new RuntimeException("google token verification failed", e);
        }
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
