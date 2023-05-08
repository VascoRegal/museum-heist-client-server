package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.MovementMessage;
import protocol.messages.PartyOperationMessage;
import protocol.messages.RoomAvailableMessage;

/**
 * Parties Stub
 * 
 * produces Messages via teh MessageFactory and sends them to the respective
 * server
 * 
 * Waits for server responses before proceeding
 */
public class PartiesMemoryStub {

    /**
     * server host
     */
    private String hostName;
    
    /**
     * server port
     */
    private int port;

    /**
     * 
     * @param host
     * @param port
     */
    public PartiesMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    /**
     * preaper excursion for partyId
     * @param partyId
     * @return
     */
    public int prepareExcursion(int partyId)
    {
        RoomAvailableMessage inMessage;
        PartyOperationMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientPartyOperationMessage(Command.PRPEXCRS, partyId);
        com.send(outMessage);

        inMessage = (RoomAvailableMessage) com.recv();
        com.close();
        return inMessage.getRoomId();
    }

    /**
     * send the party to room
     * @param partyId
     * @param roomId
     */
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
        com.close();
    }

    /**
     * crawl to location, send atributes as params
     * @param location
     * @param md
     */
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
            System.out.println("Invalid Resp crl in");
            System.exit(1);
        }
        com.close();
    }

    /**
     * crawl back to site, send canvas status as param
     * @param withCanvas
     */
    public void crawlOut(boolean withCanvas)
    {
        MovementMessage outMessage;
        Message inMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientMovementMessage(Command.CRWLOT, withCanvas);
        com.send(outMessage);
        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid Resp crl ot");
            System.exit(1);
        }
        com.close();
    }
}
