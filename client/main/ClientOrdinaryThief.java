package client.main;

import client.entities.OrdinaryThief;
import client.stubs.CollectionSiteMemoryStub;
import client.stubs.GeneralMemoryStub;
import consts.HeistConstants;
import consts.Registry;

public class ClientOrdinaryThief {
    public static void main(String[] args)
    {
        OrdinaryThief [] ots = new OrdinaryThief[HeistConstants.NUM_THIEVES];
        GeneralMemoryStub generalMemoryStub = new GeneralMemoryStub(Registry.GeneralHost, Registry.GeneralPort);
        CollectionSiteMemoryStub collectionSiteMemoryStub = new CollectionSiteMemoryStub(Registry.CollectionSiteHost, Registry.CollectionSitePort);

        for (int i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] =  new OrdinaryThief(i, generalMemoryStub, collectionSiteMemoryStub);
            ots[i].start();
        }
    }
}
