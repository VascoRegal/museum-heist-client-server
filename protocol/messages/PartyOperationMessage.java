package protocol.messages;

import client.entities.ThiefState;

/**
 *  Message for operations on parties
 */
public class PartyOperationMessage extends Message {

    /**
     * party id
     */
    private int partyId;

    /**
     * roomId
     */
    private int roomId;

    /**
     * 
     * @param thiefId
     * @param state
     * @param cmd
     * @param partyId
     */
    public PartyOperationMessage(int thiefId, ThiefState state, Command cmd, int partyId)
    {
        super(thiefId, state, cmd);
        this.partyId = partyId;
        this.roomId = -1;
    }

    /**
     * 
     * @param thiefId
     * @param state
     * @param cmd
     * @param partyId
     * @param roomId
     */
    public PartyOperationMessage(int thiefId, ThiefState state, Command cmd, int partyId, int roomId)
    {
        super(thiefId, state, cmd);
        this.partyId = partyId;
        this.roomId = roomId;
    }

    /**
     * get the id of the party
     * @return
     */
    public int getPartyId()
    {
        return this.partyId;
    }

    /**
     * get the id of the room
     * @return
     */
    public int getRoomId()
    {
        return this.roomId;
    }
}
