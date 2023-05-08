package client.stubs;

import client.entities.ThiefState;
import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;

/**
 * General Stub
 * 
 * produces Messages via teh MessageFactory and sends them to the respective
 * server
 * 
 * Waits for server responses before proceeding
 */
public class GeneralMemoryStub {
    
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
    public GeneralMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

    /**
     * initiate, used for logging
     * @param logFile
     */
    public void init(String logFile)
    {
        ClientCom com;
        Message outMessage, inMessage;
        com = new ClientCom(this.hostName, this.port);
        outMessage = new Message(Command.INIT_LOG);
        com.send(outMessage);
        inMessage = (Message) com.recv();

        if (inMessage == null || inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp");
            System.exit(1);
        }
        com.close();
    }

    /**
     * Set MasterThief state
     * @param state
     */
    public void setMasterThiefState(ThiefState state)
    {
        ClientCom com;
        Message inMessage;
        UpdateStateMessage outMessage;

        com = new ClientCom(hostName, port);


        outMessage = MessageFactory.clientStateUpdateMessage(state);

        com.send(outMessage);
        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("Invalid resp setStat");
            System.exit(1);
        }
        com.close();
    }

    /**
     * set ordinary thief state
     * @param thiefId
     * @param state
     */
    public void setOrdinaryThiefState(int thiefId, ThiefState state)
    {
        ClientCom com;
        Message inMessage;
        UpdateStateMessage outMessage;

        com = new ClientCom(hostName, port);
        outMessage = MessageFactory.clientStateUpdateMessage(state);

        com.send(outMessage);
        inMessage = (Message) com.recv();

        if (inMessage.getCommand() != Command.ACK)
        {
            System.out.println("InvalidResp setStat");
            System.exit(1);
        }
        com.close();
    }

}
