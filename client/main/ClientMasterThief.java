package client.main;

import client.entities.MasterThief;
import client.stubs.CollectionSiteMemoryStub;
import client.stubs.PartiesMemoryStub;
import client.stubs.GeneralMemoryStub;
import client.stubs.MuseumMemoryStub;
import consts.Registry;

/**
 * Client instantiation
 */
public class ClientMasterThief {
    public static void main(String [] args)
    {
        MasterThief mt;
        GeneralMemoryStub generalMemStub;
        CollectionSiteMemoryStub collectionSiteMemoryStub;
        PartiesMemoryStub partiesMemoryStub;
        MuseumMemoryStub museumMemoryStub;

        generalMemStub = new GeneralMemoryStub(Registry.GeneralHost, Registry.GeneralPort);
        collectionSiteMemoryStub = new CollectionSiteMemoryStub(Registry.CollectionSiteHost, Registry.CollectionSitePort);
        partiesMemoryStub = new PartiesMemoryStub(Registry.PartiesHost, Registry.PartiesPort); 
        museumMemoryStub = new MuseumMemoryStub(Registry.MuseumHost, Registry.MuseumPort);

        mt = new MasterThief(-1, generalMemStub, collectionSiteMemoryStub, partiesMemoryStub, museumMemoryStub);

        mt.start();
    }
}
