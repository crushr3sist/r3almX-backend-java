package r3almx.backend.Auth;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import r3almx.backend.User.User;

@Repository
public interface AuthRepository extends JpaRepository<User, UUID> {
    User findUserByEmail(String email);

    User findUserById(UUID id);

}
