package r3almx.backend.Auth;

import java.util.Map;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import io.jsonwebtoken.Claims;

@Configuration
public class JwtAuthenticationInterceptor implements HandshakeInterceptor {

    private final AuthService authService;
    private final AuthRepository authRepository;

    public JwtAuthenticationInterceptor(AuthService authService, AuthRepository authRepository) {
        this.authService = authService;
        this.authRepository = authRepository;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        System.out.println("Handshake started"); // Confirm interceptor is triggered
        String token = extractToken(request);
        System.out.println("intercept called, and token was: " + token);
        Claims claims = token != null ? authService.decodeToken(token) : null;

        if (claims != null) {
            String userId = authRepository.findUserByEmail(claims.get("email").toString()).getId().toString();
            System.out.println(userId);
            attributes.put("userId", userId);
            return true;
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;
    }

    private String extractToken(ServerHttpRequest request) {
        // Convert URI query string to extract "token" parameter manually
        String query = request.getURI().getQuery();
        if (query != null && query.contains("token=")) {
            return query.substring(query.indexOf("token=") + 6);
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Exception exception) {
    }

}
