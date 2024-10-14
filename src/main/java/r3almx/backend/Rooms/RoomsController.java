package r3almx.backend.Rooms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import r3almx.backend.Rooms.RequestInterfaces.CreateRoomReq;

@RestController
@RequestMapping("/rooms")
public class RoomsController {

    private final RoomsService roomService;

    @Autowired
    public RoomsController(RoomsService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/current_user")
    public Object current_user() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // You can use the user details now
        return userDetails;
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<?> createRoom(@RequestBody CreateRoomReq request) {
        try {
            Rooms createdRoom = roomService.createRoom(request.getRoomName());
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("status", "success");
            responseBody.put("roomId", createdRoom.getId().toString());
            responseBody.put("invitekey", createdRoom.getInviteKey());
            roomService.createRoom(request.getRoomName());
            return ResponseEntity.ok(responseBody);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    @GetMapping("/fetch")
    @ResponseBody
    public ResponseEntity<?> fetchRooms() {
        List<Rooms> _userRooms = roomService.getUserRooms();
        List<RoomDTO> roomDTO = _userRooms.stream().map(room -> new RoomDTO(room.getId(), room.getRoomName(),
                room.getInviteKey(), room.getRoomOwner().getUsername())).collect(Collectors.toList());
        return ResponseEntity.ok(roomDTO);
    }

    @PutMapping("/edit")
    @ResponseBody
    public Map<String, String> editRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public Map<String, String> deleteRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

}
