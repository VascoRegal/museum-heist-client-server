package protocol.messages;

import client.entities.ThiefState;

public class UpdateStateMessage extends Message {
    private ThiefState state;

    public UpdateStateMessage(Command cmd, int thiefId, ThiefState curState, ThiefState desiredState)
    {
        super(thiefId, curState, cmd);
        this.state = desiredState;
    }

    public ThiefState getThiefState()
    {
        return this.state;
    }

    public void setThiefState(ThiefState state)
    {
        this.state = state;
    }
}
