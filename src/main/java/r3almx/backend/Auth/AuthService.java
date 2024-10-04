package r3almx.backend.Auth;

import org.springframework.beans.factory.annotation.Autowired;
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

    public static boolean verifyUser(String email) {
        User _user = userRepository.findUserByEmail(email);
        if (_user != null) {
            return true;
        } else {
            return false;
        }

    }

    public static String createToken(String subject, String email) {
        if (verifyUser(email)) {

            return Jwts.builder()
                    .setSubject(subject)
                    .claim("email", email)
                    .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                    .signWith(key)
                    .compact();
        }
        return null;
    }
    public static Claims 
    

}
