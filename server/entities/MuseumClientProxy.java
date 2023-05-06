package server.entities;

import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.MuseumMemoryInterface;

public class MuseumClientProxy extends Thread {
    private static int nProxy = 0;

    private ServerCom sconi;

    private MuseumMemoryInterface museumI;

    public MuseumClientProxy(ServerCom sconi, MuseumMemoryInterface museumI)
    {
        this.sconi = sconi;
        this.museumI = museumI;
    }

    @Override
    public void run()
    {
        Message in = null;
        Message out = null;
        in = (Message) sconi.recv();
        try 
        {
            out = museumI.process(in);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        sconi.send(out);
        sconi.close();
    }
}