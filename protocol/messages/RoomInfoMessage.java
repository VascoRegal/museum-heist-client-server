package protocol.messages;

public class RoomInfoMessage extends Message {
    private int roomId;

    private int location;

    public RoomInfoMessage(Command cmd, int info)
    {
        super(cmd);
        switch(cmd)
        {
            case ROOMLOC:
                this.roomId = info;
                break;
            
            case ACKROOMLOC:
                this.location = info;
                break;
            default:
                break;
        }
    }

    public int getRoomId()
    {
        return this.roomId;
    }

    public int getLocation()
    {
        return this.location;
    }
}
