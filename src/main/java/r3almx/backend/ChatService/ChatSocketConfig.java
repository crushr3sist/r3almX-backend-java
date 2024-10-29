package r3almx.backend.ChatService;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import r3almx.backend.Auth.AuthRepository;
import r3almx.backend.Auth.AuthService;
import r3almx.backend.Auth.JwtAuthenticationInterceptor;

@Configuration
@EnableWebSocket
public class ChatSocketConfig implements WebSocketConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;
    private final AuthService authService;
    private final AuthRepository authRepository;

    public ChatSocketConfig(AuthService authService, AuthRepository authRepository,
            JwtAuthenticationInterceptor jwtAuthenticationInterceptor) {
        this.authService = authService;
        this.authRepository = authRepository;
        this.jwtAuthenticationInterceptor = jwtAuthenticationInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatSocket(authService, authRepository), "/message")
                .addInterceptors(jwtAuthenticationInterceptor)
                .setAllowedOrigins("*");
    }
}