package r3almx.backend.Auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;

import r3almx.backend.User.User;
import r3almx.backend.User.UserRepository;

@Service
public class AuthService {
    private static UserRepository userRepository;
    private static final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    @Autowired
    public AuthService(UserRepository userRepository) {
        AuthService.userRepository = userRepository;
    }

    public String createToken(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {

            return Jwts.builder()
                    .setSubject(user.getId().toString())
                    .claim("email", email)
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .signWith(key)
                    .compact();
        }
        return null;
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public UserDetails loadUserByEmail(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email.toString());
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password("")
                .authorities("USER")
                .build();
    }

}
