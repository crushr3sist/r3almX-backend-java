package r3almx.backend.Auth.AuthPojos;

public class RegisterDTO {
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
