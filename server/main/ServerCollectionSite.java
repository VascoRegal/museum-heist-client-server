package server.main;

import protocol.communication.ServerCom;
import server.entities.CollectionSiteClientProxy;
import server.shared.CollectionSiteMemory;
import server.shared.CollectionSiteMemoryInterface;

public class ServerCollectionSite {
    public static void main(String[] args)
    {
        CollectionSiteMemory collectionSiteMemory;
        CollectionSiteMemoryInterface colI;
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

        collectionSiteMemory = new CollectionSiteMemory();
        colI = new CollectionSiteMemoryInterface(collectionSiteMemory);
        scon = new ServerCom(port);
        scon.start();

        System.out.println("Collection Server listening @ " + port);

        CollectionSiteClientProxy cliProxy;
        boolean running = true;

        while (running)
        {
            try {
                sconi = scon.accept();
                cliProxy = new CollectionSiteClientProxy(sconi, colI);
                cliProxy.start();
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        scon.end();
    }
}
