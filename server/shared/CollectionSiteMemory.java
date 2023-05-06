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

    private int[] partyAssignments;

    private MemQueue<Integer> availableThieves;

    private MemQueue<Integer> collectiningThieves;

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
        partyAssignments = new int[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            partyAssignments[i] = -1;
        }
        availableThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
        collectiningThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
    }

    public synchronized boolean getHeistStatus()
    {
        return this.heistInProgress;
    }

    public synchronized void startOperations()
    {
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        mt.setThiefState(ThiefState.PLANNING_THE_HEIST);
        this.heistInProgress = true;
    }

    
    public synchronized int amINeeded()
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

        return partyAssignments[ordinaryThiefId];
    }

    public synchronized char appraiseSit()
    {
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        if (clearedRooms == HeistConstants.NUM_ROOMS && activeParties == 0)
        {
            mt.setThiefState(ThiefState.PRESENTING_THE_REPORT);
            heistInProgress = false;
            return 's';
        }

        if (activeParties == HeistConstants.MAX_NUM_PARTIES ||
            (availableRooms == 0 && activeParties == 1))
        {
            mt.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
            return 'r';
        } else {
            mt.setThiefState(ThiefState.ASSEMBLING_A_GROUP);
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
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        int availableThiefId, partyId;
        if (activeParties == HeistConstants.MAX_NUM_PARTIES) {
            return -1;
        }

        partyId = activeParties;
        for (int i = 0; i < HeistConstants.PARTY_SIZE; i++)
        {
            availableThiefId = availableThieves.dequeue();
            ots[availableThiefId].setThiefState(ThiefState.CRAWLING_INWARDS);
            partyAssignments[availableThiefId] = partyId;
            System.out.println("Pulled " + i + "/3" + " Thieves [OT" + availableThiefId + "]");
            numAvailableThieves--;
            notifyAll();
        }

        activeParties++;
        return partyId;
    }

    public synchronized void takeARest()
    {
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        mt.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
        while (collectiningThieves.empty())
        {
            try {
                System.out.println("Taking a rest");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
}
