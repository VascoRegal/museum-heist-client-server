package protocol.messages;

import client.entities.Thief;
import client.entities.ThiefState;

public class MessageFactory {
    public MessageFactory() {}


    // Clients
    public static Message clientCreate(Command cmd)
    {
        Thief sender;
        Message m;
        sender = (Thief) Thread.currentThread();
        m = new Message(sender.getThiefId(), cmd);
        m.setTs();

        return m;
    }

    public static UpdateStateMessage clientStateUpdateMessage(ThiefState state)
    {
        Message base = clientCreate(Command.SET_STATE);
        UpdateStateMessage m = new UpdateStateMessage(base.getCommand(), base.getThiefId(), state);
        return m;
    }




    // Servers
    public static Message serverCreate(Command cmd)
    {
        Message m;
        m = new Message(cmd);
        m.setTs();
        return  m;
    }

    public static OperationDecisionMessage serverOperationDecision(char op)
    {
        Message base = serverCreate(Command.ACK);
        OperationDecisionMessage m = new OperationDecisionMessage(base.getCommand(), op);
        return m;
    }
}