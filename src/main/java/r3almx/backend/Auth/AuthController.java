package r3almx.backend.Auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.jsonwebtoken.Claims;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/create/token")
    @ResponseBody
    public Map<String, String> createToken(@RequestBody JWTTokenGenerate request) {
        Map<String, String> tokenResponse = new HashMap<>();
        String accessToken = authService.createToken(request.getEmail());
        tokenResponse.put("access_token", accessToken);
        return tokenResponse;
    }

    @PostMapping("/decode/token")
    @ResponseBody
    public Map<String, String> decodeToken(@RequestBody JWTTokenDecode request) {
        Map<String, String> tokenResponse = new HashMap<>();
        Claims accessToken = authService.decodeToken(request.getToken());

        tokenResponse.put("email", accessToken.get("email").toString());
        tokenResponse.put("id", accessToken.getSubject().toString());

        tokenResponse.put("expire", accessToken.getExpiration().toString());
        return tokenResponse;
    }

}
