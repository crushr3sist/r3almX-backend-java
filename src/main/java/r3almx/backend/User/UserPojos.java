package r3almx.backend.User;

public class UserPojos {
    public static class ChangeUsername {
        private String newUsername;

        // Add a no-args constructor
        public ChangeUsername() {
        }

        // Your existing constructor
        public ChangeUsername(String newUsername) {
            this.newUsername = newUsername;
        }

        // Your existing getter
        public String getNewUsername() {
            return newUsername;
        }

        // Add a setter
        public void setNewUsername(String newUsername) {
            this.newUsername = newUsername;
        }
    }
}
