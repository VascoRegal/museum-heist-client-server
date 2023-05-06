package protocol.messages;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {

    private long ts;

    private Command command;

    private int thiefId;

    public Message()
    {

    }

    public Message(Command cmd)
    {
        this.command = cmd;
    }

    public Message(int thiefId, Command cmd)
    {
        this.thiefId = thiefId;
        this.command = cmd;
    }

    public void setTs()
    {
        this.ts = new Date().getTime();
    }


    public Command getCommand()
    {
        return this.command;
    }

    public int getThiefId()
    {
        return this.thiefId;
    }

    public String toString()
    {
        return String.format("Message=(thief=%s, cmd=%s, ts=%s)\n", ((Integer) thiefId == null) ? "Server" : String.valueOf(thiefId) ,command.toString(), ts);
    }
}
