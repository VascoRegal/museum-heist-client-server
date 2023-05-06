package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.PartyOperationMessage;

public class PartiesMemoryStub {
    private String hostName;
    
    private int port;

    public PartiesMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    public void prepareExcursion(int partyId)
    {
        Message inMessage;
        PartyOperationMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientPartyOperationMessage(Command.PRPEXCRS, partyId);
        com.send(outMessage);

        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.exit(1);
        }
    }

    public void sendAssaultParty(int partyId)
    {
        Message inMessage;
        PartyOperationMessage outMessage;
        ClientCom com = new ClientCom(hostName, port);

        outMessage =  MessageFactory.clientPartyOperationMessage(Command.SNDPRTY, partyId);
        com.send(outMessage);

        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.exit(1);
        }
    }
}
