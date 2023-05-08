package server.entities;

import client.entities.ThiefCloning;
import client.entities.ThiefState;
import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.PartiesMemoryInterface;


/**
 * Proxy for object cloning and request processing
 */
public class PartiesClientProxy extends Thread implements ThiefCloning {
    private int thiefId;

    private int partyId;

    private int position;

    private int md;

    private ThiefState state;

    private boolean hasCanvas;

    private ServerCom sconi;

    private PartiesMemoryInterface partiesI;

    public PartiesClientProxy(ServerCom sconi, PartiesMemoryInterface partiesI)
    {
        this.sconi = sconi;
        this.partiesI = partiesI;
    }

    @Override
    public void run()
    {
        Message in = null;
        Message out = null;
        in = (Message) sconi.recv();
        try 
        {
            out = partiesI.process(in);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        sconi.send(out);
        sconi.close();
    }

    @Override
    public int getThiefId() {
        return this.thiefId;
    }

    @Override
    public void setThiefId(int id)
    {
        this.thiefId = id;
    }

    @Override
    public ThiefState getThiefState() {
        return this.state;
    }

    @Override
    public void setThiefState(ThiefState state)
    {
        this.state = state;
    }

    @Override
    public int getPartyId()
    {
        return this.partyId;
    }

    @Override
    public void setPartyId(int id)
    {
        this.partyId = id;
    }

    public int getPosition()
    {
        return this.position;
    }

    public void setPosition(int pos)
    {
        this.position = pos;
    }

    public int move(int increment) {
        this.position += increment;
        return this.position;
    }

    public int getMaxDisplacement() {
        return this.md;
    }

    public void setMaxDisplacement(int md)
    {
        this.md = md;
    }

    public boolean hasCanvas()
    {
        return this.hasCanvas;
    }

    public void setCanvas(boolean cnvs)
    {
        this.hasCanvas = cnvs;
    }

}
