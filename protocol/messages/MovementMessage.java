package protocol.messages;

import client.entities.ThiefState;

public class MovementMessage extends Message {
    private int md;

    private int location;

    public MovementMessage(int thiefId, ThiefState state, Command cmd, int md)
    {
        super(thiefId, state, cmd);
        this.md = md;
    }

    public MovementMessage(int thiefId, ThiefState state, Command cmd, int location, int md)
    {
        super(thiefId, state, cmd);
        this.location = location;
        this.md = md;
    }

    public int getLocation()
    {
        return this.location;
    }

    public int getMaxDisplacement()
    {
        return this.md;
    }
}
