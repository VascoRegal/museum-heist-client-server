package server.main;

import protocol.communication.ServerCom;
import server.entities.MuseumClientProxy;
import server.shared.MuseumMemory;
import server.shared.MuseumMemoryInterface;


/**
 * Instantiation of servers
 */
public class ServerMuseum {
    public static void main(String[] args)
    {
        MuseumMemory museumMemory;
        MuseumMemoryInterface conI;
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

        museumMemory = new MuseumMemory();
        conI = new MuseumMemoryInterface(museumMemory);
        scon = new ServerCom(port);
        scon.start();

        System.out.println("Museum Server listening @ " + port);

        MuseumClientProxy cliProxy;
        boolean running = true;

        while (running)
        {
            try {
                sconi = scon.accept();
                cliProxy = new MuseumClientProxy(sconi, conI);
                cliProxy.start();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        scon.end();
    }
}
