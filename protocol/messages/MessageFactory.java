package protocol.messages;

import client.entities.Thief;
import client.entities.ThiefState;

/**
 * class with static methods to create messages more abstractly
 */
public class MessageFactory {
    /**
     * 
     */
    public MessageFactory() {}


    // Clients
    /**
     * base client message
     * @param cmd
     * @return
     */
    public static Message clientCreate(Command cmd)
    {
        Thief sender;
        Message m;
        sender = (Thief) Thread.currentThread();
        m = new Message(sender.getThiefId(), sender.getThiefState(), cmd);

        return m;
    }

    /**
     * message for status updates
     * @param state
     * @return
     */
    public static UpdateStateMessage clientStateUpdateMessage(ThiefState state)
    {
        Message base = clientCreate(Command.SET_STATE);
        UpdateStateMessage m = new UpdateStateMessage(base.getCommand(), base.getThiefId(), base.getCurrentThiefState(), state);
        return m;
    }

    /**
     * message for party operations
     * @param cmd
     * @param partyId
     * @return
     */
    public static PartyOperationMessage clientPartyOperationMessage(Command cmd, int partyId)
    {
        Message base = clientCreate(cmd);
        PartyOperationMessage m = new PartyOperationMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), partyId);
        return m;
    }

    /**
     * message for party operations
     * @param cmd
     * @param partyId
     * @param roomId
     * @return
     */
    public static PartyOperationMessage clientPartyOperationMessage(Command cmd, int partyId, int roomId)
    {
        Message base = clientCreate(cmd);
        PartyOperationMessage m = new PartyOperationMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), partyId, roomId);
        return m;
    }

    /**
     * message for room information
     * @param roomId
     * @return
     */
    public static RoomInfoMessage clientRoomLocationMessage(int roomId)
    {
        Message base = clientCreate(Command.ROOMLOC);
        RoomInfoMessage m = new RoomInfoMessage(base.getCommand(), roomId);
        return m;
    }

    /**
     * message for room information
     * @param roomId
     * @return
     */
    public static RoomInfoMessage clientRoomCompletion(int roomId)
    {
        Message base = clientCreate(Command.ROOMCMPLT);
        RoomInfoMessage m = new RoomInfoMessage(base.getCommand(), roomId);
        return m;
    }

    public static RoomInfoMessage clientRoomUpdate(int roomId)
    {
        Message base = clientCreate(Command.ROOMUPDT);
        RoomInfoMessage m = new RoomInfoMessage(base.getCommand(), roomId);
        return m;
    }

    /**
     * message for room information
     * @param roomId
     * @return
     */
    public static RoomInfoMessage clientRoomCanvasPickup(int roomId)
    {
        Message base = clientCreate(Command.PCKCNVAS);
        RoomInfoMessage m = new RoomInfoMessage(base.getCommand(), roomId);
        return m;
    }

    /**
     * message for movement operations
     * @param cmd
     * @param loc
     * @param md
     * @return
     */
    public static MovementMessage clientMovementMessage(Command cmd, int loc, int md)
    {
        Message base = clientCreate(cmd);
        MovementMessage m = new MovementMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), loc, md);
        return m;
    }

    /**
     * message for movement operations
     * @param cmd
     * @param md
     * @return
     */
    public static MovementMessage clientMovementMessage(Command cmd, int md)
    {
        Message base = clientCreate(cmd);
        MovementMessage m = new MovementMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), md);
        return m;
    }

    /**
     * message for movement operations
     * @param cmd
     * @param cnvs
     * @return
     */
    public static MovementMessage clientMovementMessage(Command cmd, boolean cnvs)
    {
        Message base = clientCreate(cmd);
        MovementMessage m = new MovementMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), cnvs);
        return m;
    }

    /**
     * canvas handling
     * @param hasCanvas
     * @return
     */
    public static HandCanvasMessage clientHandCanvas(boolean hasCanvas)
    {
        Message base = clientCreate(Command.HNDCNVAS);
        HandCanvasMessage m = new HandCanvasMessage(base.getThiefId(), base.getCurrentThiefState(), base.getCommand(), hasCanvas);
        return m;
    }



    // Servers
    /**
     * base server message
     * @param cmd
     * @return
     */
    public static Message serverCreate(Command cmd)
    {
        Message m;
        m = new Message(cmd);
        return  m;
    }

    /**
     * master thief operation decision
     * @param op
     * @return
     */
    public static OperationDecisionMessage serverOperationDecision(char op)
    {
        Message base = serverCreate(Command.ACK);
        OperationDecisionMessage m = new OperationDecisionMessage(base.getCommand(), op);
        return m;
    }

    /**
     * party creation
     * @param partyId
     * @return
     */
    public static CreatedPartyMessage serverCreatedParty(int partyId)
    {
        Message base = serverCreate(Command.ACKPRTY);
        CreatedPartyMessage m  = new CreatedPartyMessage(base.getCommand(), partyId);
        return m;
    }

    /**
     * thief needed in party message
     * @param partyId
     * @return
     */
    public static NeededPartyMessage serverNeededParty(int partyId)
    {
        Message base = serverCreate(Command.ACKNEEDED);
        NeededPartyMessage m  = new NeededPartyMessage(base.getCommand(), partyId);
        return m;
    }

    /**
     * room info messages
     * @param roomId
     * @return
     */
    public static RoomAvailableMessage serverRoomAvailable(int roomId)
    {
        Message base = serverCreate(Command.ACKROOM);
        RoomAvailableMessage m  = new RoomAvailableMessage(base.getCommand(), roomId);
        return m;
    }

    /**
     * room info messages
     * @param location
     * @return
     */
    public static RoomInfoMessage serverRoomLocationMessage(int location)
    {
        Message base = serverCreate(Command.ACKROOMLOC);
        RoomInfoMessage m = new RoomInfoMessage(base.getCommand(), location);
        return m;
    }
}
