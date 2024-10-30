package r3almx.backend.ChatService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDataOut {
    private  String message;
    private  String username;
    private  String uid;
    private  String timeStamp;
    private  String mid;
    private  String roomId;
    private  String channelId;

}
