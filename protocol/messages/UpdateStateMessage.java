package protocol.messages;

import client.entities.ThiefState;

/**
 * message passed for state updates
 */
public class UpdateStateMessage extends Message {

    /*
     * state to update
     */
    private ThiefState state;

    /**
     * 
     * @param cmd
     * @param thiefId
     * @param curState
     * @param desiredState
     */
    public UpdateStateMessage(Command cmd, int thiefId, ThiefState curState, ThiefState desiredState)
    {
        super(thiefId, curState, cmd);
        this.state = desiredState;
    }

    /**
     * get the state
     * @return
     */
    public ThiefState getThiefState()
    {
        return this.state;
    }

    /**
     * set the state
     * @param state
     */
    public void setThiefState(ThiefState state)
    {
        this.state = state;
    }
}
