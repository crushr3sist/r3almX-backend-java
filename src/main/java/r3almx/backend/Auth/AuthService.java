package r3almx.backend.Auth;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import r3almx.backend.Auth.AuthPojos.TokenResponse;
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

    public TokenResponse createToken(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user != null) {
            Date expireDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
            String token = Jwts.builder()
                    .setSubject(user.getId().toString())
                    .claim("email", email)
                    .setExpiration(expireDate)
                    .signWith(key)
                    .compact();
            return new AuthPojos.TokenResponse(token, expireDate);
        }
        return null;
    }

    public static Object getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static String getToken() {
        String token = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        System.out.println("token value from getToken" + token);
        return token;
    }

    public static String getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findUserByEmail(email);
        return user.getId().toString();
    }

    public Boolean userExists(String email) {
        User user = userRepository.findUserByEmail(email);
        return user != null;
    }

    public static User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        User user = userRepository.findUserByEmail(email);
        return user;
    }

    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");

        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                new ArrayList<>());
    }

    public Claims decodeToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public Boolean verifyTokenByExpire() {
        // we decode our token, create a new date variable and compare the tokens using
        // date operator
        // if the tokens time is less than current time, its not valid
        String token = getToken();
        Claims tokenExpire = decodeToken(token);
        Date rightNow = new Date();
        if (tokenExpire.getExpiration().before(rightNow)) {
            return true;
        }
        return false;
    }

}
