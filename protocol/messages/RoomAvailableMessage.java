package protocol.messages;

/**
 * message displaying availiablity
 */
public class RoomAvailableMessage extends Message {
    
    /**
     * id of the room
     */
    private int roomId;

    /**
     * 
     * @param cmd
     * @param roomId
     */
    public RoomAvailableMessage(Command cmd, int roomId)
    {
        super(cmd);
        this.roomId = roomId;
    }

    /**
     * get id of room
     * @return
     */
    public int getRoomId()
    {
        return this.roomId;
    }
}
