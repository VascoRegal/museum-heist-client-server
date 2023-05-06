package server.entities;

import client.entities.ThiefCloning;
import client.entities.ThiefState;
import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.PartiesMemoryInterface;

public class PartiesClientProxy extends Thread implements ThiefCloning {
    private static int nProxy = 0;

    private int thiefId;

    private int partyId;

    private ThiefState state;

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
}
