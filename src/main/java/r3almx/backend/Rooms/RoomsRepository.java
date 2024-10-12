package r3almx.backend.Rooms;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomsRepository extends JpaRepository<Rooms, UUID> {
    Optional<Rooms> findByRoomName(String roomName);

}
