package client.main;

import client.entities.MasterThief;
import client.stubs.CollectionSiteMemoryStub;
import client.stubs.PartiesMemoryStub;
import client.stubs.GeneralMemoryStub;
import consts.Registry;

public class ClientMasterThief {
    public static void main(String [] args)
    {
        MasterThief mt;
        GeneralMemoryStub generalMemStub;
        CollectionSiteMemoryStub collectionSiteMemoryStub;
        PartiesMemoryStub partiesMemoryStub;

        generalMemStub = new GeneralMemoryStub(Registry.GeneralHost, Registry.GeneralPort);
        collectionSiteMemoryStub = new CollectionSiteMemoryStub(Registry.CollectionSiteHost, Registry.CollectionSitePort);
        partiesMemoryStub = new PartiesMemoryStub(Registry.PartiesHost, Registry.PartiesPort); 

        mt = new MasterThief(-1, generalMemStub, collectionSiteMemoryStub, partiesMemoryStub);

        mt.start();
    }
}
