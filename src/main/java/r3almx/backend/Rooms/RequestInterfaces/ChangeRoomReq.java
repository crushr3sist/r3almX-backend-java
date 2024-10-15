package r3almx.backend.Rooms.RequestInterfaces;

public class ChangeRoomReq {
    private String roomId;
    private String newRoomName;

    public ChangeRoomReq(String roomId, String newRoomName) {
        this.roomId = roomId;
        this.newRoomName = newRoomName;
    }

    public String getNewRoomName() {
        return newRoomName;
    }

    public String getRoomId() {
        return roomId;
    }
}
