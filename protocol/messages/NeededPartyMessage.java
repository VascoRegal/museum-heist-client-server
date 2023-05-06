package protocol.messages;

public class NeededPartyMessage extends Message {
    
    private int partyId;

    public NeededPartyMessage(Command cmd, int partyId)
    {
        super(cmd);
        this.partyId = partyId;
    }

    public int getPartyId()
    {
        return this.partyId;
    }
}
