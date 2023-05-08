package protocol.messages;

import java.io.Serializable;

import client.entities.ThiefState;

/**
 * Base Serializable communication Object
 */
public class Message implements Serializable {

    /**
     * Command to execute
     */
    private Command command;

    /**
     * thief sending
     */
    private int thiefId;

    /**
     * thief state
     */
    private ThiefState curState;

    /**
     * 
     */
    public Message()
    {

    }

    /**
     * 
     * @param cmd
     */
    public Message(Command cmd)
    {
        this.command = cmd;
    }

    /**
     * 
     * @param thiefId
     * @param state
     * @param cmd
     */
    public Message(int thiefId, ThiefState state, Command cmd)
    {
        this.curState = state;
        this.thiefId = thiefId;
        this.command = cmd;
    }

    /**
     * get msg command
     * @return
     */
    public Command getCommand()
    {
        return this.command;
    }

    /**
     * get thief id
     * @return
     */
    public int getThiefId()
    {
        return this.thiefId;
    }

    /**
     * get thief state
     * @return
     */
    public ThiefState getCurrentThiefState()
    {
        return this.curState;
    }

    /**
     * representation
     */
    public String toString()
    {
        return String.format("Message=(thief=%s, cmd=%s)\n", ((Integer) thiefId == null) ? "Server" : String.valueOf(thiefId) ,command.toString());
    }
}
