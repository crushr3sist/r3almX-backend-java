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

    public JwtAuthenticationInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception {
        String token = extractToken(request);

        Claims claims = token != null ? authService.decodeToken(token) : null;
        if (claims != null) {
            attributes.put("username", authService.getCurrentUser().getUsername());
            return true;
        }
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return false;

    }

    private String extractToken(ServerHttpRequest request) {
        String bearerToken = request.getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
            WebSocketHandler wsHandler, Exception exception) {
        // This method is called after the handshake. You can add any post-handshake
        // logic here if needed.
    }

}
