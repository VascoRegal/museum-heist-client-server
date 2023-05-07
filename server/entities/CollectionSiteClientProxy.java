package server.entities;

import client.entities.ThiefCloning;
import client.entities.ThiefState;
import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.CollectionSiteMemoryInterface;

public class CollectionSiteClientProxy extends Thread implements ThiefCloning {
    private static int nProxy = 0;

    private int thiefId;

    private int partyId;

    private ThiefState state;

    private boolean canvas;

    private ServerCom sconi;

    private CollectionSiteMemoryInterface colSiteI;

    public CollectionSiteClientProxy(ServerCom sconi, CollectionSiteMemoryInterface colSiteI)
    {
        this.sconi = sconi;
        this.colSiteI = colSiteI;
    }

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
