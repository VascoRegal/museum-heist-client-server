package protocol.messages;

import client.entities.ThiefState;

public class PartyOperationMessage extends Message {
    private int partyId;

    private int roomId;

    public PartyOperationMessage(int thiefId, ThiefState state, Command cmd, int partyId)
    {
        super(thiefId, state, cmd);
        this.partyId = partyId;
        this.roomId = -1;
    }

    public PartyOperationMessage(int thiefId, ThiefState state, Command cmd, int partyId, int roomId)
    {
        super(thiefId, state, cmd);
        this.partyId = partyId;
        this.roomId = roomId;
    }

    public int getPartyId()
    {
        return this.partyId;
    }

    public int getRoomId()
    {
        return this.roomId;
    }
}
