package protocol.messages;

/**
 * Message confirming party creation
 */
public class CreatedPartyMessage extends Message {
    
    /**
     * party identification
     */
    int partyId;

    /**
     * 
     * @param cmd
     * @param partyId
     */
    public CreatedPartyMessage(Command cmd, int partyId)
    {
        super(cmd);
        this.partyId = partyId;
    }

    /**
     * 
     * @return
     */
    public int getPartyId()
    {
        return this.partyId;
    }
}
