package protocol.messages;

public class RoomAvailableMessage extends Message {
    
    private int roomId;

    public RoomAvailableMessage(Command cmd, int roomId)
    {
        super(cmd);
        this.roomId = roomId;
    }

    public int getRoomId()
    {
        return this.roomId;
    }
}
