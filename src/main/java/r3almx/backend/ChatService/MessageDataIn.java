package r3almx.backend.ChatService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDataIn {
    private String message;
    private String channelId;
    private String timeStamp;
}
