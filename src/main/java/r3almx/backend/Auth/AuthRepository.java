package r3almx.backend.Auth;
import java.util.UUID;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<User, Jwt> {
    Auth createToken(String email, String Password);    
    Auth verifyToken(String token);

}
