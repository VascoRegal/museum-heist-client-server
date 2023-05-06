package server.entities;

import protocol.communication.ServerCom;
import protocol.messages.Message;
import server.shared.GeneralMemoryInterface;

public class GeneralClientProxy extends Thread {
    private static int nProxy = 0;

    private ServerCom sconi;

    private GeneralMemoryInterface generalI;

    public GeneralClientProxy(ServerCom sconi, GeneralMemoryInterface generalI)
    {
        this.sconi = sconi;
        this.generalI = generalI;
    }

    @Override
    public void run()
    {
        Message in = null;
        Message out = null;
        in = (Message) sconi.recv();
        try 
        {
            out = generalI.process(in);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        sconi.send(out);
        sconi.close();
    }
}
