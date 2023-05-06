package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;
import server.entities.CollectionSiteClientProxy;
import structs.MemQueue;

public class CollectionSiteMemory {
    
    private int numAvailableThieves;

    private boolean heistInProgress;

    private int activeParties;

    private int clearedRooms;

    private int availableRooms;

    private CollectionSiteClientProxy mt;

    private CollectionSiteClientProxy[] ots;

    private MemQueue<Integer> availableThieves;

    public CollectionSiteMemory()
    {
        int i;

        heistInProgress = false;
        numAvailableThieves = 0;
        activeParties = 0;
        clearedRooms = 0;
        availableRooms = HeistConstants.NUM_ROOMS;

        ots = new CollectionSiteClientProxy[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            ots[i] = null;
        }
        availableThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
    }

    public synchronized boolean getHeistStatus()
    {
        return this.heistInProgress;
    }

    public synchronized void startOperations()
    {
        this.heistInProgress = true;
    }

    
    public synchronized boolean amINeeded()
    {

        int ordinaryThiefId;

        ordinaryThiefId = ((CollectionSiteClientProxy) Thread.currentThread()).getThiefId();
        System.out.println("[OT" + ordinaryThiefId + "] :  AM I NEEDED?");
        ots[ordinaryThiefId] = (CollectionSiteClientProxy) Thread.currentThread();
        ots[ordinaryThiefId].setThiefState(ThiefState.CONCENTRATION_SITE);
        availableThieves.enqueue(ordinaryThiefId);
        this.numAvailableThieves++;
        notifyAll();

        while (((CollectionSiteClientProxy) Thread.currentThread()).getThiefState() == ThiefState.CONCENTRATION_SITE)
        {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        if (!heistInProgress) {
            return false;
        }

        return true;
    }

    public synchronized char appraiseSit()
    {
        if (clearedRooms == HeistConstants.NUM_ROOMS && activeParties == 0)
        {
            heistInProgress = false;
            return 's';
        }

        if (activeParties == HeistConstants.MAX_NUM_PARTIES ||
            (availableRooms == 0 && activeParties == 1))
        {
            return 'r';
        } else {
            while (numAvailableThieves < HeistConstants.PARTY_SIZE)
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                numAvailableThieves += 1;
            }
            return 'p';
        }
    }

    public synchronized int prepareAssaultParty()
    {
        int availableThiefId, partyId;
        if (activeParties == HeistConstants.MAX_NUM_PARTIES) {
            return -1;
        }

        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            availableThiefId = availableThieves.dequeue();
            ots[availableThiefId].setThiefState(ThiefState.CRAWLING_INWARDS);
            System.out.println("Pulled " + i + "/3" + " Thieves [OT" + availableThiefId + "]");
            numAvailableThieves--;
            notifyAll();
        }

        partyId = activeParties;
        activeParties++;
        return partyId;
    }
}
