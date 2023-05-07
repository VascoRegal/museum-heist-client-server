package protocol.messages;

import client.entities.ThiefState;

public class HandCanvasMessage extends Message {
    
    private boolean handCanvas;

    public HandCanvasMessage(int thiefId, ThiefState state, Command cmd, boolean hasCanvas)
    {
        super(thiefId, state, cmd);
        this.handCanvas = hasCanvas;
    }

    public boolean getCanvas()
    {
        return this.handCanvas;
    }
}
