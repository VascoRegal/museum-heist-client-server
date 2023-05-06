package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.RoomAvailableMessage;
import protocol.messages.RoomInfoMessage;

public class MuseumMemoryStub {
    
    private String hostName;

    private int port;

    public MuseumMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    public int getAvailableRoom()
    {
        ClientCom com;
        RoomAvailableMessage inMessage;
        Message outMessage;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientCreate(Command.AVLROOM);
        com.send(outMessage);

        inMessage = (RoomAvailableMessage) com.recv();

        if (inMessage.getCommand() != Command.ACKROOM || inMessage.getRoomId() == -1)
        {
            System.out.println("Invalid resp");
            System.exit(1);
        }

        return inMessage.getRoomId();
    }

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
            System.out.println("Invalid resp");
            System.exit(1);
        }

        return inMessage.getLocation();
    }

    public boolean pickCanvas(int roomId)
    {
        ClientCom com;
        RoomInfoMessage outMessage;
        Message inMessage;
        boolean res;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientRoomCanvasPickup(roomId);
        com.send(outMessage);
        inMessage = (Message) com.recv();

        return inMessage.getCommand() == Command.ACKCNVS;
    
    }
}
