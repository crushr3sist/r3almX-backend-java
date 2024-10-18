package r3almx.backend.User;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;

public interface UserRepository extends JpaRepository<User, UUID> {

    User findUserByEmail(String email);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.googleId = ?1 WHERE u.id = ?2")
    int updateGoogleIdById(String googleId, UUID id); // Update method, returns rows affected

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.profilePic = ?1 WHERE u.id = ?2")
    int updateProfilePicById(String profilePic, UUID id); // Correct JPA method

}
