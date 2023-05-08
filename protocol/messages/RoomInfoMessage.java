package protocol.messages;

/**
 * Information regarding rooms. Information passed depends on command
 */
public class RoomInfoMessage extends Message {

    /**
     * id of the room
     */
    private int roomId;

    /**
     * location of the room
     */
    private int location;

    /**
     * 
     * @param cmd
     * @param info
     */
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

            case PCKCNVAS:
                this.roomId = info;
                break;
            default:
                break;
        }
    }

    /**
     * get room id
     * @return
     */
    public int getRoomId()
    {
        return this.roomId;
    }

    /**
     * get location id
     * @return
     */
    public int getLocation()
    {
        return this.location;
    }
}
