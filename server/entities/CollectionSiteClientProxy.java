package server.entities;

import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.CollectionSiteMemoryInterface;

public class CollectionSiteClientProxy extends Thread {
    private static int nProxy = 0;

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
}
