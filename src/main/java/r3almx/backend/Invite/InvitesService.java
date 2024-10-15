package r3almx.backend.Invite;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import r3almx.backend.Rooms.Rooms;
import r3almx.backend.Rooms.RoomsRepository;
import r3almx.backend.User.User;
import r3almx.backend.User.UserRepository;

@Service
@Transactional
public class InvitesService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RoomsRepository roomsRepository;

    @Autowired
    private final InvitesRepository invitesRepository;

    public InvitesService(RoomsRepository roomsRepository, InvitesRepository invitesRepository,
            r3almx.backend.User.UserRepository userRepository) {
        this.roomsRepository = roomsRepository;
        this.invitesRepository = invitesRepository;
        this.userRepository = userRepository;
    }

    /**
     * This function joins a user to a room using an invite key if the user is not
     * already a member of the room.
     * 
     * @param inviteKey A unique key that is used to identify and join a specific
     *                  room in the system.
     * @param member    The `member` parameter in the `joinRoomByInviteKey` method
     *                  represents a user who is being invited to join a room
     *                  identified by the `inviteKey`. This user will be added as a
     *                  member to the room if they are not already a member.
     */
    public void joinRoomByInviteKey(String inviteKey, User member) {
        // get the room that the user is invited to
        Rooms room = invitesRepository.findByInviteKey(inviteKey);
        // check to see if the user is already in there
        if (!(room.getMembers().contains(member))) {
            // we then add the members id to the room members list
            room.addMember(member);
            roomsRepository.save(room);
        }
        // check to see if the user is in that room again
        if (!(member.getUserRooms().contains(room))) {
            // if theyre not then we join the room
            member.joinRoom(room);
            userRepository.save(member);
        }
    }

    public String inviteLink(String roomId) {
        UUID _roomId = UUID.fromString(roomId);
        Rooms room = roomsRepository.findById(_roomId)
                .orElseThrow(() -> new IllegalArgumentException("Room not found"));

        return "http://localhost:5000/invite/" + room.getInviteKey();
    }

}
