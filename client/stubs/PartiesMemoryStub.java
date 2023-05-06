package client.stubs;

import client.entities.OrdinaryThief;
import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.MovementMessage;
import protocol.messages.PartyOperationMessage;
import protocol.messages.RoomAvailableMessage;

public class PartiesMemoryStub {
    private String hostName;
    
    private int port;

    public PartiesMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    public int prepareExcursion(int partyId)
    {
        RoomAvailableMessage inMessage;
        PartyOperationMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientPartyOperationMessage(Command.PRPEXCRS, partyId);
        com.send(outMessage);

        inMessage = (RoomAvailableMessage) com.recv();

        return inMessage.getRoomId();
    }

    public void sendAssaultParty(int partyId, int roomId)
    {
        Message inMessage;
        PartyOperationMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage =  MessageFactory.clientPartyOperationMessage(Command.SNDPRTY, partyId, roomId);
        com.send(outMessage);

        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.exit(1);
        }
    }

    public void crawlIn(int location, int md)
    {
        Message inMessage;
        MovementMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage =  MessageFactory.clientMovementMessage(Command.CRWLIN, location, md);
        com.send(outMessage);

        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid Resp");
            System.exit(1);
        }
    }

    public void crawlOut()
    {
        Message inMessage, outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientCreate(Command.CRWLOT);
        com.send(outMessage);
        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid Resp");
            System.exit(1);
        }
    }
}
