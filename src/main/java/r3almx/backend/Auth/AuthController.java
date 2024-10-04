package r3almx.backend.Auth;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    
    @Autowired
    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/create")
    @ResponseBody
    public Map<String, String> createToken(){
        Map<String, String> tokenResponse = new HashMap<>();
        tokenResponse.put("message", "your token");
        return tokenResponse;
    }


}
