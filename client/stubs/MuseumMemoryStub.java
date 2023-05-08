package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.RoomInfoMessage;

/**
 * Museum Stub
 * 
 * produces Messages via teh MessageFactory and sends them to the respective
 * server
 * 
 * Waits for server responses before proceeding
 */
public class MuseumMemoryStub {
    
    /**
     * server hostname
     */
    private String hostName;

    /**
     * server port
     */
    private int port;

    /**
     * Stub instantiation
     * 
     * @param host
     * @param port
     */
    public MuseumMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    /**
     * get the location of a room
     * @param roomId
     * @return location of room_roomId
     */
    public int getRoomLocation(int roomId)
    {
        ClientCom com;
        RoomInfoMessage inMessage, outMessage;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientRoomLocationMessage(roomId);
        com.send(outMessage);

        inMessage = (RoomInfoMessage) com.recv();

        if (inMessage.getCommand() != Command.ACKROOMLOC)
        {
            System.out.println("Invalid resp get room loc");
            System.exit(1);
        }
        com.close();
        return inMessage.getLocation();
    }

    /**
     * pick a canvas from roomId
     * @param roomId
     * @return
     */
    public boolean pickCanvas(int roomId)
    {
        ClientCom com;
        RoomInfoMessage outMessage;
        Message inMessage;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientRoomCanvasPickup(roomId);
        com.send(outMessage);
        inMessage = (Message) com.recv();
        com.close();
        return inMessage.getCommand() == Command.ACKCNVS;
    
    }
}
