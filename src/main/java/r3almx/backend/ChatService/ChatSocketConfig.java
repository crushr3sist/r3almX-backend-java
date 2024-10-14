package r3almx.backend.ChatService;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import r3almx.backend.Auth.JwtAuthenticationInterceptor;

@Configuration
@EnableWebSocket
public class ChatSocketConfig implements WebSocketConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    public ChatSocketConfig(JwtAuthenticationInterceptor jwtAuthenticationInterceptor) {
        this.jwtAuthenticationInterceptor = jwtAuthenticationInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new ChatSocket(), "/message")
                .addInterceptors(jwtAuthenticationInterceptor)
                .setAllowedOrigins("*");
    }
}