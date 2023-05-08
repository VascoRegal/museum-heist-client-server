package server.shared;

import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.RoomInfoMessage;

/**
 * 
 * Interface to process request related to the museum
 */
public class MuseumMemoryInterface {
    
    /*
     * reference to the memory
     */
    private final MuseumMemory museumMemory;

    /**
     * 
     * @param museumMemory
     */
    public MuseumMemoryInterface(MuseumMemory museumMemory)
    {
        this.museumMemory = museumMemory;
    }

    /**
     * Process a message and return response
     * @param in
     * @return
     */
    public Message process(Message in)
    {
        //System.out.println(in);
        int roomId, location;
        RoomInfoMessage m;
        Command cmd;

        switch (in.getCommand())
        {
            case ROOMLOC:
                m = (RoomInfoMessage) in;
                roomId = m.getRoomId();
                location = museumMemory.getRoomLocation(roomId);
                return MessageFactory.serverRoomLocationMessage(location);

            case PCKCNVAS:
                m = (RoomInfoMessage) in;
                roomId = m.getRoomId();
                if (museumMemory.rollACanvas(roomId)) {
                    cmd = Command.ACKCNVS;
                } else {
                    cmd = Command.UNACKCNVS;
                }
                return MessageFactory.serverCreate(cmd);

            default:
                break;
        }

        return null;
    }
}
