package r3almx.backend.Auth;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import r3almx.backend.User.User;

public interface AuthRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);
    Object getCurrentUser();

}
