package r3almx.backend.Connection;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import r3almx.backend.Auth.JwtAuthenticationInterceptor;

@Configuration
@EnableWebSocket
public class ConnectionSocketConfig implements WebSocketConfigurer {

    private final JwtAuthenticationInterceptor jwtAuthenticationInterceptor;

    public ConnectionSocketConfig(JwtAuthenticationInterceptor jwtAuthenticationInterceptor) {
        this.jwtAuthenticationInterceptor = jwtAuthenticationInterceptor;
    }

    @Override
    public void registerWebSocketHandlers(@NonNull WebSocketHandlerRegistry register) {
        register.addHandler(new ConnectionSocket(), "/connection")
                .addInterceptors(
                        jwtAuthenticationInterceptor)
                .setAllowedOrigins("*");
    }

}
