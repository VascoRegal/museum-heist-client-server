package server.entities;

import client.entities.ThiefCloning;
import client.entities.ThiefState;
import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.CollectionSiteMemoryInterface;

/**
 * Proxy for object cloning and request processing
 */
public class CollectionSiteClientProxy extends Thread implements ThiefCloning {

    /**
     * thief id
     */
    private int thiefId;

    /**
     * party
     */
    private int partyId;

    /**
     * state
     */
    private ThiefState state;

    /**
     * wether or not has canvas
     */
    private boolean canvas;

    /**
     * comunication channel
     */
    private ServerCom sconi;

    /**
     * message processing interface
     */
    private CollectionSiteMemoryInterface colSiteI;

    /**
     * 
     * @param sconi
     * @param colSiteI
     */
    public CollectionSiteClientProxy(ServerCom sconi, CollectionSiteMemoryInterface colSiteI)
    {
        this.sconi = sconi;
        this.colSiteI = colSiteI;
    }

    /**
     * receive, process and reply to a message
     */
    @Override
    public void run()
    {
        Message in = null;
        Message out = null;
        in = (Message) sconi.recv();
        try 
        {
            out = colSiteI.process(in);
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

    public boolean getCanvas()
    {
        return this.canvas;
    }

    public void setCanvas(boolean cnvs)
    {
        this.canvas = cnvs;
    }
}
