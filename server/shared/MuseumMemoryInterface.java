package server.shared;

import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.RoomInfoMessage;

public class MuseumMemoryInterface {
    
    private final MuseumMemory museumMemory;

    public MuseumMemoryInterface(MuseumMemory museumMemory)
    {
        this.museumMemory = museumMemory;
    }

    public Message process(Message in)
    {
        //System.out.println(in);
        int roomId, location;
        RoomInfoMessage m;
        Command cmd;

        switch (in.getCommand())
        {
            case AVLROOM:
                roomId = museumMemory.findNonClearedRoom();
                return MessageFactory.serverRoomAvailable(roomId);

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
