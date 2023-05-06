package client.stubs;

import client.entities.ThiefState;
import protocol.communication.ClientCom;
import protocol.messages.Command;
import protocol.messages.Message;
import protocol.messages.MessageFactory;
import protocol.messages.UpdateStateMessage;

public class GeneralMemoryStub {
    
    private String hostName;

    private int port;

    public GeneralMemoryStub(String host, int port)
    {
        this.hostName = host;
        this.port = port;
    }

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
            System.out.println("Invalid resp");
            System.exit(1);
        }
        com.close();
    }

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
            System.out.println("InvalidResp");
            System.exit(1);
        }
        com.close();
    }

}
