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
        m = new Message(sender.getThiefId(), sender.getThiefState(), cmd);
        m.setTs();

        return m;
    }

    public static UpdateStateMessage clientStateUpdateMessage(ThiefState state)
    {
        Message base = clientCreate(Command.SET_STATE);
        UpdateStateMessage m = new UpdateStateMessage(base.getCommand(), base.getThiefId(), base.getCurrentThiefState(), state);
        return m;
    }

    public static PartyOperationMessage clientPartyOperationMessage(Command cmd, int partyId)
    {
        Message base = clientCreate(cmd);
        PartyOperationMessage m = new PartyOperationMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), partyId);
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

    public static CreatedPartyMessage serverCreatedParty(int partyId)
    {
        Message base = serverCreate(Command.ACKPRTY);
        CreatedPartyMessage m  = new CreatedPartyMessage(base.getCommand(), partyId);
        return m;
    }

    public static NeededPartyMessage serverNeededParty(int partyId)
    {
        Message base = serverCreate(Command.ACKNEEDED);
        NeededPartyMessage m  = new NeededPartyMessage(base.getCommand(), partyId);
        return m;
    }
}
