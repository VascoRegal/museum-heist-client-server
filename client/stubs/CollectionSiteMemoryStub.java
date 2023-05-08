package client.stubs;

import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.CreatedPartyMessage;
import protocol.messages.HandCanvasMessage;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.OperationDecisionMessage;
import protocol.messages.NeededPartyMessage;

/**
 * CollectionSite Stub
 * 
 * produces Messages via teh MessageFactory and sends them to the respective
 * server
 * 
 * Waits for server responses before proceeding
 */
public class CollectionSiteMemoryStub {
    
    /**
     * host of the server
     */
    private String hostName;

    /**
     * server port
     */
    private int port; 

    /**
     * server identification and stub instantiation
     * @param host
     * @param port
     */
    public CollectionSiteMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    /**
     * start operations
     */
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
            System.out.println("Invalid resp start ops");
            System.exit(1);
        }
        com.close();
    }

    /**
     * query the status of the heist
     * @return true if heist in progress, false otherwise
     */
    public boolean isHeistInProgress()
    {
        ClientCom com;
        Message outMessage, inMessage;
        boolean res = true;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientCreate(Command.HSTSTATE);
        com.send(outMessage);
        inMessage = (Message) com.recv();
        if (inMessage.getCommand() == Command.ACK)
        {
            res = true;
        } else if (inMessage.getCommand() == Command.UNACK)
        {
            res = false;
        } else {
            System.out.println("Invalid resp isHeistInProgress()");
            System.exit(1);
        }
        com.close();
        return res;
    }

    /**
     * decide which operation to do based on server resources
     * @return char
     */
    public char appraiseSit()
    {
        ClientCom com;
        Message outMessage;
        OperationDecisionMessage inMessage;

        com = new ClientCom(hostName, port);
        outMessage =MessageFactory.clientCreate(Command.APPRSIT);
        com.send(outMessage);

        inMessage = (OperationDecisionMessage) com.recv();

        com.close();

        return inMessage.getOperation();
    }


    /**
     * block until server answers to form a party
     * @return partyId
     */
    public int amINeeded()
    {
        int partyId = -1;
        ClientCom com;
        Message outMessage;
        NeededPartyMessage inMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientCreate(Command.AMNEEDED);
        com.send(outMessage);

        inMessage = (NeededPartyMessage) com.recv();

        if (inMessage.getCommand() == Command.ACKNEEDED)
        {
            partyId = inMessage.getPartyId();
        } else {
            System.out.println("Invalid resp amIneeded");
            System.exit(1);
        }
        com.close();
        return partyId;
    }

    /**
     * setup party creation
     * @return partyId
     */
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
        if (inMessage.getPartyId() == -1)
        {
            System.out.println("Invalid resp prepParty");
            System.exit(1);
        } 
        partyId = inMessage.getPartyId();
        com.close();
        return partyId;
    }

    /**
     * block until servers answers with another operation
     */
    public void takeARest()
    {
        ClientCom com;
        Message inMessage, outMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientCreate(Command.TKREST);
        com.send(outMessage);
        inMessage = (Message) com.recv();
        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp takeRest");
            System.exit(1);
        }
        com.close();
    }

    /**
     * hand a canvas to collection site
     * @param hasCanvas
     */
    public void handACanvas(boolean hasCanvas)
    {
        ClientCom com;
        HandCanvasMessage outMessage;
        Message inMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientHandCanvas(hasCanvas);

        com.send(outMessage);

        inMessage = (Message) com.recv();
        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp handCanvas");
            System.exit(1);
        }
        com.close();
    }

    /**
     * collect a canvas, called by master thief
     */
    public void collectCanvas()
    {
        ClientCom com;
        Message inMessage, outMessage;

        com = new ClientCom(hostName, port);

        outMessage = MessageFactory.clientCreate(Command.COLCNVAS);
        com.send(outMessage);

        inMessage = (Message) com.recv();
        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp collect");
            System.exit(1);
        }
        com.close();
    }
}
