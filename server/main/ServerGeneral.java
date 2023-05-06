package server.main;

import protocol.communication.ServerCom;
import server.entities.GeneralClientProxy;
import server.shared.GeneralMemory;
import server.shared.GeneralMemoryInterface;

public class ServerGeneral
{
    public static void main(String[] args)
    {
        GeneralMemory generalMemory;
        GeneralMemoryInterface generalI;
        ServerCom scon, sconi;
        int port = -1;

        if (args.length != 1)
        {
            System.out.println("Invalid port format");
            System.exit(1);
        }

        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e)
        {
            e.printStackTrace();
            System.exit(1);
        }

        generalMemory = new GeneralMemory();
        generalI = new GeneralMemoryInterface(generalMemory);
        scon = new ServerCom(port);
        scon.start();

        System.out.println("General Server listening @ " + port);

        GeneralClientProxy cliProxy;
        boolean running = true;

        while (running)
        {
            try {
                sconi = scon.accept();
                cliProxy = new GeneralClientProxy(sconi, generalI);
                cliProxy.start();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        scon.end();
    }
}