package r3almx.backend.Auth;

import java.util.Date;

public class AuthPojos {

    public static class TokenResponse {
        private String token;
        private Date expireTime;

        public TokenResponse(String token, Date expireTime) {
            this.token = token;
            this.expireTime = expireTime;
        }

        public String getToken() {
            return token;
        }

        public Date getExpireTime() {
            return expireTime;
        }
    }

    public static class RegisterDTO {
        private String email;
        private String username;
        private String password;
        private String googleId;
        private String profilePic;

        public RegisterDTO(String email, String username, String password, String googleId, String profilePic) {
            this.email = email;
            this.username = username;
            this.password = password;
            this.googleId = googleId;
            this.profilePic = profilePic;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getGoogleId() {
            return googleId;
        }

        public String getProfilePic() {
            return profilePic;
        }

    }

    public static class JWTTokenGenerate {
        private String email;

        // Getters and setters
        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
    }

    public static class JWTTokenDecode {
        private String token;

        // Getters and setters
        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    public static class GoogleTokenRequest {
        private String googleToken;
        private String code;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getGoogleToken() {
            return googleToken;
        }

        public void setGoogleToken(String googleToken) {
            this.googleToken = googleToken;
        }
    }

    public static class GoogleCallbackResponse {
        private String access_token;
        private String token_type;
        private Date expire_time;
        private Boolean username_set;

        public GoogleCallbackResponse(String access_token,
                String token_type,
                Date expire_time,
                Boolean username_set) {
            this.access_token = access_token;
            this.token_type = token_type;
            this.expire_time = expire_time;
            this.username_set = username_set;
        }

        public String getAccessToken() {
            return access_token;
        }

        public String getTokenType() {
            return token_type;
        }

        public Date getExpireTime() {
            return expire_time;
        }

        public Boolean getUsernameSet() {
            return username_set;
        }

    }

}
