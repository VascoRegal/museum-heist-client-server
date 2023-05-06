package server.shared;

import consts.HeistConstants;
import server.entities.CollectionSiteClientProxy;

public class CollectionSiteMemory {
    
    private int availableThieves;

    private boolean heistInProgress;

    private int activeParties;

    private int clearedRooms;

    private int availableRooms;

    private CollectionSiteClientProxy mt;

    public CollectionSiteMemory()
    {
        heistInProgress = false;
        availableThieves = 0;
        activeParties = 0;
        clearedRooms = 0;
        availableRooms = HeistConstants.NUM_ROOMS;
    }

    public synchronized boolean getHeistStatus()
    {
        return this.heistInProgress;
    }

    public synchronized void startOperations()
    {
        this.heistInProgress = true;
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
            availableThieves = 0;
            while (availableThieves < HeistConstants.PARTY_SIZE)
            {
                try {
                    wait();
                } catch (InterruptedException e) {
                    System.exit(1);
                }
                // block
                availableThieves += 1;
            }
            return 'p';
        }
    }
}
