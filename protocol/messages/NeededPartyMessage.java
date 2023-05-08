package protocol.messages;

/**
 * Message passed to OTs to notify their new party
 */
public class NeededPartyMessage extends Message {
    
    /**
     * party
     */
    private int partyId;

    /**
     * 
     * @param cmd
     * @param partyId
     */
    public NeededPartyMessage(Command cmd, int partyId)
    {
        super(cmd);
        this.partyId = partyId;
    }

    /**
     * get party
     * @return
     */
    public int getPartyId()
    {
        return this.partyId;
    }
}
