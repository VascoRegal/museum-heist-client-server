package protocol.messages;

import client.entities.ThiefState;

/**
 * Message for canvas handing
 */
public class HandCanvasMessage extends Message {
    
    /**
     * canvas flag
     */
    private boolean handCanvas;

    /**
     * 
     * @param thiefId
     * @param state
     * @param cmd
     * @param hasCanvas
     */
    public HandCanvasMessage(int thiefId, ThiefState state, Command cmd, boolean hasCanvas)
    {
        super(thiefId, state, cmd);
        this.handCanvas = hasCanvas;
    }

    /**
     * canvas falg
     * @return
     */
    public boolean getCanvas()
    {
        return this.handCanvas;
    }
}
