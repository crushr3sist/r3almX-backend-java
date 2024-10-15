package r3almx.backend.Invite;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import r3almx.backend.Auth.AuthService;
import r3almx.backend.Rooms.Rooms;
import r3almx.backend.User.User;

@RestController()
@RequestMapping("/invite")
public class InvitesController {

    @Autowired
    private final InvitesService invitesService;

    @Autowired
    private final InvitesRepository invitesRepository;

    public InvitesController(InvitesService invitesService, InvitesRepository invitesRepository) {
        this.invitesService = invitesService;
        this.invitesRepository = invitesRepository;
    }

    @GetMapping("/get")
    public ResponseEntity<?> getInviteKey(@RequestParam("roomId") String roomId) {
        String inviteUrl = invitesService.inviteLink(roomId);
        Map<String, String> responseJson = new HashMap<>();
        responseJson.put("status", "200");
        responseJson.put("link", inviteUrl);
        return ResponseEntity.ok(responseJson);
    }

    @PostMapping("/{invite_key}")
    public ResponseEntity<?> joinRoomWithInvite(@PathVariable("invite_key") String inviteKey) {
        User member = AuthService.getCurrentUser();

        invitesService.joinRoomByInviteKey(inviteKey, member);
        Rooms roomMembers = invitesRepository.findByInviteKey(inviteKey);

        Map<String, Object> responseJson = new HashMap<>();
        responseJson.put("status", "200");
        responseJson.put("members", roomMembers.getMembers());
        return ResponseEntity.ok(responseJson);
    }

    @PutMapping("/edit/invite")
    public ResponseEntity<?> editInvite() {
        return ResponseEntity.ok("");

    }

}