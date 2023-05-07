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

    private int[] partyCollections;

    private int[] partyClears;

    private MemQueue<Integer> availableThieves;

    private MemQueue<Integer> collectingThieves;

    private int paintings;

    public CollectionSiteMemory()
    {
        int i;

        heistInProgress = false;
        numAvailableThieves = 0;
        activeParties = 0;
        clearedRooms = 0;
        paintings = 0;
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

        partyCollections = new int[HeistConstants.MAX_NUM_PARTIES];
        partyClears = new int[HeistConstants.MAX_NUM_PARTIES];
        for (i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            partyCollections[i] = 0;
            partyClears[i] = 0;
        }

        availableThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
        collectingThieves = new MemQueue<>(new Integer[HeistConstants.NUM_THIEVES]);
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
        System.out.println("[OT" + ordinaryThiefId + "] :  Checking if I am Needed");
        ots[ordinaryThiefId] = (CollectionSiteClientProxy) Thread.currentThread();
        ots[ordinaryThiefId].setThiefState(ThiefState.CONCENTRATION_SITE);
        availableThieves.enqueue(ordinaryThiefId);
        this.numAvailableThieves++;
        notifyAll();

        while (((CollectionSiteClientProxy) Thread.currentThread()).getThiefState() == ThiefState.CONCENTRATION_SITE)
        {
            try {
                System.out.println("[OT" + ordinaryThiefId + "] :  Blocking on amINeeded, waiting to be called by MT");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }


        System.out.println("[OT" + ordinaryThiefId + "] :  Got Called to join party " + partyAssignments[ordinaryThiefId]);
        return partyAssignments[ordinaryThiefId];
    }

    public synchronized char appraiseSit()
    {


        System.out.println(String.format("[MT] : Deciding what to do with clearedRooms=%d, activeParties=%d and numThieves=%d", clearedRooms, activeParties, numAvailableThieves));
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        if (clearedRooms == HeistConstants.NUM_ROOMS && activeParties == 0)
        {
            mt.setThiefState(ThiefState.PRESENTING_THE_REPORT);
            heistInProgress = false;
            System.out.println("[MT] :  Decide to finish the heist");
            return 's';
        }

        if (activeParties == HeistConstants.MAX_NUM_PARTIES ||
            (availableRooms == 0 && activeParties == 1))
        {
            mt.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
            System.out.println("[MT] :  Decide to wait for groups");
            return 'r';
        } else {
            System.out.println("[MT] :  Decide to form a party");
            mt.setThiefState(ThiefState.ASSEMBLING_A_GROUP);
            while (numAvailableThieves < HeistConstants.PARTY_SIZE)
            {
                try {
                    System.out.println(String.format("[MT] :  Waiting for 3 thieves to be available. Current: %d / 3", numAvailableThieves));
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
                numAvailableThieves += 1;
                System.out.println(String.format("[MT] :  Thief became available. Current: %d / 3", numAvailableThieves));
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
        System.out.println("[MT] :  Preparing Assault Party #" + partyId);
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
        System.out.println("[MT] :  Taking a rest");
        mt = ((CollectionSiteClientProxy) Thread.currentThread());
        mt.setThiefState(ThiefState.WAITING_FOR_GROUP_ARRIVAL);
        while (collectingThieves.empty())
        {
            try {
                System.out.println("[MT] :  collecting queue is empty");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("[MT] :  Thief joined collecting queue, proceeding to collection");
    }

    public synchronized void handCanvas()
    {
        int ordinaryThiefId;

        ordinaryThiefId = ((CollectionSiteClientProxy) Thread.currentThread()).getThiefId();
        System.out.println("[OT" + ordinaryThiefId + "] :  handing my canvas");
        ots[ordinaryThiefId] = (CollectionSiteClientProxy) Thread.currentThread();
        ots[ordinaryThiefId].setThiefState(ThiefState.COLLECTION_SITE);
        collectingThieves.enqueue(ordinaryThiefId);
        notifyAll();

        while (((CollectionSiteClientProxy) Thread.currentThread()).getThiefState() == ThiefState.COLLECTION_SITE)
        {
            try {
                System.out.println("[OT" + ordinaryThiefId + "] :  waiting for MT to call me for collection");
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
        System.out.println("[OT" + ordinaryThiefId + "] :  Proceeding after collection");
    }

    public synchronized void collectCanvas()
    {
        CollectionSiteClientProxy collectingThief;
        int party, thiefId;

        thiefId = collectingThieves.dequeue();
        party = partyAssignments[thiefId];

        collectingThief = ots[thiefId];


        System.out.println(String.format("[MT] :  Collecting OT%d canvas from party %d", thiefId, party));

        if (collectingThief.getCanvas())
        {
            System.out.println(String.format("[MT] :  OT%d has a canvas.", thiefId));
            paintings++;
            System.out.println(String.format("[MT] :  Current num paintings: %d", paintings));
        } else {
            if (partyClears[party] == 0)
            {
                System.out.println(String.format("[MT] :  OT%d 's party (%d) cleared the room'.", thiefId, party));
                partyClears[party] = 1;
                clearedRooms++;
                System.out.println(String.format("[MT] :  Cleared rooms %d / %d ", clearedRooms, HeistConstants.NUM_ROOMS));
                availableRooms--;
            }
        }


        partyCollections[party]++;

        
        System.out.println(String.format("[MT] :  Collected %d / 3 paintings from party %d", partyCollections[party], party));
        if (partyCollections[party] == HeistConstants.PARTY_SIZE)
        {
            if (partyClears[party] == 0)
            {
                availableRooms++;
            }

            partyClears[party] = 0;
            partyAssignments[thiefId] = -1;
            partyCollections[party] = 0;
        }

        collectingThief.setThiefState(ThiefState.CONCENTRATION_SITE);
        notifyAll();
    }
}
