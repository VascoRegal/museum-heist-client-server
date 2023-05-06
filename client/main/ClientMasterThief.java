package client.main;

import client.entities.MasterThief;
import client.stubs.CollectionSiteMemoryStub;
import client.stubs.GeneralMemoryStub;
import consts.Registry;

public class ClientMasterThief {
    public static void main(String [] args)
    {
        MasterThief mt;
        GeneralMemoryStub generalMemStub;
        CollectionSiteMemoryStub collectionSiteMemoryStub;
        generalMemStub = new GeneralMemoryStub(Registry.GeneralHost, Registry.GeneralPort);
        collectionSiteMemoryStub = new CollectionSiteMemoryStub(Registry.CollectionSiteHost, Registry.CollectionSitePort);
        mt = new MasterThief(-1, generalMemStub, collectionSiteMemoryStub);

        mt.start();
    }
}
