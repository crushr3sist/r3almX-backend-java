package r3almx.backend.Rooms;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rooms")
public class RoomsController {

    @GetMapping("/current_user")
    public Object current_user() {
        Object userDetails = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // You can use the user details now
        return userDetails;
    }

    @PostMapping("/create")
    @ResponseBody
    public Map<String, String> createRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @PostMapping("/ban")
    @ResponseBody
    public Map<String, String> banUserFromRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @PostMapping("/kick")
    @ResponseBody
    public Map<String, String> kickUserFromRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @GetMapping("/fetch")
    @ResponseBody

    public Map<String, String> fetchRooms() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @PutMapping("/edit")
    @ResponseBody
    public Map<String, String> editRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

    @DeleteMapping("/edit")
    @ResponseBody
    public Map<String, String> deleteRoom() {
        Map<String, String> responseBody = new HashMap<>();
        return responseBody;
    }

}
