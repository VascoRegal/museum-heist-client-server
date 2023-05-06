package protocol.messages;

import client.entities.ThiefState;

public class PartyOperationMessage extends Message {
    private int partyId;

    public PartyOperationMessage(int thiefId, ThiefState state, Command cmd, int partyId)
    {
        super(thiefId, state, cmd);
        this.partyId = partyId;
    }

    public int getPartyId()
    {
        return this.partyId;
    }
}
