package r3almx.backend.Rooms;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/rooms")
public class RoomsController {
    
    @GetMapping("/protected")
    public String getRooms() {
        return "shouldnt see this message";
    }
    
}
