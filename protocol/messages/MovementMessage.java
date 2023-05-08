package protocol.messages;

import client.entities.ThiefState;

public class MovementMessage extends Message {
    private int md;

    private int location;

    private boolean hasCanvas;

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

    public MovementMessage(int thiefId, ThiefState state, Command cmd, boolean cnvs)
    {
        super(thiefId, state, cmd);
        this.hasCanvas = cnvs;
    }

    public int getLocation()
    {
        return this.location;
    }

    public int getMaxDisplacement()
    {
        return this.md;
    }

    public boolean getCanvas()
    {
        return this.hasCanvas;
    }
}
