package protocol.messages;

public class CreatedPartyMessage extends Message {
    
    int partyId;

    public CreatedPartyMessage(Command cmd, int partyId)
    {
        super(cmd);
        this.partyId = partyId;
    }

    public int getPartyId()
    {
        return this.partyId;
    }
}
