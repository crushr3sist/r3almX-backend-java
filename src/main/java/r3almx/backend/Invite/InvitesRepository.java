package r3almx.backend.Invite;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import r3almx.backend.Rooms.Rooms;

@Repository
public interface InvitesRepository extends JpaRepository<Rooms, UUID> {
    Rooms findByInviteKey(String inviteKey);

}
