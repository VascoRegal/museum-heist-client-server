package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;
import server.entities.PartiesClientProxy;
public class PartiesMemory {
    

    private PartiesClientProxy[] ots;

    private int partyMembers[];

    private int readyParties[];

    public PartiesMemory()
    {
        int i;

        ots = new PartiesClientProxy[HeistConstants.NUM_THIEVES];
        partyMembers = new int[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] = null;
            partyMembers[i] = -1;
        }

        readyParties = new int[HeistConstants.MAX_NUM_PARTIES];
        for (i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            readyParties[i] = 0;
        }
    }
    
    public synchronized void sendAssaultParty(int partyId)
    {
        readyParties[partyId] = HeistConstants.PARTY_SIZE;
        notifyAll();
    }

    public synchronized void prepareExcursion(int partyId)
    {
        int ordinaryThiefId;

        ordinaryThiefId = ((PartiesClientProxy) Thread.currentThread()).getThiefId();
        ots[ordinaryThiefId] = (PartiesClientProxy) Thread.currentThread();
        ots[ordinaryThiefId].setThiefState(ThiefState.CRAWLING_INWARDS);
        partyMembers[ordinaryThiefId] = partyId;

        while ((readyParties[partyId] == 0 ||
                partyMembers[ordinaryThiefId] != partyId))
        {
            try {
                System.out.println("[OT" + ordinaryThiefId + "] prep exurs on party " + partyId);
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("[OT" + ordinaryThiefId + "] NOTIFIED PARTY " + partyId + " to start. ME=(id=" + ordinaryThiefId + ",p=" + partyMembers[ordinaryThiefId] + ")");
        }
        System.out.println("[OT" + ordinaryThiefId + "] Proceeding ");
        partyMembers[ordinaryThiefId] = -1;
        readyParties[partyId]--;
    }

}
