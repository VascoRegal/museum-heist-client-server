package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;
import server.entities.PartiesClientProxy;
import structs.MemException;
import structs.MemPartyArray;
public class PartiesMemory {
    

    private PartiesClientProxy[] ots;

    private int partyMembers[];

    private int readyParties[];

    private final MemPartyArray[] parties;

    private int nextMovingThief[];

    private int partyRooms[];

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
        parties = new MemPartyArray[HeistConstants.MAX_NUM_PARTIES];
        nextMovingThief = new int[HeistConstants.MAX_NUM_PARTIES];
        partyRooms = new int[HeistConstants.MAX_NUM_PARTIES];
        for (i = 0; i < HeistConstants.MAX_NUM_PARTIES; i++)
        {
            readyParties[i] = 0;
            parties[i] = null;
            nextMovingThief[i] = -1;
            partyRooms[i] = -1;
        }
    }
    
    public synchronized void sendAssaultParty(int partyId, int roomId)
    {
        readyParties[partyId] = HeistConstants.PARTY_SIZE;
        partyRooms[partyId] = roomId;
        notifyAll();
    }

    public synchronized int prepareExcursion(int partyId)
    {
        int ordinaryThiefId;

        ordinaryThiefId = ((PartiesClientProxy) Thread.currentThread()).getThiefId();
        ots[ordinaryThiefId] = (PartiesClientProxy) Thread.currentThread();
        ots[ordinaryThiefId].setThiefState(ThiefState.CRAWLING_INWARDS);
        partyMembers[ordinaryThiefId] = partyId;

        if (parties[partyId] == null)
        {
            parties[partyId] = new MemPartyArray(new PartiesClientProxy[HeistConstants.PARTY_SIZE]);
            nextMovingThief[partyId] = ordinaryThiefId;
            System.out.println("[P" + partyId + " I AM HEAD - " + ordinaryThiefId);
        }

        try {
            parties[partyId].join(((PartiesClientProxy) Thread.currentThread()));
        } catch (MemException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true)
        {
            try {
                //System.out.println("[OT" + ordinaryThiefId + "] prep exurs on party " + partyId);
                wait();

                if (readyParties[partyId] != 0 && partyMembers[ordinaryThiefId] == partyId)
                {
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("[OT" + ordinaryThiefId + "] NOTIFIED PARTY " + partyId + " to start. ME=(id=" + ordinaryThiefId + ",p=" + partyMembers[ordinaryThiefId] + ")");
        }
        //System.out.println("[OT" + ordinaryThiefId + "] Proceeding ");
        readyParties[partyId]--;
        return partyRooms[partyId];
    }

    public synchronized void crawlingIn(int roomLocation)
    {
        PartiesClientProxy currentThief, closestThief;
        int partyId;
        currentThief = ((PartiesClientProxy) Thread.currentThread());
        partyId = partyMembers[currentThief.getThiefId()];
        parties[partyId].updateThreads();

        while (true) {

            while (nextMovingThief[partyId] != currentThief.getThiefId())
            {
                try {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            if (partyId == 0) {
                System.out.println(String.format("OT%d (pos=%d, md=%d) will try movement", currentThief.getThiefId(), currentThief.getPosition(), currentThief.getMaxDisplacement()));
            }
            while (parties[partyId].canMove() && currentThief.getPosition() < roomLocation) {

                parties[partyId].doBestMove();
            }
            closestThief = parties[partyId].getNext();
            nextMovingThief[partyId] = closestThief.getThiefId();
            notifyAll();

            if (currentThief.getPosition() >= roomLocation) {
                currentThief.setThiefState(ThiefState.AT_A_ROOM);
                currentThief.setPosition(roomLocation);
                break;
            }
        }
    }

    public synchronized void crawlingOut()
    {
        PartiesClientProxy currentThief, closestThief;
        int partyId;
        int siteLocation;

        siteLocation = 0;

        currentThief = ((PartiesClientProxy) Thread.currentThread());
        partyId = partyMembers[currentThief.getThiefId()];

        parties[partyId].updateThreads();

        if (currentThief.getThiefId() == parties[partyId].tail().getThiefId())
        {
            nextMovingThief[partyId] = parties[partyId].head().getThiefId();
            notifyAll();
        }

        while (true) {

            while (nextMovingThief[partyId] != currentThief.getThiefId())
            {
                try {
                    wait();
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            while (parties[partyId].canMove() && currentThief.getPosition() > siteLocation) {
                parties[partyId].doBestMove();
            }
            closestThief = parties[partyId].getNext();
            nextMovingThief[partyId] = closestThief.getThiefId();
            notifyAll();

            if (currentThief.getPosition() <= siteLocation) {
                currentThief.setThiefState(ThiefState.COLLECTION_SITE);
                currentThief.setPosition(siteLocation);
                
                partyMembers[currentThief.getThiefId()] = -1;
                if (currentThief.getThiefId() == parties[partyId].tail().getThiefId())
                {
                    parties[partyId] = null;
                    partyRooms[partyId] = -1;
                    nextMovingThief[partyId] = -1;
                    readyParties[partyId] = -1;
                }

                break;
            }
        }
    }
}
