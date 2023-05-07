package server.main;

import protocol.communication.ServerCom;
import server.entities.PartiesClientProxy;
import server.shared.PartiesMemory;
import server.shared.PartiesMemoryInterface;

public class ServerParties {
    public static void main(String[] args)
    {
        PartiesMemory partiesMemory;
        PartiesMemoryInterface conI;
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

        partiesMemory = new PartiesMemory();
        conI = new PartiesMemoryInterface(partiesMemory);
        scon = new ServerCom(port);
        scon.start();

        System.out.println("Parties Server listening @ " + port);

        PartiesClientProxy cliProxy;
        boolean running = true;

        while (running)
        {
            try {
                sconi = scon.accept();
                cliProxy = new PartiesClientProxy(sconi, conI);
                cliProxy.start();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        scon.end();
    }
}
