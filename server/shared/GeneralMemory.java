package server.shared;

import client.entities.ThiefState;
import consts.HeistConstants;

/**
 * General shared region
 */
public class GeneralMemory {
    
    /**
     * master thief state reference
     */
    private ThiefState mtState;

    /**
     * ordinary thief state reference
     */
    private ThiefState [] otStates;

    /**
     * instatiation
     */
    public GeneralMemory()
    {
        int i;

        this.mtState = ThiefState.PLANNING_THE_HEIST;
        this.otStates = new ThiefState[HeistConstants.NUM_THIEVES];
        for (i = 0; i < HeistConstants.NUM_THIEVES; i++)
        {
            otStates[i] = null;
        }
    }

    /**
     * initialize logging
     * @param fileName
     */
    public synchronized void init(String fileName)
    {
        // TODO: LOGS
    }

    /**
     * set the state of the MT
     * @param ts
     */
    public synchronized void setMasterThiefState(ThiefState ts)
    {
        this.mtState = ts;
        System.out.println("[GENERAL] Update MasterThief state to " + ts);
    }

    /**
     * set Ot states
     * @param thiefId
     * @param ts
     */
    public synchronized void setOrdinaryThiefState(int thiefId, ThiefState ts)
    {
        System.out.println("[GENERAL] Update OrdinaryThief_" + thiefId + " state to " + ts);
        this.otStates[thiefId] = ts;
    }
}
