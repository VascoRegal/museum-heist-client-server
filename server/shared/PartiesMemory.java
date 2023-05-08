package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;
import server.entities.PartiesClientProxy;
import server.entities.RoomState;
import structs.MemException;
import structs.MemPartyArray;

/**
 * Parties memory region
 */
public class PartiesMemory {
    
    /**
     * ordinary thieves thread reference
     */
    private PartiesClientProxy[] ots;

    /**
     * reference to thieves party assignment
     */
    private int partyMembers[];

    /**
     * reference to parties ready to be sent
     */
    private int readyParties[];

    /**
     * array of party MemArrays for movement operations
     */
    private final MemPartyArray[] parties;

    /**
     * reference to the next thief to move
     */
    private int nextMovingThief[];

    /**
     * track of party target room
     */
    private int partyRooms[];

    /**
     * state of rooms
     */
    private RoomState rooms[];

    /**
     * constructor
     */
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
        rooms = new RoomState[HeistConstants.NUM_ROOMS];
        for (i = 0; i < HeistConstants.NUM_ROOMS; i++)
        {
            rooms[i] = RoomState.AVAILABLE;
        }
    }
    
    /**
     * send assault party to room
     * @param partyId
     * @param roomId
     */
    public synchronized void sendAssaultParty(int partyId, int roomId)
    {
        roomId = findNonClearedRoom();
        System.out.println("[PARTY_" + partyId + "] to Room_" + roomId);
        rooms[roomId] = RoomState.IN_PROGRESS;
        readyParties[partyId] = HeistConstants.PARTY_SIZE;
        partyRooms[partyId] = roomId;

        notifyAll();
    }

    /**
     * OT waits for party to start
     * @param partyId
     * @return
     */
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
                System.out.println("[PARTY_" + partyId + " OT_" + ordinaryThiefId + " preparing for excursion");
                wait();
                if (readyParties[partyId] != 0 && partyMembers[ordinaryThiefId] == partyId)
                {
                    break;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("[PARTY_" + partyId + " OT_" + ordinaryThiefId + " starting movement");
        readyParties[partyId]--;
        return partyRooms[partyId];
    }

    /**
     * start crawling movement to room
     * @param roomLocation
     */
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
            System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") trying to move");
            while (parties[partyId].canMove() && currentThief.getPosition() < roomLocation) {
                System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") can move");
                parties[partyId].doBestMove();
            }
            System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") can no longer move");
            closestThief = parties[partyId].getNext();
            nextMovingThief[partyId] = closestThief.getThiefId();
            notifyAll();

            if (currentThief.getPosition() >= roomLocation) {
                currentThief.setThiefState(ThiefState.AT_A_ROOM);
                currentThief.setPosition(roomLocation);
                System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") arrived at location");
                if (currentThief.getThiefId() == parties[partyId].tail().getThiefId())
                {
                    nextMovingThief[partyId] = -1;
                }
                break;
            }
        }
    }

    /**
     * crawl back to site
     */
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
                System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") trying to move");
                parties[partyId].doBestMove();
            }
            System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") can no longer move");

            closestThief = parties[partyId].getNext();
            nextMovingThief[partyId] = closestThief.getThiefId();
            notifyAll();

            if (currentThief.getPosition() <= siteLocation) {
                currentThief.setThiefState(ThiefState.COLLECTION_SITE);
                currentThief.setPosition(siteLocation);
                
                partyMembers[currentThief.getThiefId()] = -1;


                System.out.println(String.format("[PARTY%d] [ROOM%d] [OT%d] :  Has canvas - %s", partyId, partyRooms[partyId] ,currentThief.getThiefId(), (currentThief.hasCanvas()) ? "true" : "false" ));
                if (!currentThief.hasCanvas() && rooms[partyRooms[partyId]] != RoomState.COMPLETED)
                {
                    rooms[partyRooms[partyId]] = RoomState.COMPLETED;
                    System.out.println("UPDATED ROOM " + partyRooms[partyId] + " TO COMPLETED");
                }

                if (currentThief.getThiefId() == parties[partyId].tail().getThiefId())
                {

                    if (rooms[partyRooms[partyId]] != RoomState.COMPLETED)
                    {
                        rooms[partyRooms[partyId]] = RoomState.AVAILABLE;
                    }

                    parties[partyId] = null;
                    partyRooms[partyId] = -1;
                    nextMovingThief[partyId] = -1;
                    readyParties[partyId] = -1;
                    System.out.println("[PARTY_" + partyId + " OT_" + currentThief.getThiefId() + " (pos=" + currentThief.getPosition() + ", md=" + currentThief.getMaxDisplacement() + ") arrived at collection site");
                }

                break;
            }
        }
    }

    /**
     * find next available room
     * @return
     */
    private int findNonClearedRoom()
    {
        for (int i = 0; i < HeistConstants.NUM_ROOMS; i++)
        {
            if (rooms[i] == RoomState.AVAILABLE)
            {
                return i;
            }
        }
        return -1;
    }
}
