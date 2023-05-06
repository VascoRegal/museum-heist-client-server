package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.CreatedPartyMessage;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.OperationDecisionMessage;

public class CollectionSiteMemoryStub {
    
    private String hostName;

    private int port; 

    public CollectionSiteMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    public void startOperations()
    {
        Message in;
        Message out;

        ClientCom com = new ClientCom(hostName, port);

        out = MessageFactory.clientCreate(Command.STRTOPS);
        com.send(out);

        in = (Message) com.recv();
        if (in.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp");
            System.exit(1);
        }
        com.close();
    }

    public boolean isHeistInProgress()
    {
        ClientCom com;
        Message outMessage, inMessage;
        boolean res = true;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientCreate(Command.HSTSTATE);
        com.send(outMessage);
        inMessage = (Message) com.recv();
        System.out.println(inMessage);
        if (inMessage.getCommand() == Command.ACK)
        {
            res = true;
        } else if (inMessage.getCommand() == Command.UNACK)
        {
            res = false;
        } else {
            System.out.println("Invalid resp");
            System.exit(1);
        }
        com.close();
        return res;
    }

    public char appraiseSit()
    {
        ClientCom com;
        Message outMessage;
        OperationDecisionMessage inMessage;

        com = new ClientCom(hostName, port);
        outMessage =MessageFactory.clientCreate(Command.APPRSIT);
        com.send(outMessage);

        inMessage = (OperationDecisionMessage) com.recv();
        System.out.println(inMessage);

        com.close();

        return inMessage.getOperation();
    }


    public boolean amINeeded()
    {
        boolean needed = false;
        ClientCom com;
        Message inMessage, outMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientCreate(Command.AMNEEDED);
        com.send(outMessage);

        inMessage = (Message) com.recv();

        if (inMessage.getCommand() == Command.ACK)
        {
            needed = true;
        } else if (inMessage.getCommand() == Command.UNACK)
        {
            needed = false;
        } else {
            System.out.println("Invalid resp");
            System.exit(1);
        }
        return needed;
    }

    public int prepareAssaultParty()
    {
        int partyId = -1;
        ClientCom com;
        Message outMessage;
        CreatedPartyMessage inMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientCreate(Command.PRPPRTY);
        com.send(outMessage);
        inMessage = (CreatedPartyMessage) com.recv();
        System.out.println(inMessage);
        if (inMessage.getPartyId() == -1)
        {
            System.out.println("Invalid resp");
            System.exit(1);
        } 
        partyId = inMessage.getPartyId();
        com.close();
        return partyId;
    }
}
